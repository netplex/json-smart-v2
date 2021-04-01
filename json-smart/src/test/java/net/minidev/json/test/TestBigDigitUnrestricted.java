package net.minidev.json.test;

import java.math.BigDecimal;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class TestBigDigitUnrestricted extends TestCase {
	public static String[] VALID_DOUBLE_JSON = new String[] {"{\"v\":0.12345678912345678}"}; 
	
	public void testRestrictedBigDigit() throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_RFC4627);
		String json = VALID_DOUBLE_JSON[0];
		JSONObject obj = (JSONObject) p.parse(json);
		Object value = obj.get("v");
		assertEquals("Should not Store this big number as a double", Double.class, value.getClass());
	}
	
	public void testUnrestrictedBigDigit() throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		String json = VALID_DOUBLE_JSON[0];
		JSONObject obj = (JSONObject) p.parse(json);
		Object value = obj.get("v");
		assertEquals("Should not Store this big number as a double", BigDecimal.class, value.getClass());
	}

}
