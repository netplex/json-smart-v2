package net.minidev.json.test;

import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

public class TestTruncated {

  @Test
  public void testS1() throws Exception {
    String s = "{\"key\":{}";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_EOF);
  }

  @Test
  public void testS2() throws Exception {
    String s = "{\"key\":";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_EOF);
  }

  @Test
  public void testS3() throws Exception {
    String s = "{\"key\":123";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_EOF);
  }
}
