package net.minidev.asm;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindev.pojos.AccessorTestPojo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

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

  private static class AcceptNoneFilter implements FieldFilter {

    @Override
    public boolean canUse(Field field) {
      return false;
    }

    @Override
    public boolean canUse(Field field, Method method) {
      return false;
    }

    @Override
    public boolean canRead(Field field) {
      return false;
    }

    @Override
    public boolean canWrite(Field field) {
      return false;
    }
  }

  @Test
  public void testWriteOnlyField() throws NoSuchFieldException, SecurityException {

    Field writeOnlyField = AccessorTestPojo.class.getDeclaredField("writeOnlyField");
    Accessor accessor = new Accessor(AccessorTestPojo.class, writeOnlyField, new AcceptAllFilter());

    assertTrue(accessor.isWritable());
    assertFalse(accessor.isReadable());

    accessor = new Accessor(AccessorTestPojo.class, writeOnlyField, new AcceptNoneFilter());
    assertFalse(accessor.isWritable());
    assertFalse(accessor.isReadable());
  }

  @Test
  public void testReadOnlyField() throws NoSuchFieldException, SecurityException {

    Field readOnlyField = AccessorTestPojo.class.getDeclaredField("readOnlyField");
    Accessor accessor = new Accessor(AccessorTestPojo.class, readOnlyField, new AcceptAllFilter());

    assertFalse(accessor.isWritable());
    assertTrue(accessor.isReadable());

    accessor = new Accessor(AccessorTestPojo.class, readOnlyField, new AcceptNoneFilter());
    assertFalse(accessor.isWritable());
    assertFalse(accessor.isReadable());
  }

  @Test
  public void testReadAndWriteableField() throws NoSuchFieldException, SecurityException {

    Field readAndWriteableField = AccessorTestPojo.class.getDeclaredField("readAndWriteableField");
    Accessor accessor =
        new Accessor(AccessorTestPojo.class, readAndWriteableField, new AcceptAllFilter());

    assertTrue(accessor.isWritable());
    assertTrue(accessor.isReadable());

    accessor = new Accessor(AccessorTestPojo.class, readAndWriteableField, new AcceptNoneFilter());
    assertFalse(accessor.isWritable());
    assertFalse(accessor.isReadable());
  }
}
