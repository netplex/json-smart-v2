package net.minidev.json;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;
import org.junit.jupiter.api.Test;

public class JStylerObjTest {

  @Test
  public void testIsSpace() {
    assertTrue(JStylerObj.isSpace(' '));
    assertTrue(JStylerObj.isSpace('\t'));
    assertTrue(JStylerObj.isSpace('\n'));
    assertTrue(JStylerObj.isSpace('\r'));

    assertFalse(JStylerObj.isSpace('a'));
    assertFalse(JStylerObj.isSpace('1'));
    assertFalse(JStylerObj.isSpace(':'));
  }

  @Test
  public void testIsSpecialChar() {
    assertTrue(JStylerObj.isSpecialChar('\b'));
    assertTrue(JStylerObj.isSpecialChar('\f'));
    assertTrue(JStylerObj.isSpecialChar('\n'));

    assertFalse(JStylerObj.isSpecialChar('a'));
    assertFalse(JStylerObj.isSpecialChar('\t'));
    assertFalse(JStylerObj.isSpecialChar('\r'));
  }

  @Test
  public void testIsSpecialOpen() {
    assertTrue(JStylerObj.isSpecialOpen('{'));
    assertTrue(JStylerObj.isSpecialOpen('['));
    assertTrue(JStylerObj.isSpecialOpen(','));
    assertTrue(JStylerObj.isSpecialOpen(':'));

    assertFalse(JStylerObj.isSpecialOpen('}'));
    assertFalse(JStylerObj.isSpecialOpen(']'));
    assertFalse(JStylerObj.isSpecialOpen('a'));
  }

  @Test
  public void testIsSpecialClose() {
    assertTrue(JStylerObj.isSpecialClose('}'));
    assertTrue(JStylerObj.isSpecialClose(']'));
    assertTrue(JStylerObj.isSpecialClose(','));
    assertTrue(JStylerObj.isSpecialClose(':'));

    assertFalse(JStylerObj.isSpecialClose('{'));
    assertFalse(JStylerObj.isSpecialClose('['));
    assertFalse(JStylerObj.isSpecialClose('a'));
  }

  @Test
  public void testIsSpecial() {
    assertTrue(JStylerObj.isSpecial('{'));
    assertTrue(JStylerObj.isSpecial('['));
    assertTrue(JStylerObj.isSpecial('}'));
    assertTrue(JStylerObj.isSpecial(']'));
    assertTrue(JStylerObj.isSpecial(','));
    assertTrue(JStylerObj.isSpecial(':'));
    assertTrue(JStylerObj.isSpecial('\''));
    assertTrue(JStylerObj.isSpecial('"'));

    assertFalse(JStylerObj.isSpecial('a'));
    assertFalse(JStylerObj.isSpecial('1'));
    assertFalse(JStylerObj.isSpecial(' '));
  }

  @Test
  public void testIsUnicode() {
    assertTrue(JStylerObj.isUnicode('\u0000'));
    assertTrue(JStylerObj.isUnicode('\u001F'));
    assertTrue(JStylerObj.isUnicode('\u007F'));
    assertTrue(JStylerObj.isUnicode('\u009F'));
    assertTrue(JStylerObj.isUnicode('\u2000'));
    assertTrue(JStylerObj.isUnicode('\u20FF'));

    assertFalse(JStylerObj.isUnicode('a'));
    assertFalse(JStylerObj.isUnicode('Z'));
    assertFalse(JStylerObj.isUnicode('1'));
    assertFalse(JStylerObj.isUnicode(' '));
  }

  @Test
  public void testIsKeyword() {
    assertTrue(JStylerObj.isKeyword("null"));
    assertTrue(JStylerObj.isKeyword("true"));
    assertTrue(JStylerObj.isKeyword("false"));
    assertTrue(JStylerObj.isKeyword("NaN"));

    assertFalse(JStylerObj.isKeyword("NULL"));
    assertFalse(JStylerObj.isKeyword("TRUE"));
    assertFalse(JStylerObj.isKeyword("FALSE"));
    assertFalse(JStylerObj.isKeyword("hello"));
    assertFalse(JStylerObj.isKeyword("a"));
    assertFalse(JStylerObj.isKeyword("ab"));
    assertFalse(JStylerObj.isKeyword(""));
  }

