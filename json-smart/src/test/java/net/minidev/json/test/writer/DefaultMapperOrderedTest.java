package net.minidev.json.test.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.parser.ParseException;
import net.minidev.json.writer.DefaultMapperOrdered;
import net.minidev.json.writer.JsonReader;
import net.minidev.json.writer.JsonReaderI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultMapperOrderedTest {

  private JsonReaderI<JSONAwareEx> mapper;
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    jsonReader = new JsonReader();
    mapper = jsonReader.DEFAULT_ORDERED;
  }

  @Test
  public void testStartObject() throws ParseException, IOException {
    JsonReaderI<?> result = mapper.startObject("testKey");
    assertSame(jsonReader.DEFAULT_ORDERED, result);
  }

  @Test
  public void testStartArray() throws ParseException, IOException {
    JsonReaderI<?> result = mapper.startArray("testKey");
    assertSame(jsonReader.DEFAULT_ORDERED, result);
  }

  @Test
  public void testMapperInstanceType() {
    assertInstanceOf(DefaultMapperOrdered.class, mapper);
  }

  @Test
  public void testCreateObject() {
    Object result = mapper.createObject();
    assertInstanceOf(LinkedHashMap.class, result);
  }

  @Test
  public void testCreateArray() {
    Object result = mapper.createArray();
    assertInstanceOf(JSONArray.class, result);
  }

  @Test
  public void testSetValue() throws ParseException, IOException {
    Map<String, Object> current = new LinkedHashMap<>();
    mapper.setValue(current, "testKey", "testValue");

    assertEquals(1, current.size());
    assertEquals("testValue", current.get("testKey"));
  }

  @Test
  public void testSetValueWithMultipleEntries() throws ParseException, IOException {
    LinkedHashMap<String, Object> current = new LinkedHashMap<>();

    mapper.setValue(current, "key1", "value1");
    mapper.setValue(current, "key2", "value2");
    mapper.setValue(current, "key3", "value3");

    assertEquals(3, current.size());
    assertEquals("value1", current.get("key1"));
    assertEquals("value2", current.get("key2"));
    assertEquals("value3", current.get("key3"));

    String[] keys = current.keySet().toArray(new String[0]);
    assertEquals("key1", keys[0]);
    assertEquals("key2", keys[1]);
    assertEquals("key3", keys[2]);
  }

  @Test
  public void testAddValue() throws ParseException, IOException {
    JSONArray current = new JSONArray();

    mapper.addValue(current, "value1");
    mapper.addValue(current, "value2");
    mapper.addValue(current, 42);

    assertEquals(3, current.size());
    assertEquals("value1", current.get(0));
    assertEquals("value2", current.get(1));
    assertEquals(42, current.get(2));
  }

  @Test
  public void testSetValueWithNullKey() throws ParseException, IOException {
    Map<String, Object> current = new LinkedHashMap<>();
    mapper.setValue(current, null, "testValue");

    assertEquals(1, current.size());
    assertTrue(current.containsKey(null));
    assertEquals("testValue", current.get(null));
  }

  @Test
  public void testSetValueWithNullValue() throws ParseException, IOException {
    Map<String, Object> current = new LinkedHashMap<>();
    mapper.setValue(current, "testKey", null);

    assertEquals(1, current.size());
    assertEquals(null, current.get("testKey"));
  }

  @Test
  public void testAddValueWithNullValue() throws ParseException, IOException {
    JSONArray current = new JSONArray();
    mapper.addValue(current, null);

    assertEquals(1, current.size());
    assertEquals(null, current.get(0));
  }

  @Test
  public void testOrderPreservation() throws ParseException, IOException {
    LinkedHashMap<String, Object> current = new LinkedHashMap<>();

    mapper.setValue(current, "first", 1);
    mapper.setValue(current, "second", 2);
    mapper.setValue(current, "third", 3);
    mapper.setValue(current, "fourth", 4);

    String[] expectedOrder = {"first", "second", "third", "fourth"};
    String[] actualOrder = current.keySet().toArray(new String[0]);

    for (int i = 0; i < expectedOrder.length; i++) {
      assertEquals(expectedOrder[i], actualOrder[i]);
    }
  }

  @Test
  public void testComplexObjectStructure() throws ParseException, IOException {
    LinkedHashMap<String, Object> root = new LinkedHashMap<>();

    mapper.setValue(root, "name", "test");
    mapper.setValue(root, "age", 25);

    JSONArray array = new JSONArray();
    mapper.addValue(array, "item1");
    mapper.addValue(array, "item2");
    mapper.setValue(root, "items", array);

    LinkedHashMap<String, Object> nested = new LinkedHashMap<>();
    mapper.setValue(nested, "nestedKey", "nestedValue");
    mapper.setValue(root, "nested", nested);

    assertEquals(4, root.size());
    assertEquals("test", root.get("name"));
    assertEquals(25, root.get("age"));
    assertEquals(array, root.get("items"));
    assertEquals(nested, root.get("nested"));

    assertEquals(2, array.size());
    assertEquals("item1", array.get(0));
    assertEquals("item2", array.get(1));

    assertEquals(1, nested.size());
    assertEquals("nestedValue", nested.get("nestedKey"));
  }
}
