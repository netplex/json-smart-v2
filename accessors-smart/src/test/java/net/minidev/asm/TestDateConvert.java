package net.minidev.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

public class TestDateConvert {
	// we do not test the century
	static TimeZone MY_TZ = TimeZone.getTimeZone("PST");
	
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	SimpleDateFormat sdfLT = new SimpleDateFormat("dd/MM/yy HH:mm");

	/**
	 * some old java version date API works differently an cause error in tests
	 * @return
	 */
	static int getJavaVersion() {
		String javaVersion = System.getProperty("java.version");
        // Extracting major version from java version string
        int majorVersion = Integer.parseInt(javaVersion.split("\\.")[1]);
        return majorVersion;
	}


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

	public TestDateConvert() {
		super();
		ConvertDate.defaultTimeZone = MY_TZ;
		if (MY_TZ != null) {
			sdfFull.setTimeZone(MY_TZ);
			sdfLT.setTimeZone(MY_TZ);
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

	@Test
	public void testDateCANADA() throws Exception {
		testDateLocalized(Locale.CANADA);
	}

	@Test
	public void testDateGERMANY() throws Exception {
		testDateLocalized(Locale.GERMANY);
	}

	@Test
	public void testDateITALY() throws Exception {
		testDateLocalized(Locale.ITALY);
	}

	@Test
	public void testDateCANADA_FRENCH() throws Exception {
		testDateLocalized(Locale.CANADA_FRENCH);
	}

	@Test
	public void testDateJAPAN() throws Exception {
		if (getJavaVersion() == 8) {
			assertTrue(true, "Ignoring Japan Date test for Java 8");
		} else {
			testDateLocalized(Locale.JAPAN);
		}
	}

	// public void testDateCHINA() throws Exception {
	// testDateLocalized(Locale.CHINA);
	// }

	// public void testDateCHINESE() throws Exception {
	// testDateLocalized(Locale.CHINESE);
	// }

	// MISSING CHINA / CHINESE

	public void testDateLocalized(Locale locale) throws Exception {
		// PM test
		Date pm = sdfFull.parse("23/01/2012 13:42:59");
		fullTestDate(pm, locale);
		// AM test
		Date am = sdfFull.parse("23/01/2012 01:42:59");
		fullTestDate(am, locale);
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
		DateFormat FormatEN = DateFormat.getDateTimeInstance(sizeId, sizeId, locale);
		if (MY_TZ != null) {
			FormatEN.setTimeZone(MY_TZ);
		}
		String testDate = FormatEN.format(expectedDate);
		Date parse = null;
		String jobName = "Test date format Local:" + locale + " size:" + sizeName + " String:\"" + testDate + "\"";
		try {
			// can not parse US style Date in short mode (due to reversed day/month).
			if (sizeId == DateFormat.SHORT) {
				if (locale.equals(Locale.US))
					return;
				if (locale.equals(Locale.CANADA_FRENCH))
					return;
			}
			parse = ConvertDate.convertToDate(testDate);
		} catch (Exception e) {
			throw new Exception(jobName, e);
		}
		// System.err.println("TEST: " + testDate + " readed as: " + resultStr);
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
