package net.minidev.json.test;

import net.minidev.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * https://github.com/netplex/json-smart-v2/issues/49
 */
public class SerializeReadonlyField {
	@Test
	public static void main(String[] args) {
		MyData data = new MyData("a");
		Map<String, Object> m = new HashMap<>();
		m.put("data", data);
		String a = new JSONObject(m).toString();
		assertEquals("{\"data\":{\"someField\":\"a\"}}", a.toString());
	}

	public static class MyData {
		private String someField;

		public MyData(String someField) {
			this.someField = someField;
		}

		public String getSomeField() {
			return someField;
		}

		// Remove comment to make serialization to work
		/*
		 * public void setSomeField(String someField) { this.someField = someField; }
		 */
	}
}
