package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

public class TestString {

  @Test
  public void testS0() throws Exception {
    MustThrows.testStrictInvalidJson(
        "{\"1\":\"one\"\n\"2\":\"two\"}", ParseException.ERROR_UNEXPECTED_TOKEN);
  }

  @Test
  public void testS1() throws Exception {
    String text = "My Test";
    String s = "{t:\"" + text + "\"}";
    JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
    assertEquals(o.get("t"), text);
  }

  @Test
  public void testS2() throws Exception {
    String text = "My Test";
    String s = "{t:'" + text + "'}";
    JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
    assertEquals(o.get("t"), text);
  }

  @Test
  public void testSEscape() throws Exception {
    String text = "My\r\nTest";
    String text2 = "My\\r\\nTest";
    String s = "{t:'" + text2 + "'}";
    JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
    assertEquals(o.get("t"), text);
  }

  @Test
  public void testBadString() throws Exception {
    String s = "{\"t\":\"Before\u000CAfter\"}";
    JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
    assertEquals("Before\u000CAfter", o.get("t"));
    try {
      o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
      assertEquals("nothink", o.get("t"));
    } catch (ParseException e) {
      assertEquals("Exception", "Exception");
    }
  }
}
