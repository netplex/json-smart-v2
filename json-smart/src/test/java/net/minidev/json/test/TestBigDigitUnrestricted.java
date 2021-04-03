package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import org.junit.jupiter.api.Test;

public class TestBigDigitUnrestricted {
	public static String[] VALID_DOUBLE_JSON = new String[] {"{\"v\":0.12345678912345678}"}; 
	
	@Test
	public void testRestrictedBigDigit() throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_RFC4627);
		String json = VALID_DOUBLE_JSON[0];
		JSONObject obj = (JSONObject) p.parse(json);
		Object value = obj.get("v");
		assertEquals(Double.class, value.getClass(), "Should not Store this big number as a double");
	}
	
	@Test
	public void testUnrestrictedBigDigit() throws Exception {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		String json = VALID_DOUBLE_JSON[0];
		JSONObject obj = (JSONObject) p.parse(json);
		Object value = obj.get("v");
		assertEquals(BigDecimal.class, value.getClass(), "Should not Store this big number as a double");
	}

}
