package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

public class TestInvalidNumber extends TestCase {

	public void testF1() {
		String test = "51e88";
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:\"51e88\"}", comp);

		o = JSONValue.parse(comp, JSONObject.class);
		assertEquals(o.get("a"), test);
	}

	public void testF2() {
		String test = "51e+88";
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:\"51e+88\"}", comp);

		o = JSONValue.parse(comp, JSONObject.class);
		assertEquals(o.get("a"), test);
	}

	public void testF3() {
		String test = "51e-88";
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:\"51e-88\"}", comp);

		o = JSONValue.parse(comp, JSONObject.class);
		assertEquals(o.get("a"), test);
	}

	public void testF4() {
		String test = "51ee88";
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:51ee88}", comp);
		
		o = JSONValue.parse(comp, JSONObject.class);
		assertEquals(o.get("a"), test);
	}

}
