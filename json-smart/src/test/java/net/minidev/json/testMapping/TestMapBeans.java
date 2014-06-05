package net.minidev.json.testMapping;

import java.util.Map;

import junit.framework.TestCase;
import net.minidev.json.JSONValue;

public class TestMapBeans extends TestCase {

	public void testObjInts() throws Exception {
		String s = "{\"vint\":[1,2,3]}";
		T1 r = JSONValue.parse(s, T1.class);
		assertEquals(3, r.vint[2]);
	}

	public void testObjIntKey() throws Exception {
		String s = "{\"data\":{\"1\":\"toto\"}}";
		T2 r = JSONValue.parse(s, T2.class);
		assertEquals("toto", r.data.get(1));
	}

	public void testObjEnumKey() throws Exception {
		String s = "{\"data\":{\"red\":10}}";
		T3 r = JSONValue.parse(s, T3.class);
		assertEquals((Integer)10, r.data.get(ColorEnum.red));
	}

	public void testObjBool1() throws Exception {
		String s = "{\"data\":true}";
		T4 r = JSONValue.parse(s, T4.class);
		assertEquals(true, r.data);
	}

	public void testObjBool2() throws Exception {
		String s = "{\"data\":true}";
		T5 r = JSONValue.parse(s, T5.class);
		assertEquals(true, r.data);
	}

	/**
	 * class containing primitive array;
	 */
	public static class T1 {
		private int[] vint;

		public int[] getVint() {
			return vint;
		}

		public void setVint(int[] vint) {
			this.vint = vint;
		}
	}

	/**
	 * class containing Map interface;
	 */
	public static class T2 {
		private Map<Integer, String> data;

		public Map<Integer, String> getData() {
			return data;
		}

		public void setData(Map<Integer, String> data) {
			this.data = data;
		}
	}

	public static enum ColorEnum {
		bleu, green, red, yellow
	}

	public static class T3 {
		private Map<ColorEnum, Integer> data;

		public Map<ColorEnum, Integer> getData() {
			return data;
		}

		public void setData(Map<ColorEnum, Integer> data) {
			this.data = data;
		}
	}
	
	
	public static class T4 {
		private boolean data;

		public boolean getData() {
			return data;
		}

		public void setData(boolean data) {
			this.data = data;
		}
	}

	public static class T5 {
		private boolean data;

		public boolean isData() {
			return data;
		}

		public void setData(boolean data) {
			this.data = data;
		}
	}
	
}
