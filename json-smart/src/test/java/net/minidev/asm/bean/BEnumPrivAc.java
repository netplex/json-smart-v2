package net.minidev.asm.bean;

import net.minidev.asm.BeansAccess;

@SuppressWarnings("rawtypes")
public class BEnumPrivAc extends BeansAccess {

	@Override
	public void set(Object object, int methodIndex, Object value) {
		if (methodIndex == 0) {
			if (value != null)
				// value = TEnum.valueOf((String) value);
				value = TEnum.valueOf(value.toString());
			((BEnumPriv) object).setValue((TEnum) value);
			return;
		}
	}

	@Override
	public Object get(Object object, int methodIndex) {
		if (methodIndex == 0) {
			return ((BEnumPriv) object).getValue();
		}
		return null;
	}

	@Override
	public void set(Object object, String methodIndex, Object value) {
		if (methodIndex.equals("value")) {
			if (value != null)
				// value = TEnum.valueOf((String) value);
				value = TEnum.valueOf(value.toString());
			((BEnumPriv) object).setValue((TEnum) value);
			return;
		}
	}

	@Override
	public Object get(Object object, String methodIndex) {
		if (methodIndex.equals("value")) {
			return ((BEnumPriv) object).getValue();
		}
		return null;
	}

	@Override
	public Object newInstance() {
		return new BEnumPriv();
	}
}
