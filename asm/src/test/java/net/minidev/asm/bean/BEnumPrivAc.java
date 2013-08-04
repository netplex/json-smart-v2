package net.minidev.asm.bean;

import net.minidev.asm.BeansAccess;
import net.minidev.asm.ex.NoSuchFiledException;

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
		throw new NoSuchFiledException("mapping BEnumPriv failed to map field:".concat(Integer.toString(methodIndex)));
	}

	@Override
	public Object get(Object object, int methodIndex) {
		if (methodIndex == 0) {
			return ((BEnumPriv) object).getValue();
		}
		throw new NoSuchFiledException("mapping BEnumPriv failed to map field:".concat(Integer.toString(methodIndex)));
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
		throw new NoSuchFiledException("mapping BEnumPriv failed to map field:".concat(methodIndex));
	}

	@Override
	public Object get(Object object, String methodIndex) {
		if (methodIndex.equals("value")) {
			return ((BEnumPriv) object).getValue();
		}
		throw new NoSuchFiledException("mapping BEnumPriv failed to map field:".concat(methodIndex));
	}

	@Override
	public Object newInstance() {
		return new BEnumPriv();
	}
}
