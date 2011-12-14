package net.minidev.json.test.strict;

import junit.framework.TestCase;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import net.minidev.json.test.MustThrows;

/**
 * @since 1.0.7
 */
public class TestZeroLead extends TestCase {

	public void test0O() throws Exception {
		String s = "{\"t\":0}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
		assertEquals(o.get("t"), 0);
		JSONValue.parseWithException(s);
	}

	public void test0A() throws Exception {
		String s = "[0]";
		JSONArray o = (JSONArray) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
		assertEquals(o.get(0), 0);
		JSONValue.parseWithException(s);
	}

	public void test0Float() throws Exception {
		String s = "[00.0]";
		// strict
		MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_LEADING_0);
		// PERMISIVE
		JSONValue.parseWithException(s);
	}

	public void test01Float() throws Exception {
		String s = "[01.0]";
		// strict
		MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_LEADING_0);
		// PERMISIVE
		JSONValue.parseWithException(s);
	}
	
	public void test00001() throws Exception {
		String s = "{\"t\":00001}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), 1);
		JSONValue.parseWithException(s);
	}

	public void test00001Strict() throws Exception {
		String s = "{\"t\":00001}";
		MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_LEADING_0);
		JSONValue.parseWithException(s);
	}

	// disable in 1.1
//	public void testDup() throws Exception {
//		String s = "{'t':1,'t':2}";
//		try {
//			new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
//			assertEquals("Should Stack", "");
//		} catch (ParseException e) {
//			assertEquals(ParseException.ERROR_UNEXPECTED_DUPLICATE_KEY, e.getErrorType());
//		}
//	}

}
