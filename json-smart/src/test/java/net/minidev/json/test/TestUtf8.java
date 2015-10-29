package net.minidev.json.test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class TestUtf8 extends TestCase {
	// Sinhalese language
	static String[] nonLatinTexts = new String[] { "සිංහල ජාතිය", "日本語", "Русский", "فارسی", "한국어", "Հայերեն", "हिन्दी", "עברית", "中文", "አማርኛ", "മലയാളം",
			"ܐܬܘܪܝܐ", "მარგალური" };

	public void testString() throws Exception {
		for (String nonLatinText : nonLatinTexts) {
			String s = "{\"key\":\"" + nonLatinText + "\"}";
			JSONObject obj = (JSONObject) JSONValue.parse(s);
			String v = (String) obj.get("key"); // result is incorrect
			assertEquals(v, nonLatinText);
		}
	}

	public void testReader() throws Exception {
		for (String nonLatinText : nonLatinTexts) {
			String s = "{\"key\":\"" + nonLatinText + "\"}";
			StringReader reader = new StringReader(s);
			JSONObject obj = (JSONObject) JSONValue.parse(reader);

			String v = (String) obj.get("key"); // result is incorrect
			assertEquals(v, nonLatinText);
		}
	}

	public void testInputStream() throws Exception {
		for (String nonLatinText : nonLatinTexts) {
			String s = "{\"key\":\"" + nonLatinText + "\"}";
			ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes("utf8"));
			JSONObject obj = (JSONObject) JSONValue.parse(bis);
			String v = (String) obj.get("key"); // result is incorrect
			assertEquals(v, nonLatinText);
		}
	}

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
