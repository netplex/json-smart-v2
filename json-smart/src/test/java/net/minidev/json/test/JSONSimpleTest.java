package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;

public class JSONSimpleTest {
  @Test
  public void testLong() throws Exception {
    String s = "[1]";
    JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
    JSONArray array = (JSONArray) p.parse(s);
    assertEquals(Long.valueOf(1), (Long) array.get(0));
  }

  @Test
  public void testDefault() throws Exception {
    String s = "[1]";
    JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
    JSONArray array = (JSONArray) p.parse(s);
    assertEquals(Integer.valueOf(1), (Integer) array.get(0));
  }
}
