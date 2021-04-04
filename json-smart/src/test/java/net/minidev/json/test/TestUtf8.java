package net.minidev.json.test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestUtf8 {
	// Sinhalese language
	static String[] nonLatinTexts = new String[] { "සිංහල ජාතිය", "日本語", "Русский", "فارسی", "한국어", "Հայերեն", "हिन्दी", "עברית", "中文", "አማርኛ", "മലയാളം",
			"ܐܬܘܪܝܐ", "მარგალური" };

	@Test
	public void testString() throws Exception {
		for (String nonLatinText : nonLatinTexts) {
			String s = "{\"key\":\"" + nonLatinText + "\"}";
			JSONObject obj = (JSONObject) JSONValue.parse(s);
			String v = (String) obj.get("key"); // result is incorrect
			assertEquals(v, nonLatinText);
		}
	}

	@Test
	public void testReader() throws Exception {
		for (String nonLatinText : nonLatinTexts) {
			String s = "{\"key\":\"" + nonLatinText + "\"}";
			StringReader reader = new StringReader(s);
			JSONObject obj = (JSONObject) JSONValue.parse(reader);

			String v = (String) obj.get("key"); // result is incorrect
			assertEquals(v, nonLatinText);
		}
	}

	@Test
	public void testInputStream() throws Exception {
		for (String nonLatinText : nonLatinTexts) {
			String s = "{\"key\":\"" + nonLatinText + "\"}";
			ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes("utf8"));
			JSONObject obj = (JSONObject) JSONValue.parse(bis);
			String v = (String) obj.get("key"); // result is incorrect
			assertEquals(v, nonLatinText);
		}
	}

	@Test
	public void testBytes() throws Exception {
		for (String nonLatinText : nonLatinTexts) {
			String s = "{\"key\":\"" + nonLatinText + "\"}";
			byte[] bs = s.getBytes("utf8");
			JSONObject obj = (JSONObject) JSONValue.parse(bs);
			String v = (String) obj.get("key"); // result is incorrect
			assertEquals(v, nonLatinText);
		}
	}
}