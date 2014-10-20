package net.minidev.json.test;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import junit.framework.TestCase;

public class TestInts extends TestCase {

	public void testIntMax() throws Exception {
		String s = "{t:" + Integer.MAX_VALUE + "}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), Integer.MAX_VALUE);
	}

	public void testIntMin() throws Exception {
		String s = "{t:" + Integer.MIN_VALUE + "}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), Integer.MIN_VALUE);
	}

	public void testIntResult() throws Exception {
		String s = "{\"t\":1}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
		assertEquals(o.get("t"), Integer.valueOf(1));

		o = (JSONObject) new JSONParser(JSONParser.MODE_JSON_SIMPLE).parse(s);
		assertEquals(o.get("t"), Long.valueOf(1));

		o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), Integer.valueOf(1));
	}

	public void testInt() throws Exception {
		String s = "{t:90}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), Integer.valueOf(90));
	}

	public void testIntNeg() throws Exception {
		String s = "{t:-90}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), -90);
	}

	public void testBigInt() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++)
			sb.append(Integer.MAX_VALUE);
		String bigText = sb.toString();
		BigInteger big = new BigInteger(bigText, 10);
		String s = "{t:" + bigText + "}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), big);
	}

	public void testBigDoubleInt() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++)
			sb.append(Integer.MAX_VALUE);
		sb.append('.');
		for (int i = 0; i < 10; i++)
			sb.append(Integer.MAX_VALUE);

		String bigText = sb.toString();
		BigDecimal big = new BigDecimal(bigText);
		String s = "{\"t\":" + bigText + "}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
		assertEquals(o.get("t"), big);
		o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), big);
	}

	public void testjunkTaillingData() throws Exception {
		String s = "{\"t\":124}$ifsisg045";

		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_JSON_SIMPLE).parse(s);
		assertEquals(o.get("t"), 124L);

		MustThrows.testInvalidJson(s, JSONParser.MODE_RFC4627, ParseException.ERROR_UNEXPECTED_TOKEN);
		// o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
		// assertEquals(o.get("t"), 124);

		o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), 124);
	}
}
