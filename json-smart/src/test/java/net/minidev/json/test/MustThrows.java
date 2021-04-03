package net.minidev.json.test;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class MustThrows {

	@Test
	public static void testStrictInvalidJson(String json, int execptionType) throws Exception {
		testStrictInvalidJson(json, execptionType, null);
	}

	@Test
	public static void testStrictInvalidJson(String json, int execptionType, Class<?> cls) throws Exception {
		testInvalidJson(json, JSONParser.MODE_RFC4627, execptionType, cls);
	}

	@Test
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
			assertFalse(true, "Exception Should Occure parsing:" + json);
		} catch (ParseException e) {
			if (execptionType == -1)
				execptionType = e.getErrorType();
			assertEquals(execptionType, e.getErrorType());
		}
	}

}
