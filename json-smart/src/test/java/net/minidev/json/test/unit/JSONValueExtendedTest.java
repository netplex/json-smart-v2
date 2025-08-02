package net.minidev.json.test.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

public class JSONValueExtendedTest {

  @Test
  public void testParseSimpleTypes() {
    assertEquals("hello", JSONValue.parse("\"hello\""));
    assertEquals(42, JSONValue.parse("42"));
    assertEquals(3.14, JSONValue.parse("3.14"));
    assertEquals(true, JSONValue.parse("true"));
    assertEquals(false, JSONValue.parse("false"));
    assertNull(JSONValue.parse("null"));
  }

  @Test
  public void testParseArray() {
    Object result = JSONValue.parse("[1, 2, 3]");
    assertInstanceOf(JSONArray.class, result);
    JSONArray array = (JSONArray) result;
    assertEquals(3, array.size());
    assertEquals(1, array.get(0));
    assertEquals(2, array.get(1));
    assertEquals(3, array.get(2));
  }

  @Test
  public void testParseObject() {
    Object result = JSONValue.parse("{\"name\":\"John\", \"age\":30}");
    assertInstanceOf(JSONObject.class, result);
    JSONObject obj = (JSONObject) result;
    assertEquals("John", obj.get("name"));
    assertEquals(30, obj.get("age"));
  }

  @Test
  public void testParseKeepingOrder() {
    Object result = JSONValue.parseKeepingOrder("{\"c\":3, \"a\":1, \"b\":2}");
    assertInstanceOf(LinkedHashMap.class, result);
    LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) result;

