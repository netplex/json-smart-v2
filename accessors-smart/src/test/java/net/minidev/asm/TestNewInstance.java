package net.minidev.asm;

import java.util.TreeMap;

import junit.framework.TestCase;

public class TestNewInstance extends TestCase {
	public void testLangUtilPkg() {
		@SuppressWarnings("rawtypes")
		BeansAccess<TreeMap> acTm = BeansAccess.get(TreeMap.class);
		acTm.newInstance();
	}
}
