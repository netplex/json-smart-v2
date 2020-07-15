package net.minidev.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

public class TestDateConvert {
	// we do not test the century
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	SimpleDateFormat sdfLT = new SimpleDateFormat("dd/MM/yy HH:mm");

	@Test
	public void testDateFR() throws Exception {
		String expectedDateText = "23/01/12 13:42:12";
		ArrayList<String> tests = new ArrayList<String>();
		tests.add("23 janvier 2012 13:42:12");
		tests.add("lundi 23 janvier 2012 13:42:12");
		tests.add("2012-01-23 13:42:12");
		// need to use the same time Zone
		// tests.add("Thu Jan 23 13:42:12 PST 2012");
		//
		// tests.add("Thu Jan 23 13:42:12 CET 2012");
		ConvertDate.convertToDate(null);
		for (String testDate : tests) {
			String jobName = "Parsing FR Date:" + testDate;
			Date parsed = null;
			try {
				parsed = ConvertDate.convertToDate(testDate);
			} catch (Exception e) {
				throw new Exception(jobName, e);
			}
			assertEquals(expectedDateText, sdfFull.format(parsed), jobName);
		}
	}

	@Test
	public void testAdvanceTimeStamp() throws Exception {
		String testDate = "2014-08-27T12:53:10+02:00";
		ConvertDate.convertToDate(testDate);
	}

	@Test
	public void testDateUS() throws Exception {
		testDateLocalized(Locale.US);
	}

	@Test
	public void testDateFRANCE() throws Exception {
		testDateLocalized(Locale.FRANCE);
	}

	//@Test
	//public void testDateCANADA() throws Exception {
	//	testDateLocalized(Locale.CANADA);
	//}

	//@Test
	//public void testDateGERMANY() throws Exception {
	//	testDateLocalized(Locale.GERMANY);
	//}

	@Test
	public void testDateITALY() throws Exception {
		testDateLocalized(Locale.ITALY);
	}

	// MISSING JAPAN / CHINA

	public void testDateLocalized(Locale locale) throws Exception {
		// PM test
		fullTestDate(sdfFull.parse("23/01/2012 13:42:59"), locale);
		// AM test
		fullTestDate(sdfFull.parse("23/01/2012 01:42:59"), locale);
	}

	/**
	 * Parse all JDK DateTimeFormat
	 */
	public void fullTestDate(Date expectedDate, Locale locale) throws Exception {
		fullTestDate(expectedDate, locale, "SHORT", DateFormat.SHORT);
		// fullTestDate(expectedDate, locale, "MEDIUM", DateFormat.MEDIUM);
		// fullTestDate(expectedDate, locale, "LONG", DateFormat.LONG);
		// fullTestDate(expectedDate, locale, "FULL", DateFormat.FULL);
	}

	public void fullTestDate(Date expectedDate, Locale locale, String sizeName, int sizeId) throws Exception {
		String jobName = "Test date format Local:" + locale + " format: " + sizeName;
		DateFormat FormatEN = DateFormat.getDateTimeInstance(sizeId, sizeId, locale);
		String testDate = FormatEN.format(expectedDate);
		Date parse = null;
		try {
			// can not parse US Date in short mode.
			if (sizeId == DateFormat.SHORT && locale.equals(Locale.US))
				return;
				//parse = ConvertDate.convertToDate(obj)(testDate);
			else
				parse = ConvertDate.convertToDate(testDate);
		} catch (Exception e) {
			throw new Exception(jobName, e);
		}
		//System.err.println("TEST: " + testDate + " readed as: " + resultStr);
		// is source format contains second
		if (testDate.contains("59")) {
			String resultStr = sdfFull.format(parse);
			String expectedDateText = sdfFull.format(expectedDate);
			assertEquals(expectedDateText, resultStr, jobName);
		} else {
			String resultStr = sdfLT.format(parse);
			String expectedDateText = sdfLT.format(expectedDate);
			assertEquals(expectedDateText, resultStr, jobName);
		}
//			System.err.printf("no sec for Format %-6s %-40s -> %10s\n", sizeName, testDate, resultStr);
	}

}
