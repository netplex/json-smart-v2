package net.minidev.json.testMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;

public class TestDate {
  @Test
  public void testBooleans() throws Exception {
    String s = "[true,true,false]";
    boolean[] bs = new boolean[] {true, true, false};
    String s2 = JSONValue.toJSONString(bs);
    assertEquals(s, s2);
  }

  @Test
  public void testInts() throws Exception {
    String s = "[1,2,3]";
    int[] bs = new int[] {1, 2, 3};
    String s2 = JSONValue.toJSONString(bs);
    assertEquals(s, s2);
  }
}
