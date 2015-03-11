package net.minidev.json.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import junit.framework.TestCase;

public class TestBigValue extends TestCase {
	String bigStr = "12345678901234567890123456789";

	/**
	 * test BigDecimal serialization
	 */
	public void testBigDecimal() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		BigDecimal bigDec = new BigDecimal(bigStr + "." + bigStr);
		map.put("big", bigDec);
		String test = JSONValue.toJSONString(map);
		String result = "{\"big\":" + bigStr + "." +bigStr + "}";
		assertEquals(result, test);
		JSONObject obj =  (JSONObject)JSONValue.parse(test);
		assertEquals(bigDec, obj.get("big"));
		assertEquals(bigDec.getClass(), obj.get("big").getClass());
	}

	/**
	 * test BigInteger serialization
	 */
	public void testBigInteger() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		BigInteger bigInt = new BigInteger(bigStr);
		map.put("big", bigInt);
		String test = JSONValue.toJSONString(map);
		String result = "{\"big\":" + bigStr + "}";
		assertEquals(result, test);
		JSONObject obj =  (JSONObject)JSONValue.parse(test);
		assertEquals(bigInt, obj.get("big"));
		assertEquals(bigInt.getClass(), obj.get("big").getClass());
	}
}
