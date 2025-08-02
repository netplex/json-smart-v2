package net.minidev.asm.ex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NoSuchFieldExceptionTest {

  @Test
  public void testDefaultConstructor() {
    NoSuchFieldException exception = new NoSuchFieldException();
    assertNotNull(exception);
    assertNull(exception.getMessage());
    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  public void testConstructorWithMessage() {
    String message = "Test field not found error";
    NoSuchFieldException exception = new NoSuchFieldException(message);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  public void testConstructorWithNullMessage() {
    NoSuchFieldException exception = new NoSuchFieldException(null);
    assertNotNull(exception);
    assertNull(exception.getMessage());
  }

  @Test
  public void testConstructorWithEmptyMessage() {
    String message = "";
    NoSuchFieldException exception = new NoSuchFieldException(message);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
  }
}