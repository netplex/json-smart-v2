package net.minidev.json.test;

import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		assertTrue(false);
	}
}
