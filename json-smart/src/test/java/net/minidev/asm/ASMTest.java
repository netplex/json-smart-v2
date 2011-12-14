package net.minidev.asm;

import junit.framework.TestCase;

public class ASMTest extends TestCase {

	public void testGet() throws Exception {
		BeansAccess acc = BeansAccess.get(BTest.class);
		BTest t = new BTest();
		t.pubBoolValue = true;
		t.pubIntValue = 13;
		t.pubStrValue = "Test";
		t.setPrivIntValue(16);
		t.setPrivStrValue("test Priv");
		assertEquals(Integer.valueOf(13), acc.get(t, "pubIntValue"));
		acc.set(t, "pubIntValue", 14);
		assertEquals(Integer.valueOf(14), acc.get(t, "pubIntValue"));
	}
	
	
}
