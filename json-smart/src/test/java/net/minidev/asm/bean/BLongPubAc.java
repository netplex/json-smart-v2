package net.minidev.asm.bean;

import net.minidev.asm.BeansAccess;
import net.minidev.json.JSONUtil;

@SuppressWarnings("rawtypes")
public class BLongPubAc extends BeansAccess {

	@Override
	public void set(Object object, int methodIndex, Object value) {
		if (methodIndex == 0) {
			((BLongPub) object).value = JSONUtil.convertToLong(value);
			return;
		}
	}

	@Override
	public Object get(Object object, int methodIndex) {
		if (methodIndex == 0) {
			return ((BLongPub) object).value;
		}
		return null;
	}

	@Override
	public void set(Object object, String methodIndex, Object value) {
		if (methodIndex.equals("value")) {
			((BLongPub) object).value = JSONUtil.convertToLong(value);
			return;
		}
	}

	@Override
	public Object get(Object object, String methodIndex) {
		if (methodIndex.equals("value")) {
			return ((BLongPub) object).value;
		}
		return null;
	}

	@Override
	public Object newInstance() {
		return new BLongPub();
	}

}
