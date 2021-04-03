package net.minidev.json.testMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

import net.minidev.asm.BeansAccessConfig;
import net.minidev.json.JSONValue;

public class TestAdvancedMapper {
	public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Test
	public void testCustomBean() throws Exception {
		BeansAccessConfig.addTypeMapper(Object.class, MyLocalConverterot.class);
		String s = "{'val':2,'date':'19/04/2010'}";
		TestBean r = JSONValue.parseWithException(s, TestBean.class);
		assertEquals("19/04/2010", sdf.format(r.date));
	}

	public static class TestBean {
		public int val;
		public Date date;
	}

	public static class MyLocalConverterot {

		public static Date fromString(Object text) throws Exception {
			if (text == null)
				return null;
			synchronized (sdf) {
				return sdf.parse(text.toString());
			}
		}
	}
}
