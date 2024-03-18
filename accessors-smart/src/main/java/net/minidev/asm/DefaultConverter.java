package net.minidev.asm;

import net.minidev.asm.ex.ConvertException;

/**
 * Provides utility methods to convert objects to different primitive types and their wrapper classes.
 * It supports conversion from {@link Number} instances and {@link String} representations of numbers
 * to their corresponding primitive types or wrapper classes. Conversion from types that are not supported
 * will result in a {@link ConvertException}.
 */
public class DefaultConverter {
	/**
	 * Default constructor
	 */
	public DefaultConverter() {
		super();
	}
	/**
     * Converts the given object to an {@code int}.
     * 
     * @param obj the object to convert
     * @return the converted int value, or 0 if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to an int
     */
	public static int convertToint(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).intValue();
		if (obj instanceof String)
			return Integer.parseInt((String) obj);
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
	}

	/**
     * Converts the given object to an {@link Integer}.
     * 
     * @param obj the object to convert
     * @return the converted Integer, or {@code null} if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to an Integer
     */
	public static Integer convertToInt(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Integer.class)
			return (Integer) obj;
		if (obj instanceof Number)
			return Integer.valueOf(((Number) obj).intValue());
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Integer");
	}

	/**
     * Converts the given object to a {@code short}.
     * 
     * @param obj the object to convert
     * @return the converted short value, or 0 if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a short
     */
	public static short convertToshort(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).shortValue();
		if (obj instanceof String)
			return Short.parseShort((String) obj);
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to short");
	}

	/**
     * Converts the given object to a {@code short}.
     * 
     * @param obj the object to convert
     * @return the converted short value, or 0 if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a short
     */
	public static Short convertToShort(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Short.class)
			return (Short) obj;
		if (obj instanceof Number)
			return Short.valueOf(((Number) obj).shortValue());
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Short");
	}

	/**
     * Converts the given object to a {@code long}.
     * 
     * @param obj the object to convert
     * @return the converted long value, or 0 if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a long
     */
	public static long convertTolong(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).longValue();
		if (obj instanceof String)
			return Long.parseLong((String) obj);
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to long");
	}

	/**
     * Converts the given object to a {@link Long}.
     * 
     * @param obj the object to convert
     * @return the converted Long, or {@code null} if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a Long
     */
	public static Long convertToLong(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Long.class)
			return (Long) obj;
		if (obj instanceof Number)
			return Long.valueOf(((Number) obj).longValue());
		throw new ConvertException("Primitive: Can not convert value '" + obj+ "' As " + obj.getClass().getName() + " to Long");
	}

	/**
     * Converts the given object to a {@code byte}.
     * 
     * @param obj the object to convert
     * @return the converted byte value, or 0 if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a byte
     */
	public static byte convertTobyte(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).byteValue();
		if (obj instanceof String)
			return Byte.parseByte((String) obj);
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to byte");
	}

	/**
     * Converts the given object to a {@link Byte}.
     * 
     * @param obj the object to convert
     * @return the converted Byte, or {@code null} if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a Byte
     */
	public static Byte convertToByte(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Byte.class)
			return (Byte) obj;
		if (obj instanceof Number)
			return Byte.valueOf(((Number) obj).byteValue());
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Byte");
	}

	/**
     * Converts the given object to a {@code float}.
     * 
     * @param obj the object to convert
     * @return the converted float value, or 0f if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a float
     */
	public static float convertTofloat(Object obj) {
		if (obj == null)
			return 0f;
		if (obj instanceof Number)
			return ((Number) obj).floatValue();
		if (obj instanceof String)
			return Float.parseFloat((String) obj);
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
	}

	/**
     * Converts the given object to a {@link Byte}.
     * 
     * @param obj the object to convert
     * @return the converted Byte, or {@code null} if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a Byte
     */
	public static Float convertToFloat(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Float.class)
			return (Float) obj;
		if (obj instanceof Number)
			return Float.valueOf(((Number) obj).floatValue());
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
	}

	/**
     * Converts the given object to a {@code double}.
     * 
     * @param obj the object to convert
     * @return the converted double value, or 0.0 if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a double
     */
	public static double convertTodouble(Object obj) {
		if (obj == null)
			return 0.0;
		if (obj instanceof Number)
			return ((Number) obj).doubleValue();
		if (obj instanceof String)
			return Double.parseDouble((String) obj);
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
	}

	/**
     * Converts the given object to a {@link Double}.
     * 
     * @param obj the object to convert
     * @return the converted Double, or {@code null} if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a Double
     */
	public static Double convertToDouble(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Double.class)
			return (Double) obj;
		if (obj instanceof Number)
			return Double.valueOf(((Number) obj).doubleValue());
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
	}

	/**
     * Converts the given object to a {@code char}.
     * 
     * @param obj the object to convert
     * @return the converted char value, or a space character if the object is {@code null} or the string is empty
     * @throws ConvertException if the object cannot be converted to a char
     */
	public static char convertTochar(Object obj) {
		if (obj == null)
			return ' ';
		if (obj instanceof String)
			if (((String) obj).length() > 0)
				return ((String) obj).charAt(0);
			else
				return ' ';
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to char");
	}

	/**
     * Converts the given object to a {@link Character}.
     * 
     * @param obj the object to convert
     * @return the converted Character, or {@code null} if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a Character
     */
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
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Character");
	}

	/**
     * Converts the given object to a {@code boolean}.
     * 
     * @param obj the object to convert
     * @return the converted boolean value, false if the object is {@code null} or represents the numeric value 0
     * @throws ConvertException if the object cannot be converted to a boolean
     */
	public static boolean convertTobool(Object obj) {
		if (obj == null)
			return false;
		if (obj.getClass() == Boolean.class)
			return ((Boolean) obj).booleanValue();
		if (obj instanceof String)
			return Boolean.parseBoolean((String) obj);
		if (obj instanceof Number) {
			if (obj.toString().equals("0"))
				return false;
			else
				return true;
		}
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to boolean");
	}

	/**
     * Converts the given object to a {@link Boolean}.
     * 
     * @param obj the object to convert
     * @return the converted Boolean, or {@code null} if the object is {@code null}
     * @throws ConvertException if the object cannot be converted to a Boolean
     */
	public static Boolean convertToBool(Object obj) {
		if (obj == null)
			return null;
		Class<?> c = obj.getClass();
		if (c == Boolean.class)
			return (Boolean) obj;
		if (obj instanceof String)
			return Boolean.parseBoolean((String) obj);
		throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Boolean");
	}
}
