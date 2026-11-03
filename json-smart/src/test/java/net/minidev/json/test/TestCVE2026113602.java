package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** A literal 0x1A (ASCII SUB) must not pass for the end of input in strict mode. */
public class TestCVE2026113602 {

  private static final char SUB = 0x1A;

  enum Backend {
    STRING,
    BYTE_ARRAY,
    READER,
    INPUT_STREAM;

    Object parseStrict(String json) throws Exception {
      JSONParser p = new JSONParser(JSONParser.MODE_RFC4627);
      byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
      switch (this) {
        case STRING:
          return p.parse(json);
        case BYTE_ARRAY:
          return p.parse(bytes);
        case READER:
          return p.parse(new StringReader(json));
        default:
          return p.parse(new ByteArrayInputStream(bytes));
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Backend.class)
  public void strictParserRejectsSubAfterValue(Backend backend) {
    assertThrows(ParseException.class, () -> backend.parseStrict("{\"a\":1}" + SUB));
    assertThrows(ParseException.class, () -> backend.parseStrict("{\"a\":1}" + SUB + "garbage"));
  }

  @ParameterizedTest
  @EnumSource(Backend.class)
  public void strictParserRejectsSubInString(Backend backend) {
    assertThrows(ParseException.class, () -> backend.parseStrict("{\"a\":\"x" + SUB + "y\"}"));
  }
}
