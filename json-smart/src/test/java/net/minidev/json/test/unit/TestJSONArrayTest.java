package net.minidev.json.test.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONStyle;
import org.junit.jupiter.api.Test;

/** Test serialization of JSONArray to max coverage increase JSONArray coverage from 47% to 100% */
public class TestJSONArrayTest {

  @Test
  public void testAlloc() {
    // test default constructor
    JSONArray a = new JSONArray(12);
    assertEquals(0, a.size());
  }

  @Test
  public void testToJSONStringWithEmptyList() {
    String jsonString = JSONArray.toJSONString(new ArrayList<>());
    assertEquals("[]", jsonString);
  }

  @Test
  public void testAppendElement() throws IOException {
    JSONArray a = new JSONArray(1);
    a.appendElement(1);
    assertEquals("[1]", a.toJSONString(JSONStyle.MAX_COMPRESS));

    assertEquals("[1]", a.toString());

    assertEquals("[1]", a.toString(JSONStyle.MAX_COMPRESS));

    StringBuilder sb = new StringBuilder();
    a.writeJSONString(sb);
    assertEquals("[1]", sb.toString());

    sb = new StringBuilder();
    JSONArray.writeJSONString(Arrays.asList(1), sb);

    sb = new StringBuilder();
    JSONArray.writeJSONString(null, sb, JSONStyle.MAX_COMPRESS);
    assertEquals("null", sb.toString());
  }

  @Test
  public void testToJSONStringWithSingleElement() {
    List<Object> list = Arrays.asList(1);
    String jsonString = JSONArray.toJSONString(list);
    assertEquals("[1]", jsonString);
  }

  @Test
  public void testToJSONStringWithMultipleElements() {
    List<Object> list = Arrays.asList(1, "two", 3.0);
    String jsonString = JSONArray.toJSONString(list);
    assertEquals("[1,\"two\",3.0]", jsonString);
  }

  @Test
  public void testWriteJSONStringWithEmptyList() throws Exception {
    StringWriter out = new StringWriter();
    JSONArray.writeJSONString(new ArrayList<>(), out);
    assertEquals("[]", out.toString());
  }

  @Test
  public void testWriteJSONStringWithSingleElement() throws Exception {
    StringWriter out = new StringWriter();
    List<Object> list = Arrays.asList(1);
    JSONArray.writeJSONString(list, out);
    assertEquals("[1]", out.toString());
  }

  @Test
  public void testWriteJSONStringWithMultipleElements() throws Exception {
    StringWriter out = new StringWriter();
    List<Object> list = Arrays.asList(1, "two", 3.0);
    JSONArray.writeJSONString(list, out);
    assertEquals("[1,\"two\",3.0]", out.toString());
  }
}
