package net.minidev.json.test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

/**
 * Test all Compression Styles
 * 
 * @author Uriel Chemouni <uchemouni@gmail.com>
 *
 */
public class TestCompressorFlags extends TestCase {

	public void testProtect() throws Exception {
		String compressed = "{k:value}";
		String nCompress = "{\"k\":\"value\"}";

		JSONObject obj = (JSONObject) JSONValue.parse(nCompress);

		// test MAX_COMPRESS
		String r = obj.toJSONString(JSONStyle.MAX_COMPRESS);
		assertEquals(compressed, r);

		// test LT_COMPRESS
		r = obj.toJSONString(JSONStyle.LT_COMPRESS);
		assertEquals(nCompress, r);

		// test NO_COMPRESS
		r = obj.toJSONString(JSONStyle.NO_COMPRESS);
		assertEquals(nCompress, r);

		// only keys values
		JSONStyle style = new JSONStyle(-1 & JSONStyle.FLAG_PROTECT_KEYS);
		r = obj.toJSONString(style);
		assertEquals("{k:\"value\"}", r);

		// only protect values
		style = new JSONStyle(-1 & JSONStyle.FLAG_PROTECT_VALUES);
		r = obj.toJSONString(style);
		assertEquals("{\"k\":value}", r);
	}

	public void testAggresive() throws Exception {
		String r;
		JSONStyle style;

		String NProtectValue = "{\"a b\":\"c d\"}";
		JSONObject obj = (JSONObject) JSONValue.parse(NProtectValue);
		
		/**
		 * Test Without Agressive
		 */
		style = new JSONStyle(-1 & JSONStyle.FLAG_PROTECT_KEYS);
		r = obj.toJSONString(style);
		assertEquals(NProtectValue, r);

		style = new JSONStyle(-1 & JSONStyle.FLAG_PROTECT_VALUES);
		r = obj.toJSONString(style);
		assertEquals(NProtectValue, r);

		/**
		 * Test With Agressive
		 */
		style = new JSONStyle(-1 & (JSONStyle.FLAG_PROTECT_VALUES | JSONStyle.FLAG_AGRESSIVE));
		r = obj.toJSONString(style);
		assertEquals("{\"a b\":c d}", r);

		style = new JSONStyle(-1 & (JSONStyle.FLAG_PROTECT_KEYS | JSONStyle.FLAG_AGRESSIVE));
		r = obj.toJSONString(style);
		assertEquals("{a b:\"c d\"}", r);

		style = JSONStyle.MAX_COMPRESS;
		r = obj.toJSONString(style);
		assertEquals("{a b:c d}", r);
	}

	public void test4Web() throws Exception {
		String NProtectValue = "{\"k\":\"http:\\/\\/url\"}";
		
		JSONObject obj = (JSONObject) JSONValue.parse(NProtectValue);

		String r = obj.toJSONString(JSONStyle.MAX_COMPRESS);
		assertEquals("{k:\"http://url\"}", r);
		
		r = obj.toJSONString(JSONStyle.LT_COMPRESS);
		assertEquals("{\"k\":\"http://url\"}", r);

		r = obj.toJSONString(JSONStyle.NO_COMPRESS);
		assertEquals("{\"k\":\"http:\\/\\/url\"}", r);
	}

}
