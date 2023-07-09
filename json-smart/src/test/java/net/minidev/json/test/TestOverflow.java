package net.minidev.json.test;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import static net.minidev.json.parser.JSONParser.DEFAULT_PERMISSIVE_MODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class TestOverflow {
	@Test
	public void stressTest() throws Exception {
		int size = 10000;
		StringBuilder sb = new StringBuilder(10 + size*4);
		for (int i=0; i < size; i++) {
			sb.append("{a:");
		}
		sb.append("true");
		for (int i=0; i < size; i++) {
			sb.append("}");
		}
		String s = sb.toString();
		try {
			JSONValue.parseWithException(s);
		} catch (ParseException e) {
			assertEquals(e.getErrorType(), ParseException.ERROR_UNEXPECTED_JSON_DEPTH);
			return;
		}
		fail();
	}

	@Test
	public void shouldNotFailWhenInfiniteJsonDepth() throws Exception {
		int size = 500;
		StringBuilder sb = new StringBuilder(10 + size*4);
		for (int i=0; i < size; i++) {
			sb.append("{a:");
		}
		sb.append("true");
		for (int i=0; i < size; i++) {
			sb.append("}");
		}
		String s = sb.toString();
		try {
			JSONParser parser = new JSONParser(DEFAULT_PERMISSIVE_MODE & ~JSONParser.LIMIT_JSON_DEPTH);
			parser.parse(s,  JSONValue.defaultReader.DEFAULT);
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void shouldNotFailParsingArraysWith400Elements() throws Exception {
		int size = 400;
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i=0; i < size; i++) {
			sb.append("{a:true}");
			if(i+1 < size) {
				sb.append(",");
			}
		}
		sb.append("]");
		String s = sb.toString();
		JSONArray array = (JSONArray) JSONValue.parseWithException(s);
		assertEquals(array.size(), size);
	}
}
