package net.minidev.asm.ex;

/**
 * Same exception as java.lang.NoSuchFieldException but extends RuntimException
 * 
 * @author uriel
 *
 */
public class NoSuchFieldException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoSuchFieldException() {
		super();
	}

	/**
	 * @param message the detail message. The detail message is saved for 
	 *                later retrieval by the Throwable.getMessage() method.
	 */
	public NoSuchFieldException(String message) {
		super(message);
	}
}
