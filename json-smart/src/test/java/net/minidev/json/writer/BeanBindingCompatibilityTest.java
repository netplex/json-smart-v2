package net.minidev.json.writer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;

class BeanBindingCompatibilityTest {
  public static class Detail {
    public String label;
  }

  public static class Model {
    public Detail detail;
    public List<Detail> items;
    public Map<String, Detail> entries;
    public Detail[] details;
    public int[] numbers;
    public Date date;
  }

  public static class Amount extends Number {
    public int value;

    @Override
    public int intValue() {
      return value;
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  public static class AmountHolder {
    public Amount amount;
  }

  @Test
  void bindsUserNumberBean() throws Exception {
    AmountHolder holder =
        JSONValue.parseWithException("{\"amount\":{\"value\":7}}", AmountHolder.class);
    assertEquals(7, holder.amount.intValue());
  }

  @Test
  void retainsUpdater() throws Exception {
    JsonReader readers = new JsonReader();
    Model model = new Model();
    model.detail = new Detail();
    Detail original = model.detail;
    Model result =
        new JSONParser(JSONParser.MODE_PERMISSIVE)
            .parse(
                "{\"detail\":{\"label\":\"updated\"}}", new UpdaterMapper<Model>(readers, model));
    assertSame(model, result);
    assertSame(original, result.detail);
    assertEquals("updated", result.detail.label);
  }

  @Test
  void bindsNormalNestedStructures() throws Exception {
    Model result =
        JSONValue.parseWithException(
            "{\"detail\":{\"label\":\"one\"},\"items\":[{\"label\":\"two\"}],"
                + "\"entries\":{\"first\":{\"label\":\"three\"}},"
                + "\"details\":[{\"label\":\"four\"}],\"numbers\":[1,2],\"date\":0}",
            Model.class);
    assertEquals("one", result.detail.label);
    assertEquals("two", result.items.get(0).label);
    assertEquals("three", result.entries.get("first").label);
    assertEquals("four", result.details[0].label);
    assertArrayEquals(new int[] {1, 2}, result.numbers);
    assertEquals(new Date(0), result.date);
  }

  @Test
  void honorsCustomReaderAtRootAndProperty() throws Exception {
    JsonReader readers = new JsonReader();
    readers.registerReader(
        Detail.class,
        new ArraysMapper<Detail>(readers) {
          @Override
          public Detail convert(Object current) {
            Detail detail = new Detail();
            detail.label = ((List<?>) current).get(0).toString();
            return detail;
          }
        });
    JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
    Detail detail = parser.parse("[\"custom\"]", readers.getMapper(Detail.class));
    assertEquals("custom", detail.label);
    Model model = parser.parse("{\"detail\":[\"nested\"]}", readers.getMapper(Model.class));
    assertEquals("nested", model.detail.label);
  }

  @Test
  void retainsRemappingAndScalarConversion() throws Exception {
    JsonReader readers = new JsonReader();
    readers.remapField(Detail.class, "title", "label");
    Detail detail =
        new JSONParser(JSONParser.MODE_PERMISSIVE)
            .parse("{\"title\":\"renamed\"}", readers.getMapper(Detail.class));
    assertEquals("renamed", detail.label);
    assertEquals("42", JSONValue.parseWithException("{\"label\":42}", Detail.class).label);
    assertEquals("text", JSONValue.parseWithException("\"text\"", String.class));
    assertNull(JSONValue.parse("null", Model.class));
    assertNull(JSONValue.parseWithException("null", Model.class));
  }

  @Test
  void retainsRawCollectionsAndNestedNull() throws Exception {
    assertEquals(2, JSONValue.parseWithException("[1,2]", java.util.ArrayList.class).size());
    assertEquals(
        "value",
        JSONValue.parseWithException("{\"key\":\"value\"}", java.util.HashMap.class).get("key"));
    Model model = JSONValue.parseWithException("{\"detail\":null}", Model.class);
    assertNotNull(model);
    assertNull(model.detail);
  }
}
