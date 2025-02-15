package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;

public class TestFloatStrict {

  @Test
  public void testFloat() throws Exception {
    for (String s : TestFloat.TRUE_NUMBERS) {
      String json = "{\"v\":" + s + "}";
      Double val = Double.valueOf(s.trim());
      JSONObject obj = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(json);
      Object value = obj.get("v");
      assertEquals(val, value, "Should be parse as double");
    }
  }

  @Test
  public void testNonFloat() throws Exception {
    for (String s : TestFloat.FALSE_NUMBERS) {
      String json = "{\"v\":" + s + "}";
      MustThrows.testStrictInvalidJson(json, -1);
    }
  }
}
