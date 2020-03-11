package com.ebay.sojourner.ubd.common.sharedlib.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SOJTS2Date {

  // the offset align with UTC-7
  public static final long OFFSET =
      2208963600000000L; // 25567L *24 * 3600 * 1000 * 1000 - 7 * 3600 * 1000 * 1000;
  public static final long MILSECOFDAY = 86400000000L; // 24 * 3600 * 1000 * 1000
  public static final int MILLI2MICRO = 1000;
  public static final String EBAY_TIMEZONE = "GMT-7";

  /**
   * Get Sojourner default Calendar for being used.
   */
  public static Calendar getCalender() {
    return Calendar.getInstance(TimeZone.getTimeZone(EBAY_TIMEZONE), Locale.US);
  }

  public static DateFormat getDateFormat(String pattern) {
    DateFormat dateFormat = new SimpleDateFormat(pattern);
    // Make consistent with getCalender()
    dateFormat.setTimeZone(TimeZone.getTimeZone(EBAY_TIMEZONE));
    return dateFormat;
  }

  public static long getSojTimestamp(long milliseconds) {
    return (milliseconds * MILLI2MICRO) + OFFSET;
  }

  public static long getUnixTimestamp(long microseconds) {
    return (microseconds - OFFSET) / MILLI2MICRO;
  }

  public static long castSojTimestampToDate(long microseconds) {
    return microseconds - (microseconds % MILSECOFDAY);
  }

  public static long getUnixDate(long microseconds) {
    microseconds = castSojTimestampToDate(microseconds);
    return getUnixTimestamp(microseconds);
  }

  public static Date getDate(long ts) {
    try {
      return new Date((ts - OFFSET) / MILLI2MICRO);
    } catch (Exception e) {
      return null;
    }
  }

  public static String normalized(String ts) {
    try {
      long timestamp = Long.valueOf(ts.trim());
      timestamp = (timestamp - OFFSET) / MILLI2MICRO;
      return String.valueOf(timestamp);
    } catch (Exception e) {
      throw new RuntimeException("normalized timestamp failed", e);
    }
  }

  public static void main(String[] args) {
    long millis = getUnixTimestamp(3584201122910000L);
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-7"));
    calendar.setTimeInMillis(millis);
    System.out.println("=================Use Calendar Fields=====================");
    System.out.print(calendar.get(Calendar.YEAR));
    System.out.print("-" + (calendar.get(Calendar.MONTH) + 1));
    System.out.print("-" + calendar.get(Calendar.DAY_OF_MONTH));
    System.out.print(" " + calendar.get(Calendar.HOUR_OF_DAY));
    System.out.print(":" + calendar.get(Calendar.MINUTE));
    System.out.print(":" + calendar.get(Calendar.SECOND));
    System.out.println();
    System.out.println("=================Use GetTime + DateFormat =====================");
    String DATE_PARTITION = "yyyy-MM-dd HH:mm:ss";
    DateFormat DATE_PARTITION_FORMAT = new SimpleDateFormat(DATE_PARTITION);
    System.out.println(DATE_PARTITION_FORMAT.format(calendar.getTime()));
    System.out.println(
        "==============Use GetTime + DateFormat with Set TimeZone or Calendar ================");

    long ts = getUnixTimestamp(3586117768000000L);

    // 3586113856370000L
    calendar.setTimeInMillis(ts);
    DATE_PARTITION_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT-7"));
    // DATE_PARTITION_FORMAT.setCalendar(calendar);
    System.out.println(DATE_PARTITION_FORMAT.format(calendar.getTime()));

    Calendar calendar2 = getCalender();
    calendar2.set(Calendar.YEAR, 2013);
    calendar2.set(Calendar.MONTH, 7);
    calendar2.set(Calendar.DAY_OF_MONTH, 21);
    calendar2.set(Calendar.HOUR_OF_DAY, 23);

    calendar2.set(Calendar.MINUTE, 49);

    calendar2.set(Calendar.SECOND, 28);
    calendar2.set(Calendar.MILLISECOND, 0);
    System.out.println(calendar2.getTimeInMillis());
    System.out.println(DATE_PARTITION_FORMAT.format(calendar2.getTime()));
    System.out.println(getSojTimestamp(calendar2.getTimeInMillis()));
  }
}
