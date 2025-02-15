package net.minidev.asm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestDateConvertCustom {
  /**
   * some JAVA version use aternative space.
   *
   * @throws Exception
   */
  @Test
  public void testCANADACustom() throws Exception {
    String testDate = "Jan 23, 2012, 1:42:59 PM";
    ConvertDate.convertToDate(testDate);
    assertTrue(true, "parse " + testDate + " do not crash");
  }
}
