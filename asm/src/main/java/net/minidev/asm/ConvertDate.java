package net.minidev.asm;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class ConvertDate {
	public static void main(String[] args) {
		System.out.println("ddd");
		System.out.println(convertToDate("23 janvier 2012 13:42:12"));
	}

	static TreeMap<String, Integer> monthsTable = new TreeMap<String, Integer>(new StringCmpNS()); // StringCmpNS.COMP
	static TreeMap<String, Integer> daysTable = new TreeMap<String, Integer>(new StringCmpNS()); // StringCmpNS.COMP

	public static class StringCmpNS implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}
	}

	public static Integer getMonth(String month) {
		return monthsTable.get(month);
	}

	static {

		// for (int c = 1; c <= 31; c++) {
		// String s = Integer.toString(c);
		// if (c < 10)
		// daysTable.put("0".concat(s), c - 1);
		// daysTable.put(s, c - 1);
		// }

		for (int c = 1; c <= 12; c++) {
			String s = Integer.toString(c);
			if (c < 10)
				monthsTable.put("0".concat(s), c - 1);
			monthsTable.put(s, c - 1);
		}

		for (Locale locale : DateFormatSymbols.getAvailableLocales()) {
			DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
			String[] keys = dfs.getMonths();
			for (int i = 0; i < keys.length; i++) {
				fillMap(monthsTable, keys[i], Integer.valueOf(i));
			}
			keys = dfs.getShortMonths();
			for (int i = 0; i < keys.length; i++) {
				fillMap(monthsTable, keys[i], Integer.valueOf(i));
			}
			keys = dfs.getWeekdays();
			for (int i = 0; i < keys.length; i++) {
				fillMap(daysTable, keys[i], Integer.valueOf(i));
			}
			keys = dfs.getShortWeekdays();
			for (int i = 0; i < keys.length; i++) {
				fillMap(daysTable, keys[i], Integer.valueOf(i));
			}
		}
	}

	private static void fillMap(TreeMap<String, Integer> map, String key, Integer value) {
		map.put(key, value);
		key = key.replace("é", "e");
		key = key.replace("û", "u");
		map.put(key, value);
	}

	public static Date convertToDate(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return (Date) obj;
		if (obj instanceof String) {
			StringTokenizer str = new StringTokenizer((String) obj, " -/:");
			String s1 = "";
			if (!str.hasMoreTokens())
				return null;
			s1 = str.nextToken();
			if (s1.length() == 4 && Character.isDigit(s1.charAt(0)))
				return getYYYYMMDD(str, s1);
			return getDDMMYYYY(str, s1);
		}
		throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
	}

	private static Date getYYYYMMDD(StringTokenizer st, String s1) {
		GregorianCalendar cal = new GregorianCalendar(2000, 0, 0, 0, 0, 0);
		cal.setTimeInMillis(0);
		int year = Integer.parseInt(s1);
		cal.set(Calendar.YEAR, year);
		if (!st.hasMoreTokens())
			return cal.getTime();
		s1 = st.nextToken();
		int month = monthsTable.get(s1);
		cal.set(Calendar.MONTH, month);
		if (!st.hasMoreTokens())
			return cal.getTime();
		s1 = st.nextToken();
		int day = monthsTable.get(s1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return addHour(st, cal);
	}

	private static Date getDDMMYYYY(StringTokenizer st, String s1) {
		GregorianCalendar cal = new GregorianCalendar(2000, 0, 0, 0, 0, 0);
		if (daysTable.containsKey(s1)) {
			if (!st.hasMoreTokens())
				return null;
			s1 = st.nextToken();
		}
		if (Character.isDigit(s1.charAt(0))) {
			int day = Integer.parseInt(s1);
			cal.set(Calendar.DAY_OF_MONTH, day);
			if (!st.hasMoreTokens())
				return null;
			s1 = st.nextToken();
			Integer month = monthsTable.get(s1);
			if (month == null)
				throw new NullPointerException("can not parse " + s1 + " as month");
			cal.set(Calendar.MONTH, month);
			if (!st.hasMoreTokens())
				return null;
			s1 = st.nextToken();
			int year = Integer.parseInt(s1);
			if (year < 100) {
				if (year > 20)
					year += 2000;
				else
					year += 1900;
			}
			cal.set(Calendar.YEAR, year);
			return addHour(st, cal);
		} else {
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
			// if (!st.hasMoreTokens())
			// return null;
			// s1 = st.nextToken();
			addHour(st, cal);
			if (!st.hasMoreTokens())
				return null;
			s1 = st.nextToken();
			if (Character.isLetter(s1.charAt(0))) {
				if (!st.hasMoreTokens())
					return null;
				s1 = st.nextToken();
			}
			int year = Integer.parseInt(s1);
			// CET ?
			if (year < 100) {
				if (year > 20)
					year += 2000;
				else
					year += 1900;
			}
			cal.set(Calendar.YEAR, year);
			return cal.getTime();
		}

	}

	private static Date addHour(StringTokenizer st, Calendar cal) {
		String t;
		if (!st.hasMoreTokens())
			return cal.getTime();
		t = st.nextToken();
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t));

		if (!st.hasMoreTokens())
			return cal.getTime();
		t = st.nextToken();
		cal.set(Calendar.MINUTE, Integer.parseInt(t));

		if (!st.hasMoreTokens())
			return cal.getTime();
		t = st.nextToken();
		cal.set(Calendar.SECOND, Integer.parseInt(t));
		return cal.getTime();
	}
}
