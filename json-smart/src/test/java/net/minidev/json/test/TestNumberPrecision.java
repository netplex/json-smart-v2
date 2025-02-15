package net.minidev.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;

public class TestNumberPrecision {
  @Test
  public void testMaxLong() {
    Long v = Long.MAX_VALUE;
    String s = "[" + v + "]";
    JSONArray array = (JSONArray) JSONValue.parse(s);
    Object r = array.get(0);
    assertEquals(v, r);
  }

  @Test
  public void testMinLong() {
    Long v = Long.MIN_VALUE;
    String s = "[" + v + "]";
    JSONArray array = (JSONArray) JSONValue.parse(s);
    Object r = array.get(0);
    assertEquals(v, r);
  }

  @Test
  public void testMinBig() {
    BigInteger v = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);
    String s = "[" + v + "]";
    JSONArray array = (JSONArray) JSONValue.parse(s);
    Object r = array.get(0);
    assertEquals(v, r);
  }

  @Test
  public void testMaxBig() {
    BigInteger v = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
    String s = "[" + v + "]";
    JSONArray array = (JSONArray) JSONValue.parse(s);
    Object r = array.get(0);
    assertEquals(v, r);
  }
}
