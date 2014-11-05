package net.minidev.json.mapper;

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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONAware;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONObject;
import net.minidev.json.writer.JsonReader;

/**
 * @deprecated Use the {@link JsonReader} class instead.
 */
@Deprecated
public class Mapper {
	private final static ConcurrentHashMap<Type, AMapper<?>> cache;
	static {
		cache = new ConcurrentHashMap<Type, AMapper<?>>(100);

		cache.put(Date.class, BeansMapper.MAPPER_DATE);

		cache.put(int[].class, ArraysMapper.MAPPER_PRIM_INT);
		cache.put(Integer[].class, ArraysMapper.MAPPER_INT);

		cache.put(short[].class, ArraysMapper.MAPPER_PRIM_INT);
		cache.put(Short[].class, ArraysMapper.MAPPER_INT);

		cache.put(long[].class, ArraysMapper.MAPPER_PRIM_LONG);
		cache.put(Long[].class, ArraysMapper.MAPPER_LONG);

		cache.put(byte[].class, ArraysMapper.MAPPER_PRIM_BYTE);
		cache.put(Byte[].class, ArraysMapper.MAPPER_BYTE);

		cache.put(char[].class, ArraysMapper.MAPPER_PRIM_CHAR);
		cache.put(Character[].class, ArraysMapper.MAPPER_CHAR);

		cache.put(float[].class, ArraysMapper.MAPPER_PRIM_FLOAT);
		cache.put(Float[].class, ArraysMapper.MAPPER_FLOAT);

		cache.put(double[].class, ArraysMapper.MAPPER_PRIM_DOUBLE);
		cache.put(Double[].class, ArraysMapper.MAPPER_DOUBLE);

		cache.put(boolean[].class, ArraysMapper.MAPPER_PRIM_BOOL);
		cache.put(Boolean[].class, ArraysMapper.MAPPER_BOOL);

		cache.put(JSONAwareEx.class, DefaultMapper.DEFAULT);
		cache.put(JSONAware.class, DefaultMapper.DEFAULT);
		cache.put(JSONArray.class, DefaultMapper.DEFAULT);
		cache.put(JSONObject.class, DefaultMapper.DEFAULT);
	}

	public static <T> void register(Class<T> type, AMapper<T> mapper) {
		cache.put(type, mapper);
	}

	@SuppressWarnings("unchecked")
	public static <T> AMapper<T> getMapper(Type type) {
		if (type instanceof ParameterizedType)
			return getMapper((ParameterizedType) type);
		return getMapper((Class<T>) type);
	}

	/**
	 * Get the corresponding mapper Class, or create it on first call
	 * 
	 * @param type to be map
	 */
	public static <T> AMapper<T> getMapper(Class<T> type) {
		// look for cached Mapper
		@SuppressWarnings("unchecked")
		AMapper<T> map = (AMapper<T>) cache.get(type);
		if (map != null)
			return map;
		/*
		 * Special handle
		 */
		if (type instanceof Class) {
			if (Map.class.isAssignableFrom(type))
				map = new DefaultMapperCollection<T>(type);
			else if (List.class.isAssignableFrom(type))
				map = new DefaultMapperCollection<T>(type);
			if (map != null) {
				cache.put(type, map);
				return map;
			}
		}

		if (type.isArray())
			map = new ArraysMapper.GenericMapper<T>(type);
		else if (List.class.isAssignableFrom(type))
			map = new CollectionMapper.ListClass<T>(type);
		else if (Map.class.isAssignableFrom(type))
			map = new CollectionMapper.MapClass<T>(type);
		else
			// use bean class
			map = new BeansMapper.Bean<T>(type);
		cache.putIfAbsent(type, map);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T> AMapper<T> getMapper(ParameterizedType type) {
		AMapper<T> map = (AMapper<T>) cache.get(type);
		if (map != null)
			return map;

		// Type t2 = type.getRawType();
		// if (t2 instanceof Class) {
		// Class t3 = (Class) t2;
		// if (Map.class.isAssignableFrom(t3)) {
		// map = new DefaultMapperCollection(t3);
		// cache.put(type, map);
		// } else if (List.class.isAssignableFrom(t3)) {
		// map = new DefaultMapperCollection(t3);
		// cache.put(type, map);
		// }
		// return map;
		// }

		// System.out.println("add in ParamCache " + type);
		Class<T> clz = (Class<T>) type.getRawType();
		if (List.class.isAssignableFrom(clz))
			map = new CollectionMapper.ListType<T>(type);
		else if (Map.class.isAssignableFrom(clz))
			map = new CollectionMapper.MapType<T>(type);
		cache.putIfAbsent(type, map);
		return map;
	}
}