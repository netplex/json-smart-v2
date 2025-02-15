package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;

public class TestFloat {
  public static String[] TRUE_NUMBERS =
      new String[] {
        "1.0",
        "123.456",
        "1.0E1",
        "123.456E12",
        "1.0E+1",
        "123.456E+12",
        "1.0E-1",
        "123.456E-12",
        "1.0e1",
        "123.456e12",
        "1.0e+1",
        "123.456e+12",
        "1.0e-1",
        "123.456e-12"
      };

  public static String[] FALSE_NUMBERS =
      new String[] {"1.0%", "123.45.6", "1.0E", "++123.456E12", "+-01", "1.0E+1.2"};

  @Test
  public void testPrecisionFloat() throws Exception {
    JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
    for (int len = 15; len < 25; len++) {
      StringBuilder sb = new StringBuilder("0.");
      for (int i = 0; i < len; i++) {
        sb.append("123456789".charAt(i % 9));
      }
      String s = sb.toString();
      String json = "{v:" + s + "}";
      JSONObject obj = (JSONObject) p.parse(json);
      Object value = obj.get("v").toString();
      assertEquals(s, value, "Should not loose precision on a " + len + " digits long");
    }
  }

  @Test
  public void testFloat() throws Exception {
    JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
    for (String s : TRUE_NUMBERS) {
      String json = "{v:" + s + "}";
      Double val = Double.valueOf(s.trim());
      JSONObject obj = (JSONObject) p.parse(json);
      Object value = obj.get("v");
      assertEquals(val, value, "Should be parse as double");
    }
  }

  @Test
  public void testNonFloat() throws Exception {
    JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
    for (String s : FALSE_NUMBERS) {
      String json = "{v:" + s + "}";
      JSONObject obj = (JSONObject) p.parse(json);
      assertEquals(s, obj.get("v"), "Should be parse as string");

      String correct = "{\"v\":\"" + s + "\"}";
      assertEquals(correct, obj.toJSONString(), "Should be re serialized as");
    }
  }

  /** Error reported in issue 44 */
  @Test
  public void testUUID() {
    String UUID = "58860611416142319131902418361e88";
    JSONObject obj = new JSONObject();
    obj.put("uuid", UUID);
    String compressed = obj.toJSONString(JSONStyle.MAX_COMPRESS);
    assertTrue(compressed.contains("uuid:\""));
  }
}
