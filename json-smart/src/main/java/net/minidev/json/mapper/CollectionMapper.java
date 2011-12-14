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
import java.util.List;
import java.util.Map;

import net.minidev.asm.BeansAccess;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONUtil;

public class CollectionMapper {
	public static class MapType<T> extends AMapper<T> {
		final ParameterizedType type;
		final Class<?> rawClass;
		final Class<?> instance;
		final BeansAccess ba;
		
		final Type keyType;
		final Type valueType;

		final Class<?> keyClass;
		final Class<?> valueClass;

		AMapper<?> subMapper;

		public MapType(ParameterizedType type) {
			this.type = type;
			this.rawClass = (Class<?>) type.getRawType();
			if (rawClass.isInterface())
				instance = JSONObject.class;
			else
				instance = rawClass;
			ba = BeansAccess.get(instance);

			keyType = type.getActualTypeArguments()[0];
			valueType = type.getActualTypeArguments()[1];
			if (keyType instanceof Class)
				keyClass = (Class<?>) keyType;
			else
				keyClass = (Class<?>) ((ParameterizedType) keyType).getRawType();
			if (valueType instanceof Class)
				valueClass = (Class<?>) valueType;
			else
				valueClass = (Class<?>) ((ParameterizedType) valueType).getRawType();
		}

		@Override
		public Object createObject() {
			return ba.newInstance();
		}

		@Override
		public AMapper<?> startArray(String key) {
			if (subMapper == null)
				subMapper = Mapper.getMapper(valueType);
			return subMapper;
		}

		@Override
		public AMapper<?> startObject(String key) {
			if (subMapper == null)
				subMapper = Mapper.getMapper(valueType);
			return subMapper;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object current, String key, Object value) {
			((Map<Object, Object>) current).put(JSONUtil.convertTo(key, keyClass),
					JSONUtil.convertTo(value, valueClass));
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object getValue(Object current, String key) {
			return ((Map<String, Object>) current).get(JSONUtil.convertTo(key, keyClass));
		}

		@Override
		public Type getType(String key) {
			return type;
		}
	};

	public static class MapClass<T> extends AMapper<T> {
		final Class<?> type;
		final Class<?> instance;
		final BeansAccess ba;
		
		AMapper<?> subMapper;

		public MapClass(Class<?> type) {
			this.type = type;
			if (type.isInterface())
				this.instance = JSONObject.class;
			else
				this.instance = type;
			this.ba = BeansAccess.get(instance);
		}

		@Override
		public Object createObject() {
			return ba.newInstance();
		}

		@Override
		public AMapper<?> startArray(String key) {
			return DefaultMapper.DEFAULT; // _ARRAY
		}

		@Override
		public AMapper<?> startObject(String key) {
			return DefaultMapper.DEFAULT; // _MAP
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValue(Object current, String key, Object value) {
			((Map<String, Object>) current).put(key, value);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object getValue(Object current, String key) {
			return ((Map<String, Object>) current).get(key);
		}

		@Override
		public Type getType(String key) {
			return type;
		}
	};

	public static class ListType<T> extends AMapper<T> {
		final ParameterizedType type;
		final Class<?> rawClass;
		final Class<?> instance;
		final BeansAccess ba;

		final Type valueType;
		final Class<?> valueClass;

		AMapper<?> subMapper;

		public ListType(ParameterizedType type) {
			this.type = type;
			this.rawClass = (Class<?>) type.getRawType();
			if (rawClass.isInterface())
				instance = JSONArray.class;
			else
				instance = rawClass;
			ba = BeansAccess.get(instance); // NEW
			valueType = type.getActualTypeArguments()[0];
			if (valueType instanceof Class)
				valueClass = (Class<?>) valueType;
			else
				valueClass = (Class<?>) ((ParameterizedType) valueType).getRawType();
		}

		@Override
		public Object createArray() {
			return ba.newInstance();
		}

		@Override
		public AMapper<?> startArray(String key) {
			if (subMapper == null)
				subMapper = Mapper.getMapper(type.getActualTypeArguments()[0]);
			return subMapper;
		}

		@Override
		public AMapper<?> startObject(String key) {
			if (subMapper == null)
				subMapper = Mapper.getMapper(type.getActualTypeArguments()[0]);
			return subMapper;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void addValue(Object current, Object value) {
			((List<Object>) current).add(JSONUtil.convertTo(value, valueClass));
		}
	};

	public static class ListClass<T> extends AMapper<T> {
		final Class<?> type;
		final Class<?> instance;
		final BeansAccess ba;
		
		AMapper<?> subMapper;

		public ListClass(Class<?> clazz) {
			this.type = clazz;
			if (clazz.isInterface())
				instance = JSONArray.class;
			else
				instance = clazz;
			ba = BeansAccess.get(instance);
		}

		@Override
		public Object createArray() {
			return ba.newInstance();
		}

		@Override
		public AMapper<?> startArray(String key) {
			return DefaultMapper.DEFAULT;// _ARRAY;
		}

		@Override
		public AMapper<?> startObject(String key) {
			return DefaultMapper.DEFAULT;// _MAP;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void addValue(Object current, Object value) {
			((List<Object>) current).add(value);
		}
	};
}
