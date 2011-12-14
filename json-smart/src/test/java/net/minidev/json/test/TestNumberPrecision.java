package net.minidev.json.test;

import java.math.BigInteger;

import junit.framework.TestCase;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;

public class TestNumberPrecision extends TestCase {
	public void testMaxLong() {		
		Long v = Long.MAX_VALUE;
		String s = "[" + v + "]";
		JSONArray array = (JSONArray) JSONValue.parse(s);
		Object r = array.get(0);
		assertEquals(v, r);
	}

	public void testMinLong() {
		Long v = Long.MIN_VALUE;
		String s = "[" + v + "]";
		JSONArray array = (JSONArray) JSONValue.parse(s);
		Object r = array.get(0);
		assertEquals(v, r);
	}

	public void testMinBig() {
		BigInteger v = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);
		String s = "[" + v + "]";
		JSONArray array = (JSONArray) JSONValue.parse(s);
		Object r = array.get(0);
		assertEquals(v, r);
	}

	public void testMaxBig() {
		BigInteger v = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
		String s = "[" + v + "]";
		JSONArray array = (JSONArray) JSONValue.parse(s);
		Object r = array.get(0);
		assertEquals(v, r);
	}
}
