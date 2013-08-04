package net.minidev.json.testMapping;

import junit.framework.TestCase;
import net.minidev.json.JSONValue;

public class TestDate extends TestCase {
	public void testBooleans() throws Exception {
		String s = "[true,true,false]";
		boolean[] bs = new boolean[] { true, true, false };
		String s2 = JSONValue.toJSONString(bs);
		assertEquals(s, s2);
	}

	public void testInts() throws Exception {
		String s = "[1,2,3]";
		int[] bs = new int[] { 1, 2, 3 };
		String s2 = JSONValue.toJSONString(bs);
		assertEquals(s, s2);
	}

}
