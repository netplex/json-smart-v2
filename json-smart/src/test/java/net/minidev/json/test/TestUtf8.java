package net.minidev.json.test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class TestUtf8 extends TestCase {

	public static Map<String, String> nonLatinTexts = new HashMap<String, String>();
	static {
		nonLatinTexts.put("Sinhala", "සිංහල ජාතිය");
		nonLatinTexts.put("Japanese", "日本語");
		nonLatinTexts.put("Russian", "Русский");
		nonLatinTexts.put("Farsi", "فارسی");
		nonLatinTexts.put("Korean", "한국어");
		nonLatinTexts.put("Armenian", "Հայերեն");
		nonLatinTexts.put("Hindi", "हिन्दी");
		nonLatinTexts.put("Hebrew", "עברית");
		nonLatinTexts.put("Chinese", "中文");
		nonLatinTexts.put("Amharic", "አማርኛ");
		nonLatinTexts.put("Malayalam", "മലയാളം");
		nonLatinTexts.put("Assyrian Neo-Aramaic", "ܐܬܘܪܝܐ");
		nonLatinTexts.put("Georgian", "მარგალური");

	}

	public void testString() throws Exception {
		for (Map.Entry<String, String> nonLatinEntry : nonLatinTexts.entrySet()) {
			String language = nonLatinEntry.getKey();
			String nonLatinText = nonLatinEntry.getValue();

			String s = "{\"key\":\"" + nonLatinText + "\"}";
			JSONObject obj = (JSONObject) JSONValue.parse(s);
			String actual = (String) obj.get("key");
			assertEquals("Parsing String " + language + " text", nonLatinText, actual);
		}
	}

	public void testReader() throws Exception {
		for (Map.Entry<String, String> nonLatinEntry : nonLatinTexts.entrySet()) {
			String language = nonLatinEntry.getKey();
			String nonLatinText = nonLatinEntry.getValue();

			String s = "{\"key\":\"" + nonLatinText + "\"}";
			StringReader reader = new StringReader(s);
			JSONObject obj = (JSONObject) JSONValue.parse(reader);
			String actual = (String) obj.get("key");
			assertEquals("Parsing StringReader " + language + " text", nonLatinText, actual);
		}
	}

	public void testInputStream() throws Exception {
		for (Map.Entry<String, String> nonLatinEntry : nonLatinTexts.entrySet()) {
			String language = nonLatinEntry.getKey();
			String nonLatinText = nonLatinEntry.getValue();

			String s = "{\"key\":\"" + nonLatinText + "\"}";
			ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes("utf8"));
			JSONObject obj = (JSONObject) JSONValue.parse(bis);
			String actual = (String) obj.get("key");
			assertEquals("Parsing ByteArrayInputStream " + language + " text", nonLatinText, actual);
		}
	}

	public void testBytes() throws Exception {
		for (Map.Entry<String, String> nonLatinEntry : nonLatinTexts.entrySet()) {
			String language = nonLatinEntry.getKey();
			String nonLatinText = nonLatinEntry.getValue();

			String s = "{\"key\":\"" + nonLatinText + "\"}";
			byte[] bs = s.getBytes("utf8");
			JSONObject obj = (JSONObject) JSONValue.parse(bs);
			String actual = (String) obj.get("key");
			assertEquals("Parsing bytes[] " + language + " text", nonLatinText, actual);
		}
	}
}
