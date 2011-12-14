package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class MustThrows {

	public static void testStrictInvalidJson(String json, int execptionType) throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_RFC4627);
		try {
			p.parse(json);
			TestCase.assertFalse("Exception Should Occure parssing:" + json, true);
		} catch (ParseException e) {
			if (execptionType == -1)
				execptionType = e.getErrorType();
			TestCase.assertEquals(execptionType, e.getErrorType());
		}
	}
}
