package net.minidev.asm.bean;

import net.minidev.asm.BeansAccess;

@SuppressWarnings("rawtypes")
public class BStrPubAc extends BeansAccess {

	@Override
	public void set(Object object, int methodIndex, Object value) {
		if (methodIndex == 0) {
			if (value != null)
				value = value.toString();
			((BStrPub)object).value = (String) value;
			return;
		}
	}

	@Override
	public Object get(Object object, int methodIndex) {
		if (methodIndex == 0) {
			return ((BStrPub)object).value;
		}
		return null;
	}

	@Override
	public void set(Object object, String methodIndex, Object value) {
		if (methodIndex.equals("value")) {
			if (value != null)
				value = value.toString();
			((BStrPub) object).value = (String) value;
			return;
		}
	}

	@Override
	public Object get(Object object, String methodIndex) {
		if (methodIndex.equals("value")) {
			return ((BStrPub) object).value;
		}
		return null;
	}

	@Override
	public BStrPub newInstance() {
		return new BStrPub();
	}

}