    String[] keys = map.keySet().toArray(new String[0]);
    assertEquals("c", keys[0]);
    assertEquals("a", keys[1]);
    assertEquals("b", keys[2]);
  }

  @Test
  public void testParseKeepingOrderFromReader() throws IOException {
    StringReader reader = new StringReader("{\"z\":26, \"x\":24, \"y\":25}");
    Object result = JSONValue.parseKeepingOrder(reader);
    assertInstanceOf(LinkedHashMap.class, result);
    LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) result;

    String[] keys = map.keySet().toArray(new String[0]);
    assertEquals("z", keys[0]);
    assertEquals("x", keys[1]);
    assertEquals("y", keys[2]);
  }

  @Test
  public void testParseWithException() throws ParseException {
    Object result = JSONValue.parseWithException("{\"valid\":true}");
    assertInstanceOf(JSONObject.class, result);
    assertEquals(true, ((JSONObject) result).get("valid"));
  }

  @Test
  public void testParseWithExceptionThrows() {
    assertThrows(
        ParseException.class,
        () -> {
          JSONValue.parseWithException("{\"invalid\"");
        });
  }

  @Test
  public void testParseStrict() throws ParseException {
    Object result = JSONValue.parseStrict("{\"key\":\"value\"}");
    assertInstanceOf(JSONObject.class, result);
    assertEquals("value", ((JSONObject) result).get("key"));
  }

  @Test
  public void testParseStrictThrows() {
    assertThrows(
        ParseException.class,
        () -> {
          JSONValue.parseStrict("{key:value}");
        });
  }

  @Test
  public void testIsValidJson() {
    assertTrue(JSONValue.isValidJson("{\"valid\":true}"));
    assertTrue(JSONValue.isValidJson("[1,2,3]"));
    assertTrue(JSONValue.isValidJson("\"string\""));
    assertTrue(JSONValue.isValidJson("42"));
    assertFalse(JSONValue.isValidJson("{invalid"));
  }

  @Test
  public void testIsValidJsonStrict() {
    assertTrue(JSONValue.isValidJsonStrict("{\"valid\":true}"));
    assertFalse(JSONValue.isValidJsonStrict("{key:value}"));
    assertFalse(JSONValue.isValidJsonStrict("{invalid"));
  }

  @Test
  public void testIsValidJsonWithReader() throws IOException {
    StringReader validReader = new StringReader("{\"valid\":true}");
    assertTrue(JSONValue.isValidJson(validReader));

    StringReader invalidReader = new StringReader("{invalid");
    assertFalse(JSONValue.isValidJson(invalidReader));
  }

  @Test
  public void testToJSONString() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "John");
    map.put("age", 30);

    String json = JSONValue.toJSONString(map);
    assertNotNull(json);
    assertTrue(json.contains("\"name\""));
    assertTrue(json.contains("\"John\""));
    assertTrue(json.contains("\"age\""));
    assertTrue(json.contains("30"));
  }

  @Test
  public void testToJSONStringWithStyle() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "John");

    String compressed = JSONValue.toJSONString(map, JSONStyle.MAX_COMPRESS);
    String uncompressed = JSONValue.toJSONString(map, JSONStyle.NO_COMPRESS);

    assertNotNull(compressed);
    assertNotNull(uncompressed);
    assertTrue(compressed.length() <= uncompressed.length());
  }

  @Test
  public void testWriteJSONString() throws IOException {
    StringWriter writer = new StringWriter();
    Map<String, Object> map = new HashMap<>();
    map.put("test", "value");

    JSONValue.writeJSONString(map, writer);

    String result = writer.toString();
    assertTrue(result.contains("\"test\""));
    assertTrue(result.contains("\"value\""));
  }

  @Test
  public void testWriteJSONStringWithStyle() throws IOException {
    StringWriter writer = new StringWriter();
    Map<String, Object> map = new HashMap<>();
    map.put("test", "value");

    JSONValue.writeJSONString(map, writer, JSONStyle.MAX_COMPRESS);

    String result = writer.toString();
    assertTrue(result.contains("test:value"));
  }

  @Test
  public void testWriteJSONStringNull() throws IOException {
    StringWriter writer = new StringWriter();
    JSONValue.writeJSONString(null, writer);
    assertEquals("null", writer.toString());
  }

  @Test
  public void testCompress() {
    String input = "{ \"name\" : \"John\" , \"age\" : 30 }";
    String compressed = JSONValue.compress(input);
    assertNotNull(compressed);
    assertFalse(compressed.contains(" "));
  }

  @Test
  public void testCompressWithStyle() {
    String input = "{ \"name\" : \"John\" }";
    String compressed = JSONValue.compress(input, JSONStyle.MAX_COMPRESS);
    String uncompressed = JSONValue.compress(input, JSONStyle.NO_COMPRESS);

    assertNotNull(compressed);
    assertNotNull(uncompressed);
    assertTrue(compressed.length() <= uncompressed.length());
  }

  @Test
  public void testUncompress() {
    String input = "{name:John}";
    String uncompressed = JSONValue.uncompress(input);
    assertNotNull(uncompressed);
    assertTrue(uncompressed.contains("\"name\""));
    assertTrue(uncompressed.contains("\"John\""));
  }

  @Test
  public void testParseToClass() {
    String json = "\"Hello World\"";
    String result = JSONValue.parse(json, String.class);
    assertEquals("Hello World", result);
  }

  @Test
  public void testParseToClassWithByteArray() {
    String json = "42";
    byte[] bytes = json.getBytes();
    Integer result = JSONValue.parse(bytes, Integer.class);
    assertEquals(Integer.valueOf(42), result);
  }

  @Test
  public void testParseToClassWithInputStream() {
    String json = "true";
    ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());
    Boolean result = JSONValue.parse(stream, Boolean.class);
    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testParseToClassWithReader() {
    String json = "3.14";
    StringReader reader = new StringReader(json);
    Double result = JSONValue.parse(reader, Double.class);
    assertEquals(Double.valueOf(3.14), result);
  }

  @Test
  public void testParseWithExceptionToClass() throws ParseException {
    String json = "\"test\"";
    String result = JSONValue.parseWithException(json, String.class);
    assertEquals("test", result);
  }

  @Test
  public void testParseToUpdate() {
    TestBean bean = new TestBean();
    bean.name = "original";
    bean.value = 0;

    String json = "{\"name\":\"updated\", \"value\":42}";
    TestBean result = JSONValue.parse(json, bean);

    assertEquals("updated", result.name);
    assertEquals(42, result.value);
  }

  @Test
  public void testParseToUpdateWithReader() {
    TestBean bean = new TestBean();
    bean.name = "original";

    StringReader reader = new StringReader("{\"name\":\"updated\"}");
    TestBean result = JSONValue.parse(reader, bean);

    assertEquals("updated", result.name);
  }

  @Test
  public void testParseToUpdateWithInputStream() {
    TestBean bean = new TestBean();
    bean.value = 0;

    ByteArrayInputStream stream = new ByteArrayInputStream("{\"value\":100}".getBytes());
    TestBean result = JSONValue.parse(stream, bean);

    assertEquals(100, result.value);
  }

  @Test
  public void testParseInvalidInputReturnsNull() {
    assertNull(JSONValue.parse("{invalid json"));
    Object result1 = JSONValue.parse(new byte[] {1, 2, 3});
    assertTrue(result1 == null || "".equals(result1));
    assertNull(JSONValue.parse(new StringReader("{invalid")));
    assertNull(JSONValue.parse(new ByteArrayInputStream("{invalid".getBytes())));
  }

  @Test
  public void testParseInvalidInputWithClassReturnsNull() {
    assertNull(JSONValue.parse("{invalid", String.class));
    Object result1 = JSONValue.parse(new byte[] {1, 2, 3}, String.class);
    assertTrue(result1 == null || "".equals(result1));
    assertNull(JSONValue.parse(new StringReader("{invalid"), String.class));
    assertNull(JSONValue.parse(new ByteArrayInputStream("{invalid".getBytes()), String.class));
  }

  @Test
  public void testCompressInvalidInputReturnsOriginal() {
    String invalid = "{invalid json";
    String result = JSONValue.compress(invalid);
    assertEquals(invalid, result);
  }

  public static class TestBean {
    public String name;
    public int value;
  }
}
