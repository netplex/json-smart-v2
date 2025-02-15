package net.minidev.json.testMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;

public class TestMapPrimArrays {
  @Test
  public void testInts() throws Exception {
    String s = "[1,2,3]";
    int[] r = JSONValue.parse(s, int[].class);
    assertEquals(3, r[2]);
  }

  @Test
  public void testIntss() throws Exception {
    String s = "[[1],[2],[3,4]]";
    int[][] r = JSONValue.parse(s, int[][].class);
    assertEquals(3, r[2][0]);
    assertEquals(4, r[2][1]);
  }

  @Test
  public void testLongs() throws Exception {
    String s = "[1,2,3]";
    long[] r = JSONValue.parse(s, long[].class);
    assertEquals(3, r[2]);
  }

  @Test
  public void testFloat() throws Exception {
    String s = "[1.2,22.4,3.14]";
    float[] r = JSONValue.parse(s, float[].class);
    assertEquals(3.14F, r[2]);
  }

  @Test
  public void testDouble() throws Exception {
    String s = "[1.2,22.4,3.14]";
    double[] r = JSONValue.parse(s, double[].class);
    assertEquals(3.14, r[2]);
  }

  @Test
  public void testBooleans() throws Exception {
    String s = "[true,true,false]";
    boolean[] r = JSONValue.parse(s, boolean[].class);
    assertEquals(true, r[1]);
    assertEquals(false, r[2]);
  }
}
