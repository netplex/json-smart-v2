package net.minidev.json.test;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class SerializeReadonlyField {
	/**
 	* https://github.com/netplex/json-smart-v2/issues/49
 	*/
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
	
	/**
	 * https://github.com/netplex/json-smart-v2/issues/59
	 */
	@Test
    public void test() {
		// should not crash
        Map<String, Object> cachedTable1 = new LinkedHashMap<>();
        Iterable<Path> path = Paths.get("/");
        cachedTable1.put("1", path);
        JSONValue.toJSONString(cachedTable1);
    }
}
