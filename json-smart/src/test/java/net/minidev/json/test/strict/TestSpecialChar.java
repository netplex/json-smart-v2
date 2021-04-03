package net.minidev.json.test.strict;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import net.minidev.json.test.MustThrows;
/**
 * Test control charaters
 * 
 * @author uriel
 *
 */
public class TestSpecialChar {
	
	@Test
	public void testSpecial127() throws Exception {
		String s127 = String.format("%c", 127); 
		String s = String.format("[\"%c\"]", 127);
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST, ParseException.ERROR_UNEXPECTED_CHAR);
		
		JSONArray o = (JSONArray) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
		assertEquals(o.get(0), s127);		
	}

	@Test
	public void testSpecial31() throws Exception {
		String s = String.format("[\"%c\"]", 31);
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST, ParseException.ERROR_UNEXPECTED_CHAR);
	}

}
