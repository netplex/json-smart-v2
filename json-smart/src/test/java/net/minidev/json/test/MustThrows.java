package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class MustThrows {

	public static void testStrictInvalidJson(String json, int execptionType) throws Exception {
		testStrictInvalidJson(json, execptionType, null);

	}

	public static void testStrictInvalidJson(String json, int execptionType, Class<?> cls) throws Exception {
		testInvalidJson(json, JSONParser.MODE_RFC4627, execptionType, cls);
	}

	public static void testInvalidJson(String json, int permissifMode, int execptionType) throws Exception {
		testInvalidJson(json, permissifMode, execptionType, null);
	}

	public static void testInvalidJson(String json, int permissifMode, int execptionType, Class<?> cls)
			throws Exception {
		JSONParser p = new JSONParser(permissifMode);
		try {
			if (cls == null)
				p.parse(json);
			else
				p.parse(json, cls);
			TestCase.assertFalse("Exception Should Occure parsing:" + json, true);
		} catch (ParseException e) {
			if (execptionType == -1)
				execptionType = e.getErrorType();
			TestCase.assertEquals(execptionType, e.getErrorType());
		}
	}

}
