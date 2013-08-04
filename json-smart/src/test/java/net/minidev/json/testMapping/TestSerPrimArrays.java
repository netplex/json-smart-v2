package net.minidev.json.testMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import net.minidev.json.JSONValue;

public class TestSerPrimArrays extends TestCase {
	SimpleDateFormat sdf;

	String testDateString;
	Date testDate;

	public TestSerPrimArrays() {
		try {
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			testDateString = "12/01/2010";
			testDate = sdf.parse(testDateString);
		} catch (Exception e) {
		}

	}

	public void testDate() throws Exception {
		String s = "'" + testDateString + "'";
		Date dt = JSONValue.parse(s, Date.class);
		assertEquals(dt, this.testDate);
	}

	public void testDtObj() throws Exception {
		String s = "{date:'" + testDateString + "'}";
		ADate dt = JSONValue.parse(s, ADate.class);
		assertEquals(dt.date, this.testDate);
	}

	public static class ADate {
		public Date date;
	}

}
