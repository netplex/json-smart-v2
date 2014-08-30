package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class TestMisc extends TestCase {

	public void testIssue23() throws Exception {
		String s = JSONValue.toJSONString(new int[] { 1, 2, 50, 1234, 10000 });
		assertEquals("[1,2,50,1234,10000]", s);
	}

	public void testEmptyStrict() throws Exception {
		String s = "{\"key1\":\"v1\", \"key2\":{}, \"key3\":[]}";
		JSONObject o = (JSONObject) JSONValue.parseStrict(s);

		assertEquals(o.get("key1"), "v1");
		assertEquals(((JSONObject) o.get("key2")).size(), 0);
		assertEquals(((JSONArray) o.get("key3")).size(), 0);
	}

	public void testBool() throws Exception {
		String s = "{\"key1\":\"v1\", \"key2\":{}, \"key3\":[]}";
		JSONObject o = (JSONObject) JSONValue.parseWithException(s);

		assertEquals(o.get("key1"), "v1");
		assertEquals(((JSONObject) o.get("key2")).size(), 0);
		assertEquals(((JSONArray) o.get("key3")).size(), 0);
	}

	public void testInt() throws Exception {
		String s = "123";
		Object o = JSONValue.parseWithException(s);
		assertEquals(o, 123);
	}

	public void testFloat() throws Exception {
		String s = "123.5";
		Object o = JSONValue.parseWithException(s);
		assertEquals(o, new Double(123.5));
	}

	public void testFloat2() throws Exception {
		String s = "123.5E1";
		Object o = JSONValue.parseWithException(s);
		assertEquals(o, new Double(1235));
	}

	public void testFloat3() throws Exception {
		String s = "123..5";
		Object o = JSONValue.parseWithException(s);
		assertEquals(o, "123..5");
	}

	public void testFloat4() throws Exception {
		String s = "123é.5";
		Object o = JSONValue.parseWithException(s);
		assertEquals(o, 123);
	}

}
