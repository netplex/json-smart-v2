package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
/**
 * test invalid number that will be handle ad string
 * @author uriel
 *
 */
public class TestInvalidNumber extends TestCase {

	private void validFloatAsFloat(String test) {
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:\"" + test + "\"}", comp);
		o = JSONValue.parse(comp, JSONObject.class);
		Object convertedValue = o.get("a");
		assertEquals("Should handle valid number '" + test + "' as number", convertedValue, test);
	}
	
	private void invalidFloatAsText(String test) {
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:" + test + "}", comp);
		o = JSONValue.parse(comp, JSONObject.class);
		Object convertedValue = o.get("a");
		assertEquals("should handle invalid number '" + test + "' as string", convertedValue, test);
	}
	
	public void testF1() {
		validFloatAsFloat("51e88");
	}

	public void testF2() {
		validFloatAsFloat("51e+88");
	}

	public void testF3() {
		validFloatAsFloat("51e-88");
	}

	public void testF4() {
		invalidFloatAsText("51ee88");
	}

	public void testCVE_2021_27568() {
		try {
			JSONValue.parseWithException("{a:-.}");
			assertFalse("should Throws Exception before", true);
		} catch (Exception e) {
			assertEquals("should throw EOF", e.getMessage(), "Unexpected token -. at position 5.");
		}

		try {
			JSONValue.parseWithException("{a:2e+}");
			assertFalse("should Throws Exception before", true);
		} catch (Exception e) {
			assertEquals("should throw EOF", e.getMessage(), "Unexpected token 2e+ at position 6.");
		}

		try {
			JSONValue.parseWithException("{a:[45e-}");
			assertFalse("should Throws Exception before", true);
		} catch (Exception e) {
			assertEquals("should throw EOF", e.getMessage(), "Unexpected End Of File position 8: EOF");
		}
	}
}
