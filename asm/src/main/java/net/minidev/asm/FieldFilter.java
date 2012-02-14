package net.minidev.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface FieldFilter {
	public boolean canUse(Field field);
	
	public boolean canUse(Field field, Method method);

	public boolean canRead(Field field);

	public boolean canWrite(Field field);
}
