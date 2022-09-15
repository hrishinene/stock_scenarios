package com.hvn.sensex.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

	// India time zone.
	/** The Constant INDIA_TIME_ZONE. */
	public static final String INDIA_TIMEZONE = "Asia/Kolkata";
	public static final String UTC = "UTC";

	
	/**
	 * Parses the date.
	 *
	 * @param dateStr
	 *            the date str
	 * @return the date
	 */
	public static Date parseDate(String dateStr) {
		return fromTimeString(dateStr, INDIA_TIMEZONE, new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "dd/MM/yyyy",
				"yyyy-MM-dd'T'hh:mm:ssZ", "dd MMM yy", "dd/MM/yy", "dd-MM-yy", "dd-MM-yyyy", "dd-MMM-yy" });

	}


	/**
	 * Return string representation of given timestamp in given timezone
	 * 
	 * @param startTime
	 * @param string
	 * @return
	 */
	public static Date fromTimeString(String timeString, String timeZone, String[] formats) {
		long TwentyfourHoursBefore = -24 * 3600 * 1000;
		timeString = timeString.trim();
		// for NOW - or null - return the current time
		if (timeString == null || timeString.equalsIgnoreCase("NOW"))
			return new Date();

		// if timezone is not privided it is UTC by default - because, short reason: server is in UTC
		if (timeZone == null || timeZone.isBlank())
			timeZone = UTC;

		for (String format : formats) {
			try {
				
				DateFormat fmt = new SimpleDateFormat(format);
				fmt.setTimeZone(TimeZone.getTimeZone(timeZone));
				Date dt = fmt.parse(timeString);
				if (dt != null && dt.getTime() > TwentyfourHoursBefore)
					return dt;

			} catch (ParseException e) {
				continue;
			}
		}

		return null;
	}


	public static Day parseDay(String string) {
		Date dayDate = parseDate(string);
		return new Day(dayDate);
	}
	

	/**
	 * Return string representation of given timestamp in given timezone
	 * 
	 * @param startTime
	 * @param string
	 * @return
	 */
	public static String toTimeString(Date timeStamp, String timeZone, String format) {
		if (timeStamp == null)
			return ""; // callers beware!

		DateFormat fmt = new SimpleDateFormat(format);
		
		// if timezone is not privided it is INDIA_TIMEZONE by default - because, short reason: server is in INDIA_TIMEZONE
		if (timeZone == null || timeZone.isBlank())
			timeZone = INDIA_TIMEZONE;

		fmt.setTimeZone(TimeZone.getTimeZone(timeZone));

		String dateString = fmt.format(timeStamp);
		return dateString;
	}




	public static String getYYYY_MM_DD(Date date) {
		return toTimeString(date, INDIA_TIMEZONE, "yyyy-MM-dd");
	}


	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}


	public static <T> boolean isNullOrEmpty(Collection<T> col) {
		return (col == null || col.isEmpty());
	}

	
	// ======================
	// Logging
	// ======================
	public static final int LOG_DEBUG = 1;
	public static final int LOG_INFO = 2;
	public static final int LOG_WARN = 3;
	public static final int LOG_ERROR = 4;
	public static final int LOG_FATAL = 5;
	
	public static final int LogLevel = LOG_DEBUG;

	public static void logDebug(String string) {
		if (LogLevel <= LOG_DEBUG)
			System.out.println("[DEBUG] " + string);
		
	}	
	
	public static void logInfo(String string) {
		if (LogLevel <= LOG_INFO)
			System.out.println("[INFO] " + string);
		
	}	
	
	public static void logWarn(String string) {
		if (LogLevel <= LOG_WARN)
			System.out.println("[WARN] " + string);
		
	}	
	
	public static void logError(String string) {
		if (LogLevel <= LOG_ERROR)
			System.out.println("[ERROR] " + string);
		
	}	
	
	public static void logFatal(String string) {
		if (LogLevel <= LOG_FATAL)
			System.out.println("[FATAL] " + string);
		
	}
}
