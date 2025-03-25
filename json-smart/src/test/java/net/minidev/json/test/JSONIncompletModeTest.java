package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;

/**
 * TODO make the same tests in stream and bytes mode
 */

public class JSONIncompletModeTest {
  @Test
  public void testArraySimple() throws Exception {
    String s = "[1";
    JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE | JSONParser.ACCEPT_INCOMPLETE);
    JSONArray array = (JSONArray) p.parse(s);
    assertEquals(Long.valueOf(1), (Long) array.get(0));
  }

  @Test
  public void testArrayInObject1() throws Exception {
    String s = "{\"obj\":[1";
    String result = "{\"obj\":[1]}";

    JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE | JSONParser.ACCEPT_INCOMPLETE);
    JSONObject array = (JSONObject) p.parse(s);
    assertEquals(result, array.toJSONString());
  }

  @Test
  public void testObjectCut() throws Exception {
    String s = "{\"obj\":";
    String result = "{\"obj\":null}";

    JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE | JSONParser.ACCEPT_INCOMPLETE);
    JSONObject array = (JSONObject) p.parse(s);
    assertEquals(result, array.toJSONString());
  }

  @Test
  public void testObjectCut2() throws Exception {
    String s = "{\"obj\"";
    String result = "{\"obj\":null}";

    JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE | JSONParser.ACCEPT_INCOMPLETE);
    JSONObject array = (JSONObject) p.parse(s);
    assertEquals(result, array.toJSONString());
  }

  @Test
  public void testObjectCut3() throws Exception {
    String s = "{\"obj";
    String result = "{\"obj\":null}";

    JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE | JSONParser.ACCEPT_INCOMPLETE);
    JSONObject array = (JSONObject) p.parse(s);
    assertEquals(result, array.toJSONString());
  }

  @Test
  public void testObjectCut4() throws Exception {
    String s = "{\"obj\":\"";
    String result = "{\"obj\":\"\"}";

    JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE | JSONParser.ACCEPT_INCOMPLETE);
    JSONObject array = (JSONObject) p.parse(s);
    assertEquals(result, array.toJSONString());
  }
}
