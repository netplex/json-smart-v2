package net.minidev.asm;

public class NoSuchFiledException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoSuchFiledException() {
		super();
	}

	public NoSuchFiledException(String message) {
		super(message);
	}
}
