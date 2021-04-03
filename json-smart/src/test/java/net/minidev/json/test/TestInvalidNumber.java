package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
/**
 * test invalid number that will be handle ad string
 * @author uriel
 *
 */
public class TestInvalidNumber {

	private void validFloatAsFloat(String test) {
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:\"" + test + "\"}", comp);
		o = JSONValue.parse(comp, JSONObject.class);
		Object convertedValue = o.get("a");
		assertEquals(convertedValue, test, "Should handle valid number '" + test + "' as number");
	}
	
	private void invalidFloatAsText(String test) {
		JSONObject o = new JSONObject();
		o.put("a", test);
		String comp = JSONValue.toJSONString(o, JSONStyle.MAX_COMPRESS);
		assertEquals("{a:" + test + "}", comp);
		o = JSONValue.parse(comp, JSONObject.class);
		Object convertedValue = o.get("a");
		assertEquals(convertedValue, test, "should handle invalid number '" + test + "' as string");
	}
	
	@Test
	public void testF1() {
		validFloatAsFloat("51e88");
	}

	@Test
	public void testF2() {
		validFloatAsFloat("51e+88");
	}

	@Test
	public void testF3() {
		validFloatAsFloat("51e-88");
	}

	@Test
	public void testF4() {
		invalidFloatAsText("51ee88");
	}

	@Test
	public void testCVE_2021_27568() {
		try {
			JSONValue.parseWithException("{a:-.}");
			assertFalse(true, "should Throws Exception before");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Unexpected token -. at position 5.", "should throw EOF");
		}

		try {
			JSONValue.parseWithException("{a:2e+}");
			assertFalse(true, "should Throws Exception before");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Unexpected token 2e+ at position 6.", "should throw EOF");
		}

		try {
			JSONValue.parseWithException("{a:[45e-}");
			assertFalse(true, "should Throws Exception before");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Unexpected End Of File position 8: EOF", "should throw EOF");
		}
	}
}
