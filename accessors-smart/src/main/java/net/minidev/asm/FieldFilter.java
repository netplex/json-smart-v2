package net.minidev.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * allow to control read/write access to field
 * 
 */
public interface FieldFilter {
	/**
	 * NOT Implemented YET
	 * 
	 * @param field the field
	 * @return boolean
	 */
	public boolean canUse(Field field);

	/**
	 * 
	 * @param field the field
	 * @param method the method
	 * @return boolean
	 */
	public boolean canUse(Field field, Method method);

	/**
	 * NOT Implemented YET
	 * 
	 * @param field the field
	 * @return boolean
	 */
	public boolean canRead(Field field);

	/**
	 * NOT Implemented YET
	 * 
	 * @param field the field
	 * @return boolean
	 */
	public boolean canWrite(Field field);
}
