package net.minidev.asm;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * Utility class for converting strings into {@link Date} objects, considering various global date formats.
 * It handles different month and day names across languages, and supports timezone adjustments.
 */
public class ConvertDate {
	/**
	 * default constructor
	 */
	public ConvertDate() {
		super();
	}
	static TreeMap<String, Integer> monthsTable = new TreeMap<String, Integer>(new StringCmpNS()); // StringCmpNS.COMP
	static TreeMap<String, Integer> daysTable = new TreeMap<String, Integer>(new StringCmpNS()); // StringCmpNS.COMP
	private static HashSet<String> voidData = new HashSet<String>();
	/**
     * Default TimeZone used for date conversions. Can be overwritten to change the default time zone.
     */
	public static TimeZone defaultTimeZone;
	/**
     * Comparator for case-insensitive string comparison. Used for sorting and comparing month and day names.
     */
	public static class StringCmpNS implements Comparator<String> {
		/**
		 * default constructor
		 */
		public StringCmpNS() {
			super();
		}

		@Override
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}
	}

	/**
     * Retrieves the month's integer representation based on the provided month name.
     * 
     * @param month the name of the month
     * @return the integer value of the month, or null if the month name is unrecognized
     */
	public static Integer getMonth(String month) {
		return monthsTable.get(month);
	}

	private static Integer parseMonth(String s1) {
		if (Character.isDigit(s1.charAt(0))) {
			return Integer.parseInt(s1) - 1;
		} else {
			Integer month = monthsTable.get(s1);
			if (month == null)
				throw new NullPointerException("can not parse " + s1 + " as month");
			return month.intValue();
		}
	}

	/**
	 * @return a current timezoned 01/01/2000 00:00:00 GregorianCalendar
	 */
	private static GregorianCalendar newCalandar() {
		GregorianCalendar cal = new GregorianCalendar(2000, 0, 0, 0, 0, 0);
		if (defaultTimeZone != null)
			cal.setTimeZone(defaultTimeZone);
		TimeZone TZ = cal.getTimeZone();
		if (TZ == null) {
			TZ = TimeZone.getDefault();
		}
		cal.setTimeInMillis(-TZ.getRawOffset());
		return cal;
	}
	
	static TreeMap<String, TimeZone> timeZoneMapping;
	static {		
		timeZoneMapping = new TreeMap<String, TimeZone>();
		voidData.add("à"); // added for french 1st of may 2021
		voidData.add("at");
		voidData.add("MEZ");
		voidData.add("Uhr");
		voidData.add("h");
		voidData.add("pm");
		voidData.add("PM");
		voidData.add("am");
		voidData.add("AM");
		voidData.add("min"); // Canada french
		voidData.add("um"); // German
		voidData.add("o'clock");
		
		for (String tz : TimeZone.getAvailableIDs()) {
			timeZoneMapping.put(tz, TimeZone.getTimeZone(tz));
		}
		
		for (Locale locale : DateFormatSymbols.getAvailableLocales()) {
			if ("ja".equals(locale.getLanguage()))
				continue;
			if ("ko".equals(locale.getLanguage()))
				continue;
			if ("zh".equals(locale.getLanguage()))
				continue;
			DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
			String[] keys = dfs.getMonths();
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].length() == 0)
					continue;
				fillMap(monthsTable, keys[i], Integer.valueOf(i));
			}
			keys = dfs.getShortMonths();
			for (int i = 0; i < keys.length; i++) {
				String s = keys[i];
				if (s.length() == 0)
					continue;
				if (Character.isDigit(s.charAt(s.length() - 1)))
					continue;
				fillMap(monthsTable, keys[i], Integer.valueOf(i));
				fillMap(monthsTable, keys[i].replace(".", ""), Integer.valueOf(i));
			}
			keys = dfs.getWeekdays();
			for (int i = 0; i < keys.length; i++) {
				String s = keys[i];
				if (s.length() == 0)
					continue;
				fillMap(daysTable, s, Integer.valueOf(i));
				fillMap(daysTable, s.replace(".", ""), Integer.valueOf(i));
			}
			keys = dfs.getShortWeekdays();
			for (int i = 0; i < keys.length; i++) {
				String s = keys[i];
				if (s.length() == 0)
					continue;
				fillMap(daysTable, s, Integer.valueOf(i));
				fillMap(daysTable, s.replace(".", ""), Integer.valueOf(i));
			}
		}
	}

	private static void fillMap(TreeMap<String, Integer> map, String key, Integer value) {
		map.put(key, value);
		key = key.replace("é", "e");
		key = key.replace("û", "u");
		map.put(key, value);
	}

	/**
	 * try read a Date from a Object
	 * @param obj object to convert to date
	 * @return a date value
	 */
	public static Date convertToDate(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return (Date) obj;
		if (obj instanceof Number)
			return new Date(((Number)obj).longValue());
		if (obj instanceof String) {
			obj = ((String) obj)
				.replace("p.m.", "pm")
				.replace("a.m.", "am"); // added on 1st of may 2021
			// contains 2 differents spaces
			StringTokenizer st = new StringTokenizer((String) obj, "  -/:,.+年月日曜時分秒");
			// 2012年1月23日月曜日 13時42分59秒 中央ヨーロッパ標準時
			String s1 = "";
			if (!st.hasMoreTokens())
				return null;
			s1 = st.nextToken();
			if (s1.length() == 4 && Character.isDigit(s1.charAt(0)))
				return getYYYYMMDD(st, s1);
			// skip Day if present.
			if (daysTable.containsKey(s1)) {
				if (!st.hasMoreTokens())
					return null;
				s1 = st.nextToken();
			}
			if (monthsTable.containsKey(s1))
				return getMMDDYYYY(st, s1);

			if (Character.isDigit(s1.charAt(0)))
				return getDDMMYYYY(st, s1);
			return null;
		}
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
	}

	/**
	 * 
	 * @param st StringTokenizer
	 * @param s1 previous token
	 * @return a Date
	 */
	private static Date getYYYYMMDD(StringTokenizer st, String s1) {
		GregorianCalendar cal = newCalandar();

		int year = Integer.parseInt(s1);
		cal.set(Calendar.YEAR, year);
		if (!st.hasMoreTokens())
			return cal.getTime();
		s1 = st.nextToken();

		cal.set(Calendar.MONTH, parseMonth(s1));
		if (!st.hasMoreTokens())
			return cal.getTime();

		s1 = st.nextToken();
		if (Character.isDigit(s1.charAt(0))) {
			if (s1.length() == 5 && s1.charAt(2) == 'T') {
				// TIME + TIMEZONE
				int day = Integer.parseInt(s1.substring(0,2));
				cal.set(Calendar.DAY_OF_MONTH, day);
				return addHour(st, cal, s1.substring(3));
			}
			int day = Integer.parseInt(s1);
			cal.set(Calendar.DAY_OF_MONTH, day);
			return addHour(st, cal, null);
		}
		return cal.getTime();
	}

	/**
	 * @param s1 2 years date
	 * @return a 1900 or 2000 year
	 */
	private static int getYear(String s1) {
		int year = Integer.parseInt(s1);
		// CET ?
		if (year < 100) {
			if (year > 30)
				year += 2000;
			else
				year += 1900;
		}
		return year;
	}
	/**
	 * @param st StringTokenizer
	 * @param s1 privious token
	 * @return a date
	 */
	private static Date getMMDDYYYY(StringTokenizer st, String s1) {
		GregorianCalendar cal = newCalandar();

		Integer month = monthsTable.get(s1);
		if (month == null)
			throw new NullPointerException("can not parse " + s1 + " as month");
		cal.set(Calendar.MONTH, month);
		if (!st.hasMoreTokens())
			return null;
		s1 = st.nextToken();
		// DAY
		int day = Integer.parseInt(s1);
		cal.set(Calendar.DAY_OF_MONTH, day);

		if (!st.hasMoreTokens())
			return null;
		s1 = st.nextToken();
		if (Character.isLetter(s1.charAt(0))) {
			if (!st.hasMoreTokens())
				return null;
			s1 = st.nextToken();
		}
		if (s1.length() == 4)
			cal.set(Calendar.YEAR, getYear(s1));
		else if (s1.length() == 2) {
			return addHour2(st, cal, s1);
			
		}
		// /if (st.hasMoreTokens())
		// return null;
		// s1 = st.nextToken();
		return addHour(st, cal, null);
		// return cal.getTime();
	}

	/**
	 * parse a date as DDMMYYYY
	 * @param st StringTokenizer
	 * @param s1 previous token
	 * @return a Date
	 */
	private static Date getDDMMYYYY(StringTokenizer st, String s1) {
		GregorianCalendar cal = newCalandar();
		
		int day = Integer.parseInt(s1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		if (!st.hasMoreTokens())
			return null;
		s1 = st.nextToken();
		cal.set(Calendar.MONTH, parseMonth(s1));

		if (!st.hasMoreTokens())
			return null;
		s1 = st.nextToken();
		cal.set(Calendar.YEAR, getYear(s1));
		return addHour(st, cal, null);
	}

	/**
	 * @param st StringTokenizer
	 * @param cal Calendar
	 * @param s1 previous token
	 * @return a Date
	 */
	private static Date addHour(StringTokenizer st, Calendar cal, String s1) {
		// String s1;
		if (s1 == null) {
			if (!st.hasMoreTokens())
				return cal.getTime();
			s1 = st.nextToken();
		}
		return addHour2(st, cal, s1);
	}
	
	/**
	 * @param st StringTokenizer
	 * @param cal Calendar
	 * @param s1 previous token
	 * @return a Date
	 */
	private static Date addHour2(StringTokenizer st, Calendar cal, String s1) {
		s1 = trySkip(st, s1, cal);
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s1));

		if (!st.hasMoreTokens())
			return cal.getTime();
		s1 = st.nextToken();

		s1 = trySkip(st, s1, cal);
		if (s1 == null)
			return cal.getTime();

		// if (s1.equalsIgnoreCase("h")) {
		// if (!st.hasMoreTokens())
		// return cal.getTime();
		// s1 = st.nextToken();
		// }
		cal.set(Calendar.MINUTE, Integer.parseInt(s1));

		if (!st.hasMoreTokens())
			return cal.getTime();
		s1 = st.nextToken();

		s1 = trySkip(st, s1, cal);
		if (s1 == null)
			return cal.getTime();

		cal.set(Calendar.SECOND, Integer.parseInt(s1));
		if (!st.hasMoreTokens())
			return cal.getTime();
		s1 = st.nextToken();

		s1 = trySkip(st, s1, cal);
		if (s1 == null)
			return cal.getTime();
		// TODO ADD TIME ZONE
		s1 = trySkip(st, s1, cal);
		// if (s1.equalsIgnoreCase("pm"))
		// cal.add(Calendar.HOUR_OF_DAY, 12);
		
		if (s1.length() == 4 && Character.isDigit(s1.charAt(0)))
			cal.set(Calendar.YEAR, getYear(s1));

		return cal.getTime();
	}

	/**
	 * Handle some Date Keyword like PST UTC am pm ...
	 * @param st StringTokenizer
	 * @param s1 previous token
	 * @param cal Calendar
	 * @return a date
	 */
	private static String trySkip(StringTokenizer st, String s1, Calendar cal) {
		while (true) {
			TimeZone tz = timeZoneMapping.get(s1);
			if (tz != null) {
				cal.setTimeZone(tz);
				if (!st.hasMoreTokens())
					return null;
				s1 = st.nextToken();
				continue;
			}
			if (voidData.contains(s1)) {
				if (s1.equalsIgnoreCase("pm"))
					cal.add(Calendar.AM_PM, Calendar.PM);
				if (s1.equalsIgnoreCase("am"))
					cal.add(Calendar.AM_PM, Calendar.AM);
				if (!st.hasMoreTokens())
					return null;
				s1 = st.nextToken();
				continue;
			}
			return s1;
		}
	}

}