  @Test
  public void testMPSimpleMustBeProtect() {
    JStylerObj.MustProtect mp = JStylerObj.MP_SIMPLE;

    assertFalse(mp.mustBeProtect(null));

    assertTrue(mp.mustBeProtect(""));
    assertTrue(mp.mustBeProtect(" text"));
    assertTrue(mp.mustBeProtect("text "));
    assertTrue(mp.mustBeProtect(" text "));

    assertTrue(mp.mustBeProtect("123"));
    assertTrue(mp.mustBeProtect("-456"));
    assertTrue(mp.mustBeProtect("0"));

    assertTrue(mp.mustBeProtect("null"));
    assertTrue(mp.mustBeProtect("true"));
    assertTrue(mp.mustBeProtect("false"));
    assertTrue(mp.mustBeProtect("NaN"));

    assertTrue(mp.mustBeProtect("text{"));
    assertTrue(mp.mustBeProtect("text["));
    assertTrue(mp.mustBeProtect("text\""));
    assertTrue(mp.mustBeProtect("text'"));

    assertTrue(mp.mustBeProtect("text\n"));
    assertTrue(mp.mustBeProtect("text\t"));
    assertTrue(mp.mustBeProtect("text "));

    assertTrue(mp.mustBeProtect("text\u0001"));

    assertFalse(mp.mustBeProtect("hello"));
    assertFalse(mp.mustBeProtect("world"));
    assertFalse(mp.mustBeProtect("test"));
    assertFalse(mp.mustBeProtect("a"));
  }

  @Test
  public void testMPTrueMustBeProtect() {
    JStylerObj.MustProtect mp = JStylerObj.MP_TRUE;

    assertTrue(mp.mustBeProtect("anything"));
    assertTrue(mp.mustBeProtect(""));
    assertTrue(mp.mustBeProtect("hello"));
    assertTrue(mp.mustBeProtect("123"));
    assertTrue(mp.mustBeProtect(null));
  }

  @Test
  public void testMPAggressiveMustBeProtect() {
    JStylerObj.MustProtect mp = JStylerObj.MP_AGGRESIVE;

    assertFalse(mp.mustBeProtect(null));

    assertTrue(mp.mustBeProtect(""));
    assertTrue(mp.mustBeProtect(" text"));
    assertTrue(mp.mustBeProtect("text "));

    assertTrue(mp.mustBeProtect("null"));
    assertTrue(mp.mustBeProtect("true"));
    assertTrue(mp.mustBeProtect("false"));
    assertTrue(mp.mustBeProtect("NaN"));

    assertTrue(mp.mustBeProtect("123"));
    assertTrue(mp.mustBeProtect("-456"));
    assertTrue(mp.mustBeProtect("3.14"));
    assertTrue(mp.mustBeProtect("1.23e5"));
    assertTrue(mp.mustBeProtect("1.23E-5"));
    assertTrue(mp.mustBeProtect("1.23e+5"));

    assertTrue(mp.mustBeProtect("{text"));
    assertTrue(mp.mustBeProtect("text}"));
    assertTrue(mp.mustBeProtect("text,"));

    assertTrue(mp.mustBeProtect("text\u0001"));

    assertFalse(mp.mustBeProtect("hello"));
    assertFalse(mp.mustBeProtect("world"));
    assertFalse(mp.mustBeProtect("a"));
    assertFalse(mp.mustBeProtect("abc123def"));
  }

  @Test
  public void testMPAggressiveNumberLike() {
    JStylerObj.MustProtect mp = JStylerObj.MP_AGGRESIVE;

    assertTrue(mp.mustBeProtect("0"));
    assertTrue(mp.mustBeProtect("123"));
    assertTrue(mp.mustBeProtect("-456"));
    assertTrue(mp.mustBeProtect("3.14"));
    assertTrue(mp.mustBeProtect("1e5"));
    assertTrue(mp.mustBeProtect("1E5"));
    assertTrue(mp.mustBeProtect("1e+5"));
    assertTrue(mp.mustBeProtect("1e-5"));
    assertTrue(mp.mustBeProtect("1.23e5"));
    assertTrue(mp.mustBeProtect("1.23E-5"));

    assertFalse(mp.mustBeProtect("1e"));
    assertFalse(mp.mustBeProtect("1E"));
    assertFalse(mp.mustBeProtect("123abc"));
    assertFalse(mp.mustBeProtect("12.34.56"));
  }

