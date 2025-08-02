package net.minidev.asm.ex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ConvertExceptionTest {

  @Test
  public void testDefaultConstructor() {
    ConvertException exception = new ConvertException();
    assertNotNull(exception);
    assertNull(exception.getMessage());
    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  public void testConstructorWithMessage() {
    String message = "Test conversion error";
    ConvertException exception = new ConvertException(message);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  public void testConstructorWithNullMessage() {
    ConvertException exception = new ConvertException(null);
    assertNotNull(exception);
    assertNull(exception.getMessage());
  }

  @Test
  public void testConstructorWithEmptyMessage() {
    String message = "";
    ConvertException exception = new ConvertException(message);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
  }
}