package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.parser.JSONParser;

public class TestFloat extends TestCase {
	public static String[] TRUE_NUMBERS = new String[] { "1.0", "123.456", "1.0E1", "123.456E12", "1.0E+1",
			"123.456E+12", "1.0E-1", "123.456E-12", "1.0e1", "123.456e12", "1.0e+1", "123.456e+12", "1.0e-1",
			"123.456e-12" };

	public static String[] FALSE_NUMBERS = new String[] { "1.0%", "123.45.6", "1.0E", "++123.456E12", "+-01",
			"1.0E+1.2" };

	public void testFloat() throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		for (String s : TRUE_NUMBERS) {
			String json = "{v:" + s + "}";
			Double val = Double.valueOf(s.trim());
			JSONObject obj = (JSONObject) p.parse(json);
			Object value = obj.get("v");
			assertEquals("Should be parse as double", val, value);
		}
	}

	public void testNonFloat() throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		for (String s : FALSE_NUMBERS) {
			String json = "{v:" + s + "}";
			JSONObject obj = (JSONObject) p.parse(json);
			assertEquals("Should be parse as string", s, obj.get("v"));

			String correct = "{\"v\":\"" + s + "\"}";
			assertEquals("Should be re serialized as", correct, obj.toJSONString());
		}
	}
	/**
	 * Error reported in issue 44
	 */
	public void testUUID() {
		String UUID = "58860611416142319131902418361e88";
		JSONObject obj = new JSONObject();
		obj.put("uuid", UUID);
		String compressed =obj.toJSONString(JSONStyle.MAX_COMPRESS); 
		assertTrue(compressed.contains("uuid:\""));
	}
}
