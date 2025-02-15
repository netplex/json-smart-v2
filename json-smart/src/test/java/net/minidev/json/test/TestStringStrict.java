package net.minidev.json.test;

import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

public class TestStringStrict {

  @Test
  public void testS1() throws Exception {
    String text = "My Test";
    String s = "{t:\"" + text + "\"}";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_TOKEN);
  }

  @Test
  public void testSEscape() throws Exception {
    String text2 = "My\\r\\nTest";
    String s = "{\"t\":'" + text2 + "'}";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
  }
}
