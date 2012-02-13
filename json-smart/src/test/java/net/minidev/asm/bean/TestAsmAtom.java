package net.minidev.asm.bean;

import junit.framework.TestCase;
import net.minidev.asm.BeansAccess;

public class TestAsmAtom extends TestCase {

	public void testpub() throws Exception {
		BeansAccess acASMAuto = BeansAccess.get(BStrPub.class);
		BStrPub p = (BStrPub) acASMAuto.newInstance();
		String val = "toto";
		acASMAuto.set(p, 0, val);
		assertEquals(val, acASMAuto.get(p, 0));
	}
}
