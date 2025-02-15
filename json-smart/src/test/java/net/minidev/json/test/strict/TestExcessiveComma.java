package net.minidev.json.test.strict;

import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import net.minidev.json.test.MustThrows;
import org.junit.jupiter.api.Test;

public class TestExcessiveComma {
  @Test
  public void testExcessiveComma1A() throws Exception {
    String s = "[1,2,,3]";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
    JSONValue.parseWithException(s);
  }

  @Test
  public void testExcessiveComma2A() throws Exception {
    String s = "[1,2,]";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
    JSONValue.parseWithException(s);
  }

  @Test
  public void testExcessiveComma3A() throws Exception {
    String s = "[,]";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
    JSONValue.parseWithException(s);
  }

  @Test
  public void testExcessiveComma4A() throws Exception {
    String s = "[,5]";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
    JSONValue.parseWithException(s);
  }

  @Test
  public void testExcessiveComma1O() throws Exception {
    String s = "{\"a\":1,,\"b\":1}";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
    JSONValue.parseWithException(s);
  }

  @Test
  public void testExcessiveComma2O() throws Exception {
    String s = "{\"a\":1,}";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
    JSONValue.parseWithException(s);
  }

  @Test
  public void testExcessiveComma3O() throws Exception {
    String s = "{,}";
    MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_CHAR);
    JSONValue.parseWithException(s);
  }
}
