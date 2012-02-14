package net.minidev.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minidev.asm.FieldFilter;
import net.minidev.json.annotate.JsonIgnore;

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
public class JSONUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertToStrict(Object obj, Class<?> dest) {
		if (obj == null)
			return null;
		if (dest.isAssignableFrom(obj.getClass()))
			return obj;
		if (dest.isPrimitive()) {
			if (dest == int.class)
				if (obj instanceof Number)
					return ((Number) obj).intValue();
				else
					return Integer.valueOf(obj.toString());
			else if (dest == short.class)
				if (obj instanceof Number)
					return ((Number) obj).shortValue();
				else
					return Short.valueOf(obj.toString());
			else if (dest == long.class)
				if (obj instanceof Number)
					return ((Number) obj).longValue();
				else
					return Long.valueOf(obj.toString());
			else if (dest == byte.class)
				if (obj instanceof Number)
					return ((Number) obj).byteValue();
				else
					return Byte.valueOf(obj.toString());
			else if (dest == float.class)
				if (obj instanceof Number)
					return ((Number) obj).floatValue();
				else
					return Float.valueOf(obj.toString());
			else if (dest == double.class)
				if (obj instanceof Number)
					return ((Number) obj).doubleValue();
				else
					return Double.valueOf(obj.toString());
			else if (dest == char.class) {
				String asString = dest.toString();
				if (asString.length() > 0)
					return Character.valueOf(asString.charAt(0));
			} else if (dest == boolean.class) {
				return (Boolean) obj;
			}
			throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to "
					+ dest.getName());
		} else {
			if (dest.isEnum())
				return Enum.valueOf((Class<Enum>) dest, obj.toString());
			if (dest == Integer.class)
				if (obj instanceof Number)
					return Integer.valueOf(((Number) obj).intValue());
				else
					return Integer.valueOf(obj.toString());
			if (dest == Long.class)
				if (obj instanceof Number)
					return Long.valueOf(((Number) obj).longValue());
				else
					return Long.valueOf(obj.toString());
			if (dest == Short.class)
				if (obj instanceof Number)
					return Short.valueOf(((Number) obj).shortValue());
				else
					return Short.valueOf(obj.toString());
			if (dest == Byte.class)
				if (obj instanceof Number)
					return Byte.valueOf(((Number) obj).byteValue());
				else
					return Byte.valueOf(obj.toString());
			if (dest == Float.class)
				if (obj instanceof Number)
					return Float.valueOf(((Number) obj).floatValue());
				else
					return Float.valueOf(obj.toString());
			if (dest == Double.class)
				if (obj instanceof Number)
					return Double.valueOf(((Number) obj).doubleValue());
				else
					return Double.valueOf(obj.toString());
			if (dest == Character.class) {
				String asString = dest.toString();
				if (asString.length() > 0)
					return Character.valueOf(asString.charAt(0));
			}
			throw new RuntimeException("Object: Can not Convert " + obj.getClass().getName() + " to " + dest.getName());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertToX(Object obj, Class<?> dest) {
		if (obj == null)
			return null;
		if (dest.isAssignableFrom(obj.getClass()))
			return obj;
		if (dest.isPrimitive()) {
			if (obj instanceof Number)
				return obj;
			if (dest == int.class)
				return Integer.valueOf(obj.toString());
			else if (dest == short.class)
				return Short.valueOf(obj.toString());
			else if (dest == long.class)
				return Long.valueOf(obj.toString());
			else if (dest == byte.class)
				return Byte.valueOf(obj.toString());
			else if (dest == float.class)
				return Float.valueOf(obj.toString());
			else if (dest == double.class)
				return Double.valueOf(obj.toString());
			else if (dest == char.class) {
				String asString = dest.toString();
				if (asString.length() > 0)
					return Character.valueOf(asString.charAt(0));
			} else if (dest == boolean.class) {
				return (Boolean) obj;
			}
			throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to "
					+ dest.getName());
		} else {
			if (dest.isEnum())
				return Enum.valueOf((Class<Enum>) dest, obj.toString());
			if (dest == Integer.class)
				if (obj instanceof Number)
					return Integer.valueOf(((Number) obj).intValue());
				else
					return Integer.valueOf(obj.toString());
			if (dest == Long.class)
				if (obj instanceof Number)
					return Long.valueOf(((Number) obj).longValue());
				else
					return Long.valueOf(obj.toString());
			if (dest == Short.class)
				if (obj instanceof Number)
					return Short.valueOf(((Number) obj).shortValue());
				else
					return Short.valueOf(obj.toString());
			if (dest == Byte.class)
				if (obj instanceof Number)
					return Byte.valueOf(((Number) obj).byteValue());
				else
					return Byte.valueOf(obj.toString());
			if (dest == Float.class)
				if (obj instanceof Number)
					return Float.valueOf(((Number) obj).floatValue());
				else
					return Float.valueOf(obj.toString());
			if (dest == Double.class)
				if (obj instanceof Number)
					return Double.valueOf(((Number) obj).doubleValue());
				else
					return Double.valueOf(obj.toString());
			if (dest == Character.class) {
				String asString = dest.toString();
				if (asString.length() > 0)
					return Character.valueOf(asString.charAt(0));
			}
			throw new RuntimeException("Object: Can not Convert " + obj.getClass().getName() + " to " + dest.getName());
		}
	}

	public final static JsonSmartFieldFilter JSON_SMART_FIELD_FILTER = new JsonSmartFieldFilter();

	public static class JsonSmartFieldFilter implements FieldFilter {

		@Override
		public boolean canUse(Field field) {
			JsonIgnore ignore = field.getAnnotation(JsonIgnore.class);
			if (ignore != null && ignore.value())
				return false;
			return true;
		}

		@Override
		public boolean canUse(Field field, Method method) {
			JsonIgnore ignore = method.getAnnotation(JsonIgnore.class);
			if (ignore != null && ignore.value())
				return false;
			return true;
		}

		@Override
		public boolean canRead(Field field) {
			return true;
		}

		@Override
		public boolean canWrite(Field field) {
			return true;
		}
	}
}
