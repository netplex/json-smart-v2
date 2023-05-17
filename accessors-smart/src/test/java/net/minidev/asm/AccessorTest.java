package net.minidev.asm;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import com.mindev.pojos.AccessorTestPojo;

public class AccessorTest {

	private static class AcceptAllFilter implements FieldFilter {

		@Override
		public boolean canUse(Field field) {
			return true;
		}

		@Override
		public boolean canUse(Field field, Method method) {
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

	@Test
	public void testWriteOnlyField() throws NoSuchFieldException, SecurityException {

		Field writeOnlyField = AccessorTestPojo.class.getDeclaredField("writeOnlyField");
		Accessor accessor = new Accessor(AccessorTestPojo.class, writeOnlyField, new AcceptAllFilter());

		assertTrue(accessor.isWritable());
		assertFalse(accessor.isReadable());

	}

	@Test
	public void testReadOnlyField() throws NoSuchFieldException, SecurityException {

		Field readOnlyField = AccessorTestPojo.class.getDeclaredField("readOnlyField");
		Accessor accessor = new Accessor(AccessorTestPojo.class, readOnlyField, new AcceptAllFilter());

		assertFalse(accessor.isWritable());
		assertTrue(accessor.isReadable());

	}

	@Test
	public void testReadAndWriteableField() throws NoSuchFieldException, SecurityException {

		Field readAndWriteableField = AccessorTestPojo.class.getDeclaredField("readAndWriteableField");
		Accessor accessor = new Accessor(AccessorTestPojo.class, readAndWriteableField, new AcceptAllFilter());

		assertTrue(accessor.isWritable());
		assertTrue(accessor.isReadable());

	}

}
