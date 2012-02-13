package net.minidev.asm.bean;

import net.minidev.asm.BeansAccess;

public class BStrPubAc extends BeansAccess {

	@Override
	public void set(Object object, int methodIndex, Object value) {
		if (methodIndex == 0) {
			if (value != null)
				value = value.toString();
			((BStrPub) object).pubStrValue = (String) value;
			return;
		}
	}

	@Override
	public Object get(Object object, int methodIndex) {
		if (methodIndex == 0) {
			return ((BStrPub) object).pubStrValue;
		}
		return null;
	}

	@Override
	public void set(Object object, String methodIndex, Object value) {
		if (methodIndex.equals("pubStrValue")) {
			if (value != null)
				value = value.toString();
			((BStrPub) object).pubStrValue = (String) value;
			return;
		}
	}

	@Override
	public Object get(Object object, String methodIndex) {
		if (methodIndex.equals("pubStrValue")) {
			return ((BStrPub) object).pubStrValue;
		}
		return null;
	}

	@Override
	public Object newInstance() {
		return new BStrPub();
	}

}
