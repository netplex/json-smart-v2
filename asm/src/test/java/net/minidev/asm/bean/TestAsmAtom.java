package net.minidev.asm.bean;

import junit.framework.TestCase;
import net.minidev.asm.BeansAccess;

public class TestAsmAtom extends TestCase {

	public void testpub() throws Exception {
		// int fieldID = 0;
		String fieldID = "value";
		{
			BeansAccess<BStrPub> ac = BeansAccess.get(BStrPub.class);
			BStrPub p = ac.newInstance();
			String val = "toto";
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BLongPub> ac = BeansAccess.get(BLongPub.class);
			BLongPub p = ac.newInstance();
			Long val = 123L;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BBooleanPub> ac = BeansAccess.get(BBooleanPub.class);
			BBooleanPub p = ac.newInstance();
			Boolean val = Boolean.TRUE;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BBoolPub> ac = BeansAccess.get(BBoolPub.class);
			BBoolPub p = ac.newInstance();
			Boolean val = Boolean.TRUE;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BEnumPriv> ac = BeansAccess.get(BEnumPriv.class);
			BEnumPriv p = ac.newInstance();
			TEnum val = TEnum.V2;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BObjectPriv> ac = BeansAccess.get(BObjectPriv.class);
			BObjectPriv p = ac.newInstance();
			TEnum val = TEnum.V2;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}

	}

	public void testPriv() throws Exception {
		// int fieldID = 0;
		String fieldID = "value";
		{
			BeansAccess<BStrPriv> ac = BeansAccess.get(BStrPriv.class);
			BStrPriv p = ac.newInstance();
			String val = "toto";
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BLongPriv> ac = BeansAccess.get(BLongPriv.class);
			BLongPriv p = ac.newInstance();
			Long val = 123L;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BBooleanPriv> ac = BeansAccess.get(BBooleanPriv.class);
			BBooleanPriv p = ac.newInstance();
			Boolean val = Boolean.TRUE;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BBoolPriv> ac = BeansAccess.get(BBoolPriv.class);
			BBoolPriv p = ac.newInstance();
			Boolean val = Boolean.TRUE;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BEnumPub> ac = BeansAccess.get(BEnumPub.class);
			BEnumPub p = ac.newInstance();
			TEnum val = TEnum.V2;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}
		{
			BeansAccess<BObjectPub> ac = BeansAccess.get(BObjectPub.class);
			BObjectPub p = ac.newInstance();
			TEnum val = TEnum.V2;
			ac.set(p, fieldID, val);
			assertEquals(val, ac.get(p, fieldID));
		}

	}

}