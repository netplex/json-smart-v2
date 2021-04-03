package net.minidev.json.testMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.minidev.json.JSONValue;

import org.junit.jupiter.api.Test;

public class TestSerPrimArrays {
	SimpleDateFormat sdf;

	String testDateString;
	Date testDate;

	@Test
	public TestSerPrimArrays() {
		try {
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			testDateString = "12/01/2010";
			testDate = sdf.parse(testDateString);
		} catch (Exception e) {
		}

	}

	@Test
	public void testDate() throws Exception {
		String s = "'" + testDateString + "'";
		Date dt = JSONValue.parse(s, Date.class);
		assertEquals(dt, this.testDate);
	}

	@Test
	public void testDtObj() throws Exception {
		String s = "{date:'" + testDateString + "'}";
		ADate dt = JSONValue.parse(s, ADate.class);
		assertEquals(dt.date, this.testDate);
	}

	public static class ADate {
		public Date date;
	}

}