  @Test
  public void testEscapeLTStringProtector() {
    JStylerObj.StringProtector protector = JStylerObj.ESCAPE_LT;
    StringWriter writer = new StringWriter();

    protector.escape("hello\"world", writer);
    assertTrue(writer.toString().contains("hello\\\"world"));

    writer = new StringWriter();
    protector.escape("line1\nline2", writer);
    assertTrue(writer.toString().contains("line1\\nline2"));

    writer = new StringWriter();
    protector.escape("tab\there", writer);
    assertTrue(writer.toString().contains("tab\\there"));

    writer = new StringWriter();
    protector.escape("back\\slash", writer);
    assertTrue(writer.toString().contains("back\\\\slash"));

    writer = new StringWriter();
    protector.escape("backspace\b", writer);
    assertTrue(writer.toString().contains("backspace\\b"));

    writer = new StringWriter();
    protector.escape("formfeed\f", writer);
    assertTrue(writer.toString().contains("formfeed\\f"));

    writer = new StringWriter();
    protector.escape("carriage\rreturn", writer);
    assertTrue(writer.toString().contains("carriage\\rreturn"));

    writer = new StringWriter();
    protector.escape("unicode\u0001char", writer);
    assertTrue(writer.toString().contains("unicode\\u0001char"));
  }

  @Test
  public void testEscape4WebStringProtector() {
    JStylerObj.StringProtector protector = JStylerObj.ESCAPE4Web;
    StringWriter writer = new StringWriter();

    protector.escape("hello/world", writer);
    assertTrue(writer.toString().contains("hello\\/world"));

    writer = new StringWriter();
    protector.escape("hello\"world", writer);
    assertTrue(writer.toString().contains("hello\\\"world"));

    writer = new StringWriter();
    protector.escape("line1\nline2", writer);
    assertTrue(writer.toString().contains("line1\\nline2"));

    writer = new StringWriter();
    protector.escape("unicode\u0001char", writer);
    assertTrue(writer.toString().contains("unicode\\u0001char"));
  }

  @Test
  public void testEscapeUnicodeCharacters() {
    JStylerObj.StringProtector protector = JStylerObj.ESCAPE_LT;
    StringWriter writer = new StringWriter();

    protector.escape("\u0000\u001F\u007F\u009F", writer);
    String result = writer.toString();
    assertTrue(result.contains("\\u0000"));
    assertTrue(result.contains("\\u001F"));
    assertTrue(result.contains("\\u007F"));
    assertTrue(result.contains("\\u009F"));
  }

  @Test
  public void testEscapeUnicodeRange2000() {
    JStylerObj.StringProtector protector = JStylerObj.ESCAPE_LT;
    StringWriter writer = new StringWriter();

    protector.escape("\u2000\u20FF", writer);
    String result = writer.toString();
    assertTrue(result.contains("\\u2000"));
    assertTrue(result.contains("\\u20FF"));
  }

  @Test
  public void testEscapeNormalCharacters() {
    JStylerObj.StringProtector protector = JStylerObj.ESCAPE_LT;
    StringWriter writer = new StringWriter();

    protector.escape("Hello World 123!", writer);
    String result = writer.toString();
    assertTrue(result.equals("Hello World 123!"));
  }

  @Test
  public void testMPSimpleDigitStarting() {
    JStylerObj.MustProtect mp = JStylerObj.MP_SIMPLE;

    assertTrue(mp.mustBeProtect("0abc"));
    assertTrue(mp.mustBeProtect("1test"));
    assertTrue(mp.mustBeProtect("9xyz"));
    assertTrue(mp.mustBeProtect("-5abc"));

    assertFalse(mp.mustBeProtect("a123"));
    assertFalse(mp.mustBeProtect("test"));
  }

  @Test
  public void testConstants() {
    assertTrue(JStylerObj.MP_SIMPLE instanceof JStylerObj.MustProtect);
    assertTrue(JStylerObj.MP_TRUE instanceof JStylerObj.MustProtect);
    assertTrue(JStylerObj.MP_AGGRESIVE instanceof JStylerObj.MustProtect);
    assertTrue(JStylerObj.ESCAPE_LT instanceof JStylerObj.StringProtector);
    assertTrue(JStylerObj.ESCAPE4Web instanceof JStylerObj.StringProtector);
  }
}
