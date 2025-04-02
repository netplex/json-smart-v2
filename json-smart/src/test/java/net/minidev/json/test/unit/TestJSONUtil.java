package net.minidev.json.test.unit;

import static org.junit.jupiter.api.Assertions.*;

import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;

/**
 * improve coverage for
 *
 * @see JSONUtil
 */
public class TestJSONUtil {

  @Test
  public void testConvertToStrictNumbers() {
    assertEquals(1, JSONUtil.convertToStrict(1, Number.class));
    assertEquals(null, JSONUtil.convertToStrict(null, byte.class));
    assertEquals((byte) 1, JSONUtil.convertToStrict(Byte.valueOf((byte) 1), byte.class));
    assertEquals((short) 1, JSONUtil.convertToStrict(Short.valueOf((short) 1), short.class));

    assertEquals((byte) 1, JSONUtil.convertToStrict("1", byte.class));
    assertEquals((short) 1, JSONUtil.convertToStrict("1", short.class));
    assertEquals(1, JSONUtil.convertToStrict("1", int.class));
    assertEquals(1L, JSONUtil.convertToStrict("1", long.class));
    assertEquals(1.0f, JSONUtil.convertToStrict("1", float.class));
    assertEquals(1.0, JSONUtil.convertToStrict("1", double.class));
    assertEquals((byte) 1, JSONUtil.convertToStrict("1", Byte.class));
    assertEquals((short) 1L, JSONUtil.convertToStrict("1", Short.class));
    assertEquals(1, JSONUtil.convertToStrict("1", Integer.class));
    assertEquals(1L, JSONUtil.convertToStrict("1", Long.class));
    assertEquals(1.0f, JSONUtil.convertToStrict("1", Float.class));
    assertEquals(1.0, JSONUtil.convertToStrict("1", Double.class));
  }

  @Test
  public void testConvertToStrictX() {
    assertEquals('a', JSONUtil.convertToStrict("a", char.class));
    assertEquals(true, JSONUtil.convertToStrict("true", boolean.class));
    assertEquals('a', JSONUtil.convertToStrict("a", Character.class));
    assertEquals(true, JSONUtil.convertToStrict("true", Boolean.class));
  }

  @Test
  public void testConvertToNumbers() {
    assertEquals((byte) 1, JSONUtil.convertToX("1", byte.class));
    assertEquals((short) 1, JSONUtil.convertToX("1", short.class));
    assertEquals(1, JSONUtil.convertToX("1", int.class));
    assertEquals(1L, JSONUtil.convertToX("1", long.class));
    assertEquals(1.0f, JSONUtil.convertToX("1", float.class));
    assertEquals(1.0, JSONUtil.convertToX("1", double.class));
    assertEquals((byte) 1, JSONUtil.convertToX("1", Byte.class));
    assertEquals((short) 1, JSONUtil.convertToX("1", Short.class));
    assertEquals(1, JSONUtil.convertToX("1", Integer.class));
    assertEquals(1L, JSONUtil.convertToX("1", Long.class));
    assertEquals(1.0f, JSONUtil.convertToX("1", Float.class));
    assertEquals(1.0, JSONUtil.convertToX("1", Double.class));
  }

  @Test
  public void testConvertToX() {
    assertEquals('a', JSONUtil.convertToX("a", char.class));
    assertEquals(null, JSONUtil.convertToX(null, char.class));
    assertEquals(true, JSONUtil.convertToX("true", boolean.class));

    assertEquals('a', JSONUtil.convertToX("a", Character.class));
    assertEquals(true, JSONUtil.convertToX("true", Boolean.class));
  }

  @Test
  public void testGetSetterName() {
    assertEquals("setKey", JSONUtil.getSetterName("key"));
  }

  @Test
  public void testGetGetterName() {
    assertEquals("getKey", JSONUtil.getGetterName("key"));
  }

  @Test
  public void testGetIsName() {
    assertEquals("isKey", JSONUtil.getIsName("key"));
  }

  @Test
  public void testGetIsName2() {
    assertEquals("isKey", JSONUtil.getIsName("Key"));
  }
}
