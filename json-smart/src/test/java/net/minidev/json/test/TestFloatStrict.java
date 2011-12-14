package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class TestFloatStrict extends TestCase {

	public void testFloat() throws Exception {
		for (String s : TestFloat.TRUE_NUMBERS) {
			String json = "{\"v\":" + s + "}";
			Double val = Double.valueOf(s.trim());
			JSONObject obj = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(json);
			Object value = obj.get("v");
			assertEquals("Should be parse as double", val, value);
		}
	}

	public void testNonFloat() throws Exception {
		for (String s : TestFloat.FALSE_NUMBERS) {
			String json = "{\"v\":" + s + "}";
			MustThrows.testStrictInvalidJson(json, -1);
		}
	}
}
