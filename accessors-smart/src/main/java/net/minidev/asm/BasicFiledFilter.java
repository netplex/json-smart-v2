package net.minidev.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A basic implementation of the {@link FieldFilter} interface that permits all operations on fields.
 * This implementation returns {@code true} for all checks, indicating that any field can be used, read, and written.
 * It serves as a default or fallback strategy when no specific field filtering logic is required.
 */
public class BasicFiledFilter implements FieldFilter {
     /**
      * default constructor
      */
     public BasicFiledFilter() {
          super();
     }

      /**
       * A singleton instance of {@code BasicFieldFilter}.
       * Since the filter does not maintain any state and allows all operations, it can be reused across the application.
       */
	public final static BasicFiledFilter SINGLETON = new BasicFiledFilter();

	/**
       * Always allows using the specified field.
       *
       * @param field The field to check.
       * @return Always returns {@code true}.
       */
	@Override
	public boolean canUse(Field field) {
		return true;
	}

	/**
       * Always allows using the specified field in conjunction with a method.
       *
       * @param field  The field to check.
       * @param method The method to check. This parameter is not used in the current implementation.
       * @return Always returns {@code true}.
       */
	@Override
	public boolean canUse(Field field, Method method) {
		return true;
	}

	/**
       * Always allows reading the specified field.
       *
       * @param field The field to check.
       * @return Always returns {@code true}.
       */
	@Override
	public boolean canRead(Field field) {
		return true;
	}

	/**
       * Always allows writing to the specified field.
       *
       * @param field The field to check.
       * @return Always returns {@code true}.
       */
	@Override
	public boolean canWrite(Field field) {
		return true;
	}

}
