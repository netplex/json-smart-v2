package net.minidev.json.test.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import org.junit.jupiter.api.Test;

/**
 * Test serialization of JSONObject to max coverage increase JSONObject coverage from 64% to 100%
 *
 * @see JSONObject
 */
public class TestJSONObjectTest {

  @Test
  public void testEscape() {
    String escaped = JSONObject.escape("\"test\"");
    assertEquals("\\\"test\\\"", escaped);
  }

  @Test
  public void testToJSONStringWithEmptyMap() {
    String jsonString = JSONObject.toJSONString(new HashMap<>());
    assertEquals("{}", jsonString);
  }

  @Test
  public void testToJSONStringWithSingleElement() {
    Map<String, Object> map = new HashMap<>();
    map.put("key", "value");
    String jsonString = JSONObject.toJSONString(map);
    assertEquals("{\"key\":\"value\"}", jsonString);
  }

  @Test
  public void testToJSONStringWithMultipleElements() {
    Map<String, Object> map = new HashMap<>();
    map.put("key1", 1);
    map.put("key2", "two");
    map.put("key3", 3.0);
    String jsonString = JSONObject.toJSONString(map);
    assertEquals("{\"key1\":1,\"key2\":\"two\",\"key3\":3.0}", jsonString);
  }

  @Test
  public void testWriteJSONStringWithSingleElement() throws IOException {
    JSONObject map = new JSONObject(1);
    map.put("key", "value");
    String txt = map.toJSONString();
    assertEquals("{\"key\":\"value\"}", txt);
  }

  // @Test
  // public void testWriteJSONStringWithMultipleElements() throws IOException {
  //     StringWriter out = new StringWriter();
  //     Map<String, Object> map = new HashMap<>();
  //     map.put("key1", 1);
  //     map.put("key2", "two");
  //     map.put("key3", 3.0);
  //     JSONObject.writeJSONString(map, out);
  //     assertEquals("{\"key1\":1,\"key2\":\"two\",\"key3\":3.0}", out.toString());
  // }

  @Test
  public void testAppendField() {
    JSONObject obj = new JSONObject();
    obj.appendField("key", "value");
    assertEquals("value", obj.get("key"));
  }

  @Test
  public void testGetAsString() {
    JSONObject obj = new JSONObject();
    obj.put("key", "value");
    assertEquals("value", obj.getAsString("key"));
  }

  @Test
  public void testGetAsNumber() {
    JSONObject obj = new JSONObject();
    obj.put("key", 123);
    assertEquals(123, obj.getAsNumber("key"));
  }

  @Test
  public void testMerge() {
    JSONObject obj1 = new JSONObject();
    obj1.put("key1", 1);
    JSONObject obj2 = new JSONObject();
    obj2.put("key2", "two");
    obj1.merge(obj2);
    assertEquals(1, obj1.get("key1"));
    assertEquals("two", obj1.get("key2"));
  }

  @Test
  public void testMergeWithOverwrite() {
    JSONObject obj1 = new JSONObject();
    obj1.put("key", 1);
    JSONObject obj2 = new JSONObject();
    obj2.put("key", 2);
    obj1.merge(obj2, true);
    assertEquals(2, obj1.get("key"));
  }

  @Test
  public void testMergeWithOverwrite1() {
    JSONObject obj1 = new JSONObject();
    obj1.put("key", 1);
    obj1.merge(null, true);
    assertEquals(1, obj1.get("key"));
  }

  @Test
  public void testToString() {
    JSONObject obj = new JSONObject();
    obj.put("key", "value");
    assertEquals("{\"key\":\"value\"}", obj.toString());
  }

  @Test
  public void testWriteJSONNull() throws IOException {
    StringBuilder sb = new StringBuilder();
    JSONObject.writeJSON(null, sb, JSONStyle.MAX_COMPRESS);
    assertEquals("null", sb.toString());
  }
}
