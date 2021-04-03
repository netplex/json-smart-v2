package net.minidev.json.test.strict;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import net.minidev.json.test.MustThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @since 1.0.7
 */
public class TestTaillingJunk {

	@Test
	public void testTaillingSpace() throws Exception {
		String s = "{\"t\":0}   ";
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST, ParseException.ERROR_UNEXPECTED_TOKEN);

		s = "{\"t\":0}   ";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_STRICTEST | JSONParser.ACCEPT_TAILLING_SPACE).parse(s);
		assertEquals(o.get("t"), 0);
	}

	@Test
	public void testTaillingSpace2() throws Exception {
		String s = "{\"t\":0}   \r\n ";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_STRICTEST | JSONParser.ACCEPT_TAILLING_SPACE).parse(s);
		assertEquals(o.get("t"), 0);
	}

	@Test
	public void testTaillingData() throws Exception {
		String s = "{\"t\":0}  0";
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST, ParseException.ERROR_UNEXPECTED_TOKEN, null);
	}

	@Test
	public void testTaillingDataPermisive() throws Exception {
		String s = "{\"t\":0}  0";
		JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
		assertEquals(o.get("t"), 0);
	}

	@Test
	public void testTaillingDataWithSpaceAllowed() throws Exception {
		String s = "{\"t\":0}{";
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST | JSONParser.ACCEPT_TAILLING_SPACE, ParseException.ERROR_UNEXPECTED_TOKEN);
	}
}
