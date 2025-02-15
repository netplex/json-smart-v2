package net.minidev.asm.ex;

/**
 * An exception that is thrown to indicate a problem occurred during a conversion process. This
 * class extends {@link RuntimeException} and is used to signify errors encountered while converting
 * between types, typically within a dynamic type conversion framework or library.
 */
public class ConvertException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new {@code ConvertException} with {@code null} as its detail message. The cause is
   * not initialized, and may subsequently be initialized by a call to {@link #initCause}.
   */
  public ConvertException() {
    super();
  }

  /**
   * Constructs a new {@code ConvertException} with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public ConvertException(String message) {
    super(message);
  }
}
