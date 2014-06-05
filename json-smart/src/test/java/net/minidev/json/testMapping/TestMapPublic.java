package net.minidev.json.testMapping;

import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;
import net.minidev.json.JSONValue;

public class TestMapPublic extends TestCase {
	public void testObjInts() throws Exception {
		String s = "{\"vint\":[1,2,3]}";
		T1 r = JSONValue.parse(s, T1.class);
		assertEquals(3, r.vint[2]);
	}

	String MultiTyepJson = "{\"name\":\"B\",\"age\":120,\"cost\":12000,\"flag\":3,\"valid\":true,\"f\":1.2,\"d\":1.5,\"l\":12345678912345}";

	public void testObjMixte() throws Exception {
		T2 r = JSONValue.parse(MultiTyepJson, T2.class);
		assertEquals("B", r.name);
		assertEquals(120, r.age);
		assertEquals(12000, r.cost);
		assertEquals(3, r.flag);
		assertEquals(true, r.valid);
		assertEquals(1.2F, r.f);
		assertEquals(1.5, r.d);
		assertEquals(12345678912345L, r.l);
	}

	public void testObjMixtePrim() throws Exception {
		T3 r = JSONValue.parse(MultiTyepJson, T3.class);
		assertEquals("B", r.name);
		assertEquals(Short.valueOf((short) 120), r.age);
		assertEquals(Integer.valueOf(12000), r.cost);
		assertEquals(Byte.valueOf((byte) 3), r.flag);
		assertEquals(Boolean.TRUE, r.valid);
		assertEquals(1.2F, r.f);
		assertEquals(1.5, r.d);
		assertEquals(Long.valueOf(12345678912345L), r.l);
	}

	public static class T1 {
		public int[] vint;
	}

	public static class T2 {
		public String name;
		public short age;
		public int cost;
		public byte flag;
		public boolean valid;
		public float f;
		public double d;
		public long l;
	}

	public static class T3 {
		public String name;
		public Short age;
		public Integer cost;
		public Byte flag;
		public Boolean valid;
		public Float f;
		public Double d;
		public Long l;
	}

	public static class T123 {
		public T1 t1;
		public T2 t2;
		public T3 t3;
	}


	public static class T5 {
		public Map<String, String> data;
	}
	
	public static class T6 {
		public TreeMap<String, String> data;
	}

	
}
