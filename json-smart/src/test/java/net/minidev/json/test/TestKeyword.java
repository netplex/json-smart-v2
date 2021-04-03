package net.minidev.json.test;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestKeyword {

	@Test
	public void testBool() throws Exception {
		String s = "{t:true}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), true);

		s = "{t:false}";
		o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), false);
	}

	@Test
	public void testNull() throws Exception {
		String s = "{t:null}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertNull(o.get("t"));
	}

	@Test
	public void testNaN() throws Exception {
		String s = "{t:NaN}";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), Float.NaN);
	}

	@Test
	public void testNaNStrict() throws Exception {
		String s = "{\"t\":NaN}";
		MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_TOKEN);
	}

}
