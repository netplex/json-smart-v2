package net.minidev.asm.bean;

import net.minidev.asm.BeansAccess;

public class BStrPrivAc extends BeansAccess {

	@Override
	public void set(Object object, int methodIndex, Object value) {
		if (methodIndex == 0) {
			if (value != null)
				value = value.toString();
			((BStrPriv) object).setPubStrValue((String) value);
			return;
		}
	}

	@Override
	public Object get(Object object, int methodIndex) {
		if (methodIndex == 0) {
			return ((BStrPriv) object).getPubStrValue();
		}
		return null;
	}

	@Override
	public Object newInstance() {
		return new BStrPriv();
	}

}
