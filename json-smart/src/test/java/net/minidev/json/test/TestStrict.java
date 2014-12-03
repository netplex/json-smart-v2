package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class TestStrict extends TestCase {

	public void testS1() throws Exception {
		String text = "My Test";
		String s = "{t:\"" + text + "\"}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), text);
	}

	public void testS2() throws Exception {
		String text = "My Test";
		String s = "{t:'" + text + "'}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), text);
	}

	public void testSEscape() throws Exception {
		String text = "My\r\nTest";
		String text2 = "My\\r\\nTest";
		String s = "{t:'" + text2 + "'}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), text);
	}

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

	/**
	 * issue report gitHub 8 by jochenberger
	 */
	public void testDataAfterValue() throws Exception {
		String s = "{\"foo\":\"bar\"x}";
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST | JSONParser.ACCEPT_TAILLING_SPACE, ParseException.ERROR_UNEXPECTED_TOKEN);
	}
}
