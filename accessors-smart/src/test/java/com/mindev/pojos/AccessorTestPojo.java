package com.mindev.pojos;

public class AccessorTestPojo {

	// Field with only setter method
	@SuppressWarnings("unused")
	private int writeOnlyField;
	
	// Field with only getter method
	private int readOnlyField;
	
	// Field with both getter and setter methods
	private int readAndWriteableField;

	public void setWriteOnlyField(int writeOnlyField) {
		this.writeOnlyField = writeOnlyField;
	}

	public int getReadOnlyField() {
		return readOnlyField;
	}

	public int getReadAndWriteableField() {
		return readAndWriteableField;
	}

	public void setReadAndWriteableField(int readAndWriteableField) {
		this.readAndWriteableField = readAndWriteableField;
	}

}
