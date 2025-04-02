package net.minidev.json.test.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;

/**
 * improve coverage for
 *
 * @see JSONValue
 */
public class TestJSONValue {

  static void Parse4Times(String text, Object expected) {
    Object ret = JSONValue.parse(text);
    assertEquals(ret, expected);

    byte[] b = text.getBytes();
    ret = JSONValue.parse(b);
    assertEquals(ret, expected);

    InputStream stream = new ByteArrayInputStream(b);
    ret = JSONValue.parse(stream);
    assertEquals(ret, expected);

    StringReader reader = new StringReader(text);
    ret = JSONValue.parse(reader);
    assertEquals(ret, expected);
  }

  static void Parse4TimesAs(String text, Object expected, Class<?> toClass) {
    Object ret = JSONValue.parse(text, toClass);
    assertEquals(ret, expected);

    byte[] b = text.getBytes();
    ret = JSONValue.parse(b, toClass);
    assertEquals(ret, expected);

    InputStream stream = new ByteArrayInputStream(b);
    ret = JSONValue.parse(stream, toClass);
    assertEquals(ret, expected);

    StringReader reader = new StringReader(text);
    ret = JSONValue.parse(reader, toClass);
    assertEquals(ret, expected);
  }

  /** all error are dropped as null */
  @Test
  public void testParseHideErrorString() {
    Parse4Times("{\"key\"", null);
    Parse4Times("\"key\"", "key");

    Parse4TimesAs("{\"key\"", null, String.class);
    Parse4TimesAs("\"key\"", "key", String.class);
  }
}
