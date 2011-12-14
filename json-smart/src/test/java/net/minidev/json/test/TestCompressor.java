package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONValue;

public class TestCompressor extends TestCase {
	public void testCompressor() {
		String j = "{'a':{'b':'c','d':'e'},f:[1,2,'XYZ']}".replace('\'', '"');
		String sol = j.replace(" ", "").replace("\"", "");
		String comp = JSONValue.compress(j);
		assertEquals(sol, comp);
	}

	public void testCompressor2() {
		String j = "[{} ]";
		String sol = j.replace(" ", "");
		String comp = JSONValue.compress(j);
		assertEquals(sol, comp);
	}

	public void testCompressor3() {
		String j = "[[],[],[] ]";
		String sol = j.replace(" ", "");
		String comp = JSONValue.compress(j);
		assertEquals(sol, comp);
	}

	public void testCompressor4() {
		String j = "[[1],[2,3],[4] ]";
		String sol = j.replace(" ", "");
		String comp = JSONValue.compress(j);
		assertEquals(sol, comp);
	}

	public void testCompressor5() {
		String j = "[{},{},{} ]";
		String sol = j.replace(" ", "");
		String comp = JSONValue.compress(j);
		assertEquals(sol, comp);
	}

	public void testCompressor6() {
		String j = "[{a:b},{c:d},{e:f}]";
		String sol = j;
		String comp = JSONValue.compress(j);
		assertEquals(sol, comp);
	}

}
