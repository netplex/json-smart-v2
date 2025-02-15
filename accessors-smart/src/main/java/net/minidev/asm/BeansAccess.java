package net.minidev.asm;

/*
 *    Copyright 2011-2024 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allow access reflect field using runtime generated accessor. BeansAccessor is faster than
 * java.lang.reflect.Method.invoke()
 *
 * @param <T> the type of the bean being accessed
 * @author uriel Chemouni
 */
public abstract class BeansAccess<T> {
  /** default constuctor */
  public BeansAccess() {
    super();
  }

  private HashMap<String, Accessor> map;
  private Accessor[] accs;

  /**
   * set Accessor
   *
   * @param accs Accessor list
   */
  protected void setAccessor(Accessor[] accs) {
    int i = 0;
    this.accs = accs;
    map = new HashMap<String, Accessor>();
    for (Accessor acc : accs) {
      acc.index = i++;
      map.put(acc.getName(), acc);
    }
  }

  /**
   * get internal map
   *
   * @return a map
   */
  public HashMap<String, Accessor> getMap() {
    return map;
  }

  /**
   * get internal accessor
   *
   * @return Accessor list
   */
  public Accessor[] getAccessors() {
    return accs;
  }

  /** cache used to store built BeansAccess */
  private static ConcurrentHashMap<Class<?>, BeansAccess<?>> cache =
      new ConcurrentHashMap<Class<?>, BeansAccess<?>>();

  // private final static ConcurrentHashMap<Type, AMapper<?>> cache;

  /**
   * return the BeansAccess corresponding to a type
   *
   * @param type to be access
   * @param <P> working type
   * @return the BeansAccess
   */
  public static <P> BeansAccess<P> get(Class<P> type) {
    return get(type, null);
  }

  /**
   * return the BeansAccess corresponding to a type
   *
   * @param filter FieldFilter
   * @param type to be access
   * @param <P> working type
   * @return the BeansAccess
   */
  public static <P> BeansAccess<P> get(Class<P> type, FieldFilter filter) {
    {
      @SuppressWarnings("unchecked")
      BeansAccess<P> access = (BeansAccess<P>) cache.get(type);
      if (access != null) return access;
    }
    // extract all access methods
    Accessor[] accs = ASMUtil.getAccessors(type, filter);

    // create new class name
    String className = type.getName();
    String accessClassName;
    if (className.startsWith("java.util."))
      accessClassName = "net.minidev.asm." + className + "AccAccess";
    else accessClassName = className.concat("AccAccess");

    // extend class base loader
    DynamicClassLoader loader = new DynamicClassLoader(type.getClassLoader());
    // try to load existing class
    Class<?> accessClass = null;
    try {
      accessClass = loader.loadClass(accessClassName);
    } catch (ClassNotFoundException ignored) {
    }

    LinkedList<Class<?>> parentClasses = getParents(type);

    // if the class do not exists build it
    if (accessClass == null) {
      BeansAccessBuilder builder = new BeansAccessBuilder(type, accs, loader);
      for (Class<?> c : parentClasses) builder.addConversion(BeansAccessConfig.classMapper.get(c));
      accessClass = builder.bulid();
    }
    try {
      @SuppressWarnings("unchecked")
      BeansAccess<P> access = (BeansAccess<P>) accessClass.newInstance();
      access.setAccessor(accs);
      cache.putIfAbsent(type, access);
      // add fieldname alias
      for (Class<?> c : parentClasses)
        addAlias(access, BeansAccessConfig.classFiledNameMapper.get(c));
      return access;
    } catch (Exception ex) {
      throw new RuntimeException("Error constructing accessor class: " + accessClassName, ex);
    }
  }

  /**
   * @param type current type
   * @return parents hierarchy
   */
  private static LinkedList<Class<?>> getParents(Class<?> type) {
    LinkedList<Class<?>> m = new LinkedList<Class<?>>();
    while (type != null && !type.equals(Object.class)) {
      m.addLast(type);
      for (Class<?> c : type.getInterfaces()) m.addLast(c);
      type = type.getSuperclass();
    }
    m.addLast(Object.class);
    return m;
  }

  /**
   * @param access accessor to use
   * @param m mapping
   */
  private static void addAlias(BeansAccess<?> access, HashMap<String, String> m) {
    // HashMap<String, String> m =
    // BeansAccessConfig.classFiledNameMapper.get(type);
    if (m == null) return;
    HashMap<String, Accessor> changes = new HashMap<String, Accessor>();
    for (Entry<String, String> e : m.entrySet()) {
      Accessor a1 = access.map.get(e.getValue());
      if (a1 != null) changes.put(e.getValue(), a1);
    }
    access.map.putAll(changes);
  }

  /**
   * set field value by field index
   *
   * @param object object to alter
   * @param methodIndex field id to update
   * @param value new value
   */
  public abstract void set(T object, int methodIndex, Object value);

  /**
   * get field value by field index
   *
   * @param object object to operate
   * @param methodIndex field number to operate
   * @return value of the field
   */
  public abstract Object get(T object, int methodIndex);

  /**
   * create a new targeted object
   *
   * @return new instance
   */
  public abstract T newInstance();

  /**
   * set field value by field name
   *
   * @param object target object
   * @param methodName methodName
   * @param value new field value
   */
  public void set(T object, String methodName, Object value) {
    int i = getIndex(methodName);
    if (i == -1)
      throw new net.minidev.asm.ex.NoSuchFieldException(
          methodName + " in " + object.getClass() + " to put value : " + value);
    set(object, i, value);
  }

  /**
   * get field value by field name
   *
   * @param object object to operate
   * @param methodName getter to call
   * @return field value returned by the getter
   */
  public Object get(T object, String methodName) {
    return get(object, getIndex(methodName));
  }

  /**
   * Returns the index of the field accessor.
   *
   * @param name field name
   * @return id of the field
   */
  public int getIndex(String name) {
    Accessor ac = map.get(name);
    if (ac == null) return -1;
    return ac.index;
  }
}
