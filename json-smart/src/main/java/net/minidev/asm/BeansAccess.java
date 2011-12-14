package net.minidev.asm;

/*
 *    Copyright 2011 JSON-SMART authors
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
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Allow access reflect field using runtime generated accessor. BeansAccessor is
 * faster than java.lang.reflect.Method.invoke()
 * 
 * @author uriel Chemouni
 */
public abstract class BeansAccess {
	static private String MethodAccessName = BeansAccess.class.getName().replace('.', '/');
	private HashMap<String, Accessor> map;
	// private Map<String, Accessor> map;
	private Accessor[] accs;

	private void setAccessor(Accessor[] accs) {
		int i = 0;
		this.accs = accs;
		map = new HashMap<String, Accessor>();
		for (Accessor acc : accs) {
			acc.index = i++;
			map.put(acc.getName(), acc);
		}
	}

	public HashMap<String, Accessor> getMap() {
		return map;
	}

	public Accessor[] getAccessors() {
		return accs;
	}

	/**
	 * cache used to store built BeansAccess
	 */
	private static ConcurrentHashMap<Class<?>, BeansAccess> cache = new ConcurrentHashMap<Class<?>, BeansAccess>();

	// private final static ConcurrentHashMap<Type, AMapper<?>> cache;

	/**
	 * return the BeansAccess corresponding to a type
	 * 
	 * @param type
	 *            to be access
	 * @return the BeansAccess
	 */
	static public BeansAccess get(Class<?> type) {
		{
			BeansAccess access = cache.get(type);
			if (access != null)
				return access;
		}
		// extend class base loader
		DynamicClassLoader loader = new DynamicClassLoader(type.getClassLoader());

		// extract all access methodes
		Accessor[] accs = ASMUtil.getAccessors(type);

		// create new class name
		String accessClassName = type.getName().concat("AccAccess");

		// try to load existing class
		Class<?> accessClass = null;
		try {
			accessClass = loader.loadClass(accessClassName);
		} catch (ClassNotFoundException ignored) {
		}

		// if the class do not exists build it
		if (accessClass == null) {
			accessClass = buildClass(type, accs, loader);
		}

		try {
			BeansAccess access = (BeansAccess) accessClass.newInstance();
			access.setAccessor(accs);
			cache.putIfAbsent(type, access);
			return access;
		} catch (Exception ex) {
			throw new RuntimeException("Error constructing accessor class: " + accessClassName, ex);
		}
	}

	/**
	 * Build reflect bytecode from accessor list.
	 * 
	 * @param type
	 *            type to be access
	 * @param accs
	 *            used accessor
	 * @param loader
	 *            Loader used to store the generated class
	 * 
	 * @return the new reflect class
	 */
	private static Class<?> buildClass(Class<?> type, Accessor[] accs, DynamicClassLoader loader) {
		String className = type.getName();
		String accessClassName = className.concat("AccAccess");

		String accessClassNameInternal = accessClassName.replace('.', '/');
		String classNameInternal = className.replace('.', '/');

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		MethodVisitor mv;

		cw.visit(Opcodes.V1_6, ACC_PUBLIC + Opcodes.ACC_SUPER, accessClassNameInternal, null, MethodAccessName, null);
		// init
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, MethodAccessName, "<init>", "()V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		// setter
		{
			mv = cw.visitMethod(ACC_PUBLIC, "set", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
			mv.visitCode();
			if (accs.length > 0) {
				Label defaultLabel = new Label();
				Label[] labels = ASMUtil.newLabels(accs.length);

				mv.visitVarInsn(ILOAD, 2);
				mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
				int i = 0;
				for (Accessor acc : accs) {
					mv.visitLabel(labels[i++]);
					if (!acc.isWritable()) {
						mv.visitInsn(RETURN);
						continue;
					}
					mv.visitVarInsn(ALOAD, 1);
					mv.visitTypeInsn(CHECKCAST, classNameInternal);
					mv.visitVarInsn(ALOAD, 3);
					Type fieldType = Type.getType(acc.getType());
					ASMUtil.autoUnBoxing2(mv, fieldType);
					if (acc.isPublic()) {
						mv.visitFieldInsn(PUTFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
					} else {
						String sig = Type.getMethodDescriptor(acc.setter);
						mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.setter.getName(), sig);
					}
					mv.visitInsn(RETURN);
				}
				mv.visitLabel(defaultLabel);
			}
			mv.visitInsn(RETURN);
			// mv.visitMaxs(2, 4);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		// getter
		{
			mv = cw.visitMethod(ACC_PUBLIC, "get", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
			mv.visitCode();
			if (accs.length > 0) {
				mv.visitVarInsn(ILOAD, 2);
				Label[] labels = ASMUtil.newLabels(accs.length);
				Label defaultLabel = new Label();
				mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

				int i = 0;
				for (Accessor acc : accs) {
					mv.visitLabel(labels[i++]);
					mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
					if (!acc.isReadable()) {
						mv.visitInsn(Opcodes.ACONST_NULL);
						mv.visitInsn(ARETURN);
						continue;
					}
					mv.visitVarInsn(ALOAD, 1);
					mv.visitTypeInsn(CHECKCAST, classNameInternal);
					Type fieldType = Type.getType(acc.getType());

					if (acc.isPublic()) {
						mv.visitFieldInsn(GETFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
					} else {
						String sig = Type.getMethodDescriptor(acc.getter);
						mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.getter.getName(), sig);
					}
					ASMUtil.autoBoxing(mv, fieldType);
					mv.visitInsn(ARETURN);
				}
				mv.visitLabel(defaultLabel);
			}
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(Opcodes.ACONST_NULL);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 3);
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod(ACC_PUBLIC, "newInstance", "()Ljava/lang/Object;", null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, classNameInternal);
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, classNameInternal, "<init>", "()V");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}

		cw.visitEnd();

		byte[] data = cw.toByteArray();
		// debug
		// {
		// try {
		// File dest = new
		// File("C:/project/google-code/1.1/json-smart/target/test-classes/"
		// + accessClassName.replace('.', '/') + ".class");
		// dest.getParentFile().mkdirs();
		// FileOutputStream fos = new FileOutputStream(dest);
		// fos.write(data);
		// fos.close();
		// } catch (Exception e) {
		// }
		// }
		return loader.defineClass(accessClassName, data);
	}

	/**
	 * set field value by field index
	 */
	abstract public void set(Object object, int methodIndex, Object value);

	/**
	 * get field value by field index
	 */
	abstract public Object get(Object object, int methodIndex);

	/**
	 * create a new targeted object
	 */
	abstract public Object newInstance();

	/**
	 * set field value by fieldname
	 */
	public void set(Object object, String methodName, Object value) {
		set(object, getIndex(methodName), value);
	}

	/**
	 * get field value by fieldname
	 */
	public Object get(Object object, String methodName) {
		return get(object, getIndex(methodName));
	}

	/**
	 * Returns the index of the field accessor.
	 */
	public int getIndex(String name) {
		Accessor ac = map.get(name);
		if (ac == null)
			return -1;
		return ac.index;
	}
}
