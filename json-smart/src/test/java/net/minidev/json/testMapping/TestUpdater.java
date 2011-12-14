package net.minidev.json.testMapping;

import junit.framework.TestCase;
import net.minidev.json.JSONValue;
import net.minidev.json.testMapping.TestMapPublic.T123;
import net.minidev.json.testMapping.TestMapPublic.T1;
import net.minidev.json.testMapping.TestMapPublic.T2;
import net.minidev.json.testMapping.TestMapPublic.T3;

public class TestUpdater extends TestCase {

	public void testUpdate1() throws Exception {
		T3 t3 = new T3();
		t3.age = 20;
		t3.f = 1.4f;
		t3.l = 120000L;

		String s = "{\"name\":\"text\"}";
		T3 t3_1 = JSONValue.parse(s, t3);
		assertEquals(t3, t3_1);
		assertEquals("text", t3.name);
		assertEquals((Long) 120000L, t3.l);
	}

	public void testUpdateExistingBeans() throws Exception {
		T123 t123 = new T123();
		T1 t1 = new T1(); 
		T2 t2 = new T2(); 
		T3 t3 = new T3(); 
		t123.t1 = t1;
		t123.t2 = t2;
		t123.t3 = t3;
		
		String s = "{\"t2\":{\"name\":\"valueT2\"},\"t3\":{\"name\":\"valueT3\"},}";
		T123 res = JSONValue.parse(s, t123);
		assertEquals(res, t123);
		assertEquals(res.t2, t2);
		assertEquals(res.t2.name, "valueT2");
		assertEquals(res.t3.name, "valueT3");
	}

	public void testUpdateNullBean() throws Exception {
		T123 t123 = new T123();
		T1 t1 = new T1(); 
		T2 t2 = null; 
		T3 t3 = null; 
		t123.t1 = t1;
		t123.t2 = t2;
		t123.t3 = t3;
		
		String s = "{\"t2\":{\"name\":\"valueT2\"},\"t3\":{\"name\":\"valueT3\"},}";
		T123 res = JSONValue.parse(s, t123);
		assertEquals(res, t123);
		assertEquals(res.t2.name, "valueT2");
		assertEquals(res.t3.name, "valueT3");
	}

}
