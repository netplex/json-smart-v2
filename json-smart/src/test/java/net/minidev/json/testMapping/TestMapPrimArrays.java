package net.minidev.json.testMapping;

import junit.framework.TestCase;
import net.minidev.json.JSONValue;

public class TestMapPrimArrays extends TestCase {
	public void testInts() throws Exception {
		String s = "[1,2,3]";
		int[] r = JSONValue.parse(s, int[].class);
		assertEquals(3, r[2]);
	}

	public void testIntss() throws Exception {
		String s = "[[1],[2],[3,4]]";
		int[][] r = JSONValue.parse(s, int[][].class);
		assertEquals(3, r[2][0]);
		assertEquals(4, r[2][1]);
	}

	public void testLongs() throws Exception {
		String s = "[1,2,3]";
		long[] r = JSONValue.parse(s, long[].class);
		assertEquals(3, r[2]);
	}

	public void testFloat() throws Exception {
		String s = "[1.2,22.4,3.14]";
		float[] r = JSONValue.parse(s, float[].class);
		assertEquals(3.14F, r[2]);
	}

	public void testDouble() throws Exception {
		String s = "[1.2,22.4,3.14]";
		double[] r = JSONValue.parse(s, double[].class);
		assertEquals(3.14, r[2]);
	}

	public void testBooleans() throws Exception {
		String s = "[true,true,false]";
		boolean[] r = JSONValue.parse(s, boolean[].class);
		assertEquals(true, r[1]);
		assertEquals(false, r[2]);
	}
}
