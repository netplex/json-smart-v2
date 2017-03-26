package net.minidev.asm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

public class TestDateConvert extends TestCase {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public void testDateFR() throws Exception {
		String expectedDateText = "23/01/2012 13:42:12";
		ArrayList<String> tests = new ArrayList<String>();
		tests.add("23 janvier 2012 13:42:12");
		tests.add("lundi 23 janvier 2012 13:42:12");
		tests.add("2012-01-23 13:42:12");
		tests.add("Thu Jan 23 13:42:12 PST 2012");
		//
		ConvertDate.convertToDate(null);
		for (String testDate : tests) {
			Date parsed = null;
			try {
				parsed = ConvertDate.convertToDate(testDate);
			} catch (Exception e) {
				System.err.println("can not parse:" + testDate);
				e.printStackTrace();
			}
			assertEquals(expectedDateText, sdf.format(parsed));
		}
	}

	public void testAdvanceTimeStamp() throws Exception {
		String testDate = "2014-08-27T12:53:10+02:00";
		ConvertDate.convertToDate(testDate);
	}

	public void testDateUS() throws Exception {
		testDateLocalized(Locale.US);
	}

	public void testDateFRANCE() throws Exception {
		testDateLocalized(Locale.FRANCE);
	}

	public void testDateCANADA() throws Exception {
		testDateLocalized(Locale.CANADA);
	}

	public void testDateGERMANY() throws Exception {
		testDateLocalized(Locale.GERMANY);
	}

	public void testDateITALY() throws Exception {
		testDateLocalized(Locale.ITALY);
	}

	// MISSING JAPAN / CHINA

	public void testDateLocalized(Locale locale) throws Exception {
		// PM test
		fullTestDate(sdf.parse("23/01/2012 13:42:59"), locale);
		// AM test
		fullTestDate(sdf.parse("23/01/2012 01:42:59"), locale);
	}

	/**
	 * Parse all JDK DateTimeFormat
	 */
	public void fullTestDate(Date expectedDate, Locale locale) throws Exception {
		fullTestDate(expectedDate, locale, "SHORT", DateFormat.SHORT);
		fullTestDate(expectedDate, locale, "MEDIUM", DateFormat.MEDIUM);
		fullTestDate(expectedDate, locale, "LONG", DateFormat.LONG);
		fullTestDate(expectedDate, locale, "FULL", DateFormat.FULL);
	}

	public void fullTestDate(Date expectedDate, Locale locale, String sizeName, int sizeId) throws Exception {
		String expectedDateText = sdf.format(expectedDate);
		DateFormat FormatEN = DateFormat.getDateTimeInstance(sizeId, sizeId, locale);
		String testDate = FormatEN.format(expectedDate);
		Date parse = null;
		try {
			parse = ConvertDate.convertToDate(testDate);
		} catch (Exception e) {
			super.fail("fail to parse " + testDate + " Generated using java " + sizeName);
			//System.err.println("can not parse:" + testDate + " - DateFormat." + sizeName);
			e.printStackTrace();
		}
		String resultStr = sdf.format(parse);
		//System.err.println("TEST: " + testDate + " readed as: " + resultStr);
		if (testDate.contains("59"))
			assertEquals("Test date format Local:" + locale + " format: " + sizeName, expectedDateText, resultStr);
	}

}
