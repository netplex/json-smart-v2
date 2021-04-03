package net.minidev.json.testMapping;

import net.minidev.json.JSONValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestFieldRename {

	public static class TRen {
		public String new_;
		public String default_;
	}

	@Test
	public void testRemap() throws Exception {
		String text = "{'new':'foo','default':'bar'}";
		JSONValue.remapField(TRen.class, "default", "default_");
		JSONValue.remapField(TRen.class, "new", "new_");

		TRen t = JSONValue.parse(text, TRen.class);
		assertEquals(t.new_, "foo");
		assertEquals(t.default_, "bar");
		String dest = JSONValue.toJSONString(t);
		assertTrue(dest.contains("\"default\""));

	}
}
