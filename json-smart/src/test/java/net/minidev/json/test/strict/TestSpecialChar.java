package net.minidev.json.test.strict;

import junit.framework.TestCase;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import net.minidev.json.test.MustThrows;
/**
 * Test control charaters
 * 
 * @author uriel
 *
 */
public class TestSpecialChar extends TestCase {
	
	public void testSpecial127() throws Exception {
		String s = String.format("[\"%c\"]", 127);
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST, ParseException.ERROR_UNEXPECTED_CHAR);
	}

	public void testSpecial31() throws Exception {
		String s = String.format("[\"%c\"]", 31);
		MustThrows.testInvalidJson(s, JSONParser.MODE_STRICTEST, ParseException.ERROR_UNEXPECTED_CHAR);
	}

}
