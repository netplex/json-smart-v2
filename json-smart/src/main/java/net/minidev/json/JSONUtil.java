package net.minidev.json;

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
	public static String getSetterName(String key) {
		int len = key.length();
		char[] b = new char[len + 3];
		b[0] = 's';
		b[1] = 'e';
		b[2] = 't';
		char c = key.charAt(0);
		if (c >= 'a' && c <= 'z')
			c += 'A' - 'a';
		b[3] = c;
		for (int i = 1; i < len; i++) {
			b[i + 3] = key.charAt(i);
		}
		return new String(b);
	}

	public static String getGetterName(String key) {
		int len = key.length();
		char[] b = new char[len + 3];
		b[0] = 'g';
		b[1] = 'e';
		b[2] = 't';
		char c = key.charAt(0);
		if (c >= 'a' && c <= 'z')
			c += 'A' - 'a';
		b[3] = c;
		for (int i = 1; i < len; i++) {
			b[i + 3] = key.charAt(i);
		}
		return new String(b);
	}

	public static String getIsName(String key) {
		int len = key.length();
		char[] b = new char[len + 2];
		b[0] = 'i';
		b[1] = 's';
		char c = key.charAt(0);
		if (c >= 'a' && c <= 'z')
			c += 'A' - 'a';
		b[2] = c;
		for (int i = 1; i < len; i++) {
			b[i + 2] = key.charAt(i);
		}
		return new String(b);
	}

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

	public static int convertToint(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).intValue();
		if (obj instanceof String)
			return Integer.parseInt((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
	}

	public static Integer convertToInt(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Integer.class)
			return (Integer) obj;
		if (obj instanceof Number)
			return Integer.valueOf(((Number) obj).intValue());
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Integer");
	}

	public static short convertToshort(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).shortValue();
		if (obj instanceof String)
			return Short.parseShort((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to short");
	}

	public static Short convertToShort(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Short.class)
			return (Short) obj;
		if (obj instanceof Number)
			return Short.valueOf(((Number) obj).shortValue());
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Short");
	}

	public static long convertTolong(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).longValue();
		if (obj instanceof String)
			return Long.parseLong((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to long");
	}

	public static Long convertToLong(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Long.class)
			return (Long) obj;
		if (obj instanceof Number)
			return Long.valueOf(((Number) obj).longValue());
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Long");
	}

	public static long convertTobyte(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).byteValue();
		if (obj instanceof String)
			return Byte.parseByte((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to byte");
	}

	public static Byte convertToByte(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Byte.class)
			return (Byte) obj;
		if (obj instanceof Number)
			return Byte.valueOf(((Number) obj).byteValue());
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Byte");
	}

	public static float convertTofloat(Object obj) {
		if (obj == null)
			return 0f;
		if (obj instanceof Number)
			return ((Number) obj).floatValue();
		if (obj instanceof String)
			return Float.parseFloat((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
	}

	public static Float convertToFloat(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Float.class)
			return (Float) obj;
		if (obj instanceof Number)
			return Float.valueOf(((Number) obj).floatValue());
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
	}

	public static double convertTodouble(Object obj) {
		if (obj == null)
			return 0.0;
		if (obj instanceof Number)
			return ((Number) obj).doubleValue();
		if (obj instanceof String)
			return Double.parseDouble((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
	}

	public static Double convertToDouble(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Double.class)
			return (Double) obj;
		if (obj instanceof Number)
			return Double.valueOf(((Number) obj).doubleValue());
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
	}

	public static char convertTochar(Object obj) {
		if (obj == null)
			return ' ';
		if (obj instanceof String)
			if (((String) obj).length() > 0)
				return ((String) obj).charAt(0);
			else
				return ' ';
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to char");
	}

	public static Character convertToChar(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Character.class)
			return (Character) obj;
		if (obj instanceof String)
			if (((String) obj).length() > 0)
				return ((String) obj).charAt(0);
			else
				return ' ';
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Character");
	}

	public static boolean convertTobool(Object obj) {
		if (obj == null)
			return false;
		if (obj.getClass() == Boolean.class)
			return ((Boolean) obj).booleanValue();
		if (obj instanceof String)
			return Boolean.parseBoolean((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to boolean");
	}

	public static Boolean convertToBool(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Boolean.class)
			return (Boolean) obj;
		if (obj instanceof String)
			return Boolean.parseBoolean((String) obj);
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to Boolean");
	}

//	public static <T extends Enum<T>> T convertToEnum(Object obj, Class<T> dest) {
//		return Enum.valueOf(dest, obj.toString());
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertTo(Object obj, Class<?> dest) {
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
}
