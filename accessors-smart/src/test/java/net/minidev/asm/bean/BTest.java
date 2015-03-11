package net.minidev.asm.bean;


public class BTest {
	public int pubIntValue;
	public String pubStrValue;
	private int privIntValue;
	private String privStrValue;
	public boolean pubBoolValue;
	public Integer pubIntegerValue;	
	public TEnum pubTEnum;
	
	public void setPrivIntValue(int privIntValue) {
		this.privIntValue = privIntValue;
	}

	public int getPrivIntValue() {
		return privIntValue;
	}

	public void setPrivStrValue(String privStrValue) {
		this.privStrValue = privStrValue;
	}

	public String getPrivStrValue() {
		return privStrValue;
	}

	public String toString() {
		return "Public(i:" + pubIntValue + " s:" + pubStrValue + "B: " + pubBoolValue + ") Private(i:" + privIntValue
				+ " s:" + privStrValue + ")";
	}
}
