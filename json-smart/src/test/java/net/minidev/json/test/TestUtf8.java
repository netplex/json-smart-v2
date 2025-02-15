package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestUtf8 {
  public static Stream<Arguments> languages() {
    return Stream.of(
        Arguments.of("Sinhala", "සිංහල ජාතිය"),
        Arguments.of("Japanese", "日本語"),
        Arguments.of("Russian", "Русский"),
        Arguments.of("Farsi", "فارسی"),
        Arguments.of("Korean", "한국어"),
        Arguments.of("Armenian", "Հայերեն"),
        Arguments.of("Hindi", "हिन्दी"),
        Arguments.of("Hebrew", "עברית"),
        Arguments.of("Chinese", "中文"),
        Arguments.of("Amharic", "አማርኛ"),
        Arguments.of("Malayalam", "മലയാളം"),
        Arguments.of("Assyrian Neo-Aramaic", "ܐܬܘܪܝܐ"),
        Arguments.of("Georgian", "მარგალური"),
        Arguments.of(
            "Emojis",
            "🐶🐱🐭🐹🐰🦊🐻🐼🐻‍❄🐨🐯🦁🐮🐷🐽🐸🐵🙈🙉🙊🐒🐔🐧🐦🐤🐣🐥🦆🦅🦉🦇🐺🐗🐴🦄🐝🐛"));
  }
  ;

  @ParameterizedTest
  @MethodSource("languages")
  public void supportI18nString(String language, String nonLatinText) throws Exception {
    String json = "{\"key\":\"" + nonLatinText + "\"}";
    JSONObject obj = (JSONObject) JSONValue.parse(json);
    String actual = (String) obj.get("key");
    assertEquals(nonLatinText, actual, "Parsing String " + language + " text");
  }

  @ParameterizedTest
  @MethodSource("languages")
  public void supportI18nStringReader(String language, String nonLatinText) throws Exception {
    String json = "{\"key\":\"" + nonLatinText + "\"}";
    StringReader reader = new StringReader(json);
    JSONObject obj = (JSONObject) JSONValue.parse(reader);

    String actual = (String) obj.get("key");
    assertEquals(nonLatinText, actual, "Parsing StringReader " + language + " text");
  }

  @ParameterizedTest
  @MethodSource("languages")
  public void supportI18nByteArrayInputStream(String language, String nonLatinText)
      throws Exception {
    String json = "{\"key\":\"" + nonLatinText + "\"}";
    ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    JSONObject obj = (JSONObject) JSONValue.parse(bis);
    String actual = (String) obj.get("key");
    assertEquals(nonLatinText, actual, "Parsing ByteArrayInputStream " + language + " text");
  }

  @ParameterizedTest
  @MethodSource("languages")
  public void supportI18nBytes(String language, String nonLatinText) throws Exception {
    String json = "{\"key\":\"" + nonLatinText + "\"}";
    byte[] bs = json.getBytes(StandardCharsets.UTF_8);
    JSONObject obj = JSONValue.parse(bs, JSONObject.class);
    String actual = (String) obj.get("key");
    assertEquals(nonLatinText, actual, "Parsing bytes[] " + language + " text");
  }
}
