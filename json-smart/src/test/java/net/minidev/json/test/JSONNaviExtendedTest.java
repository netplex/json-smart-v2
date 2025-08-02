package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONNavi;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import org.junit.jupiter.api.Test;

public class JSONNaviExtendedTest {

  @Test
  public void testNewInstance() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    assertNotNull(navi);
    assertFalse(navi.hasFailure());
  }

  @Test
  public void testNewInstanceObject() {
    JSONNavi<JSONObject> navi = JSONNavi.newInstanceObject();
    assertNotNull(navi);
    assertTrue(navi.isObject());
    assertFalse(navi.hasFailure());
  }

  @Test
  public void testNewInstanceArray() {
    JSONNavi<JSONArray> navi = JSONNavi.newInstanceArray();
    assertNotNull(navi);
    assertTrue(navi.isArray());
    assertFalse(navi.hasFailure());
  }

  @Test
  public void testConstructorWithJson() {
    String json = "{\"name\":\"John\", \"age\":30}";
    JSONNavi<?> navi = new JSONNavi<>(json);
    assertNotNull(navi);
    assertEquals("John", navi.getString("name"));
    assertEquals(30, navi.getInt("age"));
  }

  @Test
  public void testConstructorWithJsonAndClass() {
    String json = "{\"name\":\"John\", \"age\":30}";
    JSONNavi<JSONObject> navi = new JSONNavi<>(json, JSONObject.class);
    assertNotNull(navi);
    assertEquals("John", navi.getString("name"));
    assertEquals(30, navi.getInt("age"));
  }

  @Test
  public void testSetAndGetString() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("name", "John");

    assertEquals("John", navi.getString("name"));
    assertTrue(navi.hasKey("name"));
  }

  @Test
  public void testSetAndGetNumbers() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("intValue", 42)
        .set("longValue", 1000L)
        .set("doubleValue", 3.14)
        .set("floatValue", 2.5f);

    assertEquals(42, navi.getInt("intValue"));
    assertEquals(Integer.valueOf(42), navi.getInteger("intValue"));
    assertEquals(3.14, navi.getDouble("doubleValue"), 0.001);
  }

  @Test
  public void testAtAndUp() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("name", "root").at("child").set("name", "child1").up().set("age", 30);

    assertEquals("root", navi.getString("name"));
    assertEquals(30, navi.getInt("age"));

    navi.at("child");
    assertEquals("child1", navi.getString("name"));
  }

  @Test
  public void testArrayOperations() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.at("numbers").add(1, 2, 3, 4, 5);

    assertTrue(navi.isArray());
    assertEquals(5, navi.getSize());

    navi.at(0);
    assertEquals(1, navi.asInt());

    navi.up().at(-1);
    assertEquals(5, navi.asInt());
  }

  @Test
  public void testAtNext() {
    JSONNavi<JSONArray> navi = JSONNavi.newInstanceArray();
    navi.add("first", "second").atNext().set("third").root();

    assertEquals(3, navi.getSize());
    navi.at(2);
    assertEquals("third", navi.asString());
  }

  @Test
  public void testNestedStructures() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("user", "John")
        .at("address")
        .set("street", "123 Main St")
        .set("city", "Boston")
        .up()
        .at("phones")
        .add("555-1234", "555-5678")
        .root();

    assertEquals("John", navi.getString("user"));

    navi.at("address");
    assertEquals("123 Main St", navi.getString("street"));
    assertEquals("Boston", navi.getString("city"));

    navi.up().at("phones");
    assertEquals(2, navi.getSize());
    navi.at(0);
    assertEquals("555-1234", navi.asString());
  }

  @Test
  public void testGetKeys() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("name", "John").set("age", 30).set("city", "Boston");

    Collection<String> keys = navi.getKeys();
    assertNotNull(keys);
    assertEquals(3, keys.size());
    assertTrue(keys.contains("name"));
    assertTrue(keys.contains("age"));
    assertTrue(keys.contains("city"));
  }

  @Test
  public void testGetKeysForArray() {
    JSONNavi<JSONArray> navi = JSONNavi.newInstanceArray();
    navi.add(1, 2, 3);

    Collection<String> keys = navi.getKeys();
    assertNull(keys);
  }

  @Test
  public void testAsTypes() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("stringVal", "hello")
        .set("intVal", 42)
        .set("doubleVal", 3.14)
        .at("boolVal")
        .set(Boolean.TRUE)
        .up()
        .set("nullVal", (String) null);

    navi.at("stringVal");
    assertEquals("hello", navi.asString());

    navi.up().at("intVal");
    assertEquals(42, navi.asInt());
    assertEquals(Integer.valueOf(42), navi.asIntegerObj());
    assertEquals(42.0, navi.asDouble(), 0.001);
    assertEquals(Double.valueOf(42.0), navi.asDoubleObj());

    navi.up().at("doubleVal");
    assertEquals(3.14, navi.asDouble(), 0.001);

    navi.up().at("boolVal");
    assertTrue(navi.asBoolean());
    assertEquals(Boolean.TRUE, navi.asBooleanObj());

    navi.up().at("nullVal");
    assertNull(navi.asString());
    assertNull(navi.asIntegerObj());
    assertNull(navi.asDoubleObj());
    assertNull(navi.asBooleanObj());
  }

  @Test
  public void testSetDirectValues() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.at("text").set("hello").up().at("number").set(42).up().at("bool").set(Boolean.TRUE).root();

    assertEquals("hello", navi.getString("text"));
    assertEquals(42, navi.getInt("number"));

    navi.at("bool");
    assertTrue(navi.asBoolean());
  }

  @Test
  public void testRootNavigation() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("rootValue", "test").at("nested").set("childValue", "child").root();

    assertEquals("test", navi.getString("rootValue"));
    assertFalse(navi.hasFailure());
  }

  @Test
  public void testUpMultipleLevels() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("level0", "root")
        .at("level1")
        .set("name", "first")
        .at("level2")
        .set("name", "second")
        .at("level3")
        .set("name", "third")
        .up(3);

    assertEquals("root", navi.getString("level0"));
  }

  @Test
  public void testGetCurrentObject() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("test", "value");

    Object current = navi.getCurrentObject();
    assertNotNull(current);
  }

  @Test
  public void testGetRoot() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("test", "value");

    Object root = navi.getRoot();
    assertNotNull(root);
  }

  @Test
  public void testJPath() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.at("user").at("address").at("street");

    assertEquals("/user/address/street", navi.getJPath());
  }

  @Test
  public void testJPathWithArrays() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.at("users").add("user1", "user2").at(0);

    assertEquals("/users[0]", navi.getJPath());
  }

  @Test
  public void testToString() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("name", "John").set("age", 30);

    String json = navi.toString();
    assertNotNull(json);
    assertTrue(json.contains("\"name\":\"John\""));
    assertTrue(json.contains("\"age\":30"));
  }

  @Test
  public void testToStringWithStyle() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("name", "John");

    String compressed = navi.toString(JSONStyle.MAX_COMPRESS);
    String uncompressed = navi.toString(JSONStyle.NO_COMPRESS);

    assertNotNull(compressed);
    assertNotNull(uncompressed);
    assertTrue(compressed.length() <= uncompressed.length());
  }

  @Test
  public void testFailureHandling() {
    String json = "{\"name\":\"John\"}";
    JSONNavi<?> navi = new JSONNavi<>(json);

    navi.at("nonexistent");
    assertTrue(navi.hasFailure());

    String errorString = navi.toString();
    assertTrue(errorString.contains("Error"));
  }

  @Test
  public void testArrayFailure() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("notArray", "value");

    navi.at("notArray").at(0);
    assertTrue(navi.hasFailure());
  }

  @Test
  public void testSimpleFailure() {
    String json = "{\"name\":\"John\"}";
    JSONNavi<?> navi = new JSONNavi<>(json);

    navi.at("nonexistent");
    assertTrue(navi.hasFailure());
  }

  @Test
  public void testHasKey() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("existing", "value");

    assertTrue(navi.hasKey("existing"));
    assertFalse(navi.hasKey("nonexistent"));
  }

  @Test
  public void testHasKeyOnArray() {
    JSONNavi<JSONArray> navi = JSONNavi.newInstanceArray();
    navi.add(1, 2, 3);

    assertFalse(navi.hasKey("anykey"));
  }

  @Test
  public void testGetWithKey() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("test", "value");

    Object value = navi.get("test");
    assertEquals("value", value);

    Object missing = navi.get("missing");
    assertNull(missing);
  }

  @Test
  public void testGetWithIndex() {
    JSONNavi<JSONArray> navi = JSONNavi.newInstanceArray();
    navi.add("first", "second", "third");

    Object value = navi.get(1);
    assertEquals("second", value);
  }

  @Test
  public void testSizeCalculations() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    assertEquals(0, navi.getSize());

    navi.set("key1", "value1").set("key2", "value2");
    assertEquals(2, navi.getSize());

    navi.at("array").add(1, 2, 3, 4);
    assertEquals(4, navi.getSize());
  }

  @Test
  public void testNegativeArrayIndex() {
    JSONNavi<JSONArray> navi = JSONNavi.newInstanceArray();
    navi.add("first", "second", "third");

    navi.at(-1);
    assertEquals("third", navi.asString());

    navi.up().at(-2);
    assertEquals("second", navi.asString());
  }

  @Test
  public void testReadonlyMode() {
    String json = "{\"name\":\"John\"}";
    JSONNavi<?> navi = new JSONNavi<>(json);

    navi.at("newkey");
    assertTrue(navi.hasFailure());
  }

  @Test
  public void testComplexNestedStructure() {
    JSONNavi<JSONAwareEx> navi = JSONNavi.newInstance();
    navi.set("company", "TechCorp").at("employees").add("John", "Jane").root();

    String json = navi.toString();
    assertTrue(json.contains("TechCorp"));
    assertTrue(json.contains("John"));
    assertTrue(json.contains("Jane"));
  }
}
