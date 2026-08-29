package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;

/**
 * A number literal has no length limit, so {@code new BigInteger(String)} / {@code new
 * BigDecimal(String)} can be handed an arbitrarily long attacker controlled digit string. Their
 * cost grows superlinearly in the digit count: measured n^2.0 on Temurin 21.0.11 over the range
 * 100_000 to 1_600_000 digits, where a single 1,600,000 digit literal costs about 32 s of CPU on
 * one core.
 *
 * <p>These tests pin the limit down the same way {@code TestCVE202457699} pins the depth limit:
 * every predefined mode must reject the payload, and ordinary numbers must keep working.
 */
public class TestNumberLengthLimit {

  /** Long enough to be rejected, short enough that an unpatched parser still answers quickly. */
  private static final String LONG_INT = repeat('9', 2000);

  private static final String LONG_DEC = "1." + repeat('3', 2000);

  private static final int[] PREDEFINED_MODES = {
    JSONParser.MODE_PERMISSIVE,
    JSONParser.MODE_PERMISSIVE_WITH_INCOMPLETE,
    JSONParser.MODE_RFC4627,
    JSONParser.MODE_JSON_SIMPLE,
    JSONParser.MODE_STRICTEST,
  };

  @Test
  public void everyPredefinedModeShouldRejectLongIntegerLiteral() {
    for (int mode : PREDEFINED_MODES) {
      ParseException e =
          assertThrows(
              ParseException.class,
              () -> new JSONParser(mode).parse(LONG_INT),
              "mode " + mode + " accepted a " + LONG_INT.length() + " digit integer literal");
      assertEquals(ParseException.ERROR_UNEXPECTED_NUMBER_LENGTH, e.getErrorType());
    }
  }

  @Test
  public void everyPredefinedModeShouldRejectLongDecimalLiteral() {
    for (int mode : PREDEFINED_MODES) {
      ParseException e =
          assertThrows(
              ParseException.class,
              () -> new JSONParser(mode).parse(LONG_DEC),
              "mode " + mode + " accepted a " + LONG_DEC.length() + " char decimal literal");
      assertEquals(ParseException.ERROR_UNEXPECTED_NUMBER_LENGTH, e.getErrorType());
    }
  }

  /** The literal is normally nested in an object, as it would be in a request body. */
  @Test
  public void everyPredefinedModeShouldRejectLongLiteralInsideAnObject() {
    for (int mode : PREDEFINED_MODES) {
      assertThrows(
          ParseException.class,
          () -> new JSONParser(mode).parse("{\"x\":" + LONG_INT + "}"),
          "mode " + mode + " accepted a long integer literal inside an object");
      assertThrows(
          ParseException.class,
          () -> new JSONParser(mode).parse("[" + LONG_DEC + "]"),
          "mode " + mode + " accepted a long decimal literal inside an array");
    }
  }

  /** A negative literal takes the other branch of the sign test in parseNumber. */
  @Test
  public void negativeLongLiteralShouldBeRejected() {
    assertThrows(
        ParseException.class,
        () -> new JSONParser(JSONParser.MODE_PERMISSIVE).parse("-" + LONG_INT));
  }

  /** Exactly at the limit is still accepted; one over is not. Off by one guard. */
  @Test
  public void limitBoundaryIsInclusive() throws Exception {
    String atLimit = repeat('9', 1000);
    String overLimit = repeat('9', 1001);

    Object ok = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(atLimit);
    assertInstanceOf(BigInteger.class, ok);
    assertEquals(new BigInteger(atLimit), ok);

    assertThrows(
        ParseException.class, () -> new JSONParser(JSONParser.MODE_PERMISSIVE).parse(overLimit));
  }

  /** The limit must not disturb numbers of ordinary size, on any of the numeric branches. */
  @Test
  public void ordinaryNumbersAreUnaffected() throws Exception {
    JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
    assertEquals(0, p.parse("0"));
    assertEquals(-1, p.parse("-1"));
    assertEquals(42, p.parse("42"));
    assertEquals(
        Long.MAX_VALUE, new JSONParser(JSONParser.MODE_PERMISSIVE).parse("9223372036854775807"));
    assertEquals(
        new BigInteger("9223372036854775808"),
        new JSONParser(JSONParser.MODE_PERMISSIVE).parse("9223372036854775808"));
    assertEquals(1.5d, new JSONParser(JSONParser.MODE_PERMISSIVE).parse("1.5"));
    assertEquals(
        new BigDecimal("1.2345678901234567890123"),
        new JSONParser(JSONParser.MODE_PERMISSIVE).parse("1.2345678901234567890123"));

    JSONObject o =
        (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse("{\"a\":1,\"b\":2.5e3}");
    assertEquals(1, o.get("a"));
  }

  /**
   * The point of making the limit opt-OUT: a caller that hand builds its own bitmask, and so never
   * hears about a new flag, is protected anyway.
   *
   * <p>This bitmask is not hypothetical. It is the one com.nimbusds:oauth2-oidc-sdk 11.38.2 uses in
   * com/nimbusds/oauth2/sdk/util/JSONUtils.java, in both parseJSON and parseJSONKeepingOrder. That
   * SDK declares json-smart at compile scope, and it already sets LIMIT_JSON_DEPTH because the
   * CVE-2024-57699 fix had to be applied by hand downstream. An opt-in limit would have left it
   * exposed a second time.
   */
  @Test
  public void handBuiltDownstreamBitmaskIsProtectedWithoutChanges() {
    int oidcSdkBitmask =
        JSONParser.USE_HI_PRECISION_FLOAT
            | JSONParser.ACCEPT_TAILLING_SPACE
            | JSONParser.LIMIT_JSON_DEPTH;

    assertThrows(
        ParseException.class,
        () -> new JSONParser(oidcSdkBitmask).parse("{\"x\":" + LONG_INT + "}"),
        "hand built downstream bitmask accepted a long integer literal");
    assertThrows(
        ParseException.class,
        () -> new JSONParser(oidcSdkBitmask).parse("{\"x\":" + LONG_DEC + "}"),
        "hand built downstream bitmask accepted a long decimal literal");
  }

  /**
   * The escape hatch: an application that genuinely needs arbitrary precision literals opts out
   * explicitly.
   */
  @Test
  public void limitCanBeOptedOutOfExplicitly() throws Exception {
    int noLimit = JSONParser.MODE_PERMISSIVE | JSONParser.UNRESTRICTED_NUMBER_LENGTH;
    Object r = new JSONParser(noLimit).parse(LONG_INT);
    assertInstanceOf(BigInteger.class, r);
    assertEquals(new BigInteger(LONG_INT), r);
  }

  /** No predefined mode may switch the limit off. This is what CVE-2024-57699 asked for. */
  @Test
  public void noPredefinedModeDisablesTheLimit() {
    for (int mode : PREDEFINED_MODES) {
      assertEquals(
          0,
          mode & JSONParser.UNRESTRICTED_NUMBER_LENGTH,
          "mode " + mode + " disables the number length limit");
    }
  }

  /** The rejection message must not echo the offending token, which can be megabytes long. */
  @Test
  public void errorMessageDoesNotEchoTheOffendingToken() {
    ParseException e =
        assertThrows(
            ParseException.class, () -> new JSONParser(JSONParser.MODE_PERMISSIVE).parse(LONG_INT));
    assertTrue(
        e.getMessage().length() < 200, "message should not embed the token: " + e.getMessage());
    assertTrue(e.getMessage().contains("2000"), e.getMessage());
  }

  private static String repeat(char c, int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append(c);
    }
    return sb.toString();
  }
}
