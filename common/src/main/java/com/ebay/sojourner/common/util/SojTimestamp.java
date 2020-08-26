package com.ebay.sojourner.common.util;

import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


// FIXME: 1. remove joda, use java 8 time api instead.
//  2. consolidate all datetime utils into one class
public class SojTimestamp {

  public static final long OFFSET = 2208963600000000L;
  public static final int MILLI2MICRO = 1000;
  private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS";
  private static final String DEFAULT_DATE_FORMAT2 = "yyyy-MM-dd";
  private static DateTimeFormatter formatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT)
      .withZone(
          DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT-7")));
  private static DateTimeFormatter formatter2 = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT2)
      .withZone(
          DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT-7")));

  public static String getSojTimestamp(String s) {
    String res;
    //        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    //        sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
    Date date = formatter.parseDateTime(s.substring(0, 23)).toDate();
    long ts = date.getTime();
    long sojTimestamp = (ts * MILLI2MICRO) + OFFSET;
    res = String.valueOf(sojTimestamp);
    return res;
  }

  public static String getDateToSojTimestamp(String s) {
    String res;
    //        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //        sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
    Date date = formatter2.parseDateTime(s.substring(0, 10)).toDate();
    long ts = date.getTime();
    long sojTimestamp = (ts * MILLI2MICRO) + OFFSET;
    res = String.valueOf(sojTimestamp);
    return res;
  }

  public static String getUnixTimestamp(String s) {
    String res;
    //        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    //        sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
    Date date = formatter.parseDateTime(s.substring(0, 23)).toDate();
    long ts = date.getTime();
    res = String.valueOf(ts);
    return res;
  }

  public static Long getSojTimestampToUnixTimestamp(Long s) {
    long ts = (s - OFFSET) / MILLI2MICRO;
    return ts;
  }

  public static void main(String[] args) {
    System.out
        .println(
<<<<<<< HEAD
            getSojTimestampToUnixTimestamp(Long.valueOf(
                getSojTimestamp("2020/08/22 23:59:11.865"))));
=======
            getSojTimestampToUnixTimestamp(Long.valueOf(getSojTimestamp("2020/08/22 23:59:11.865"))));
>>>>>>> 36048a51... fix not serializeble issue and convert to  unix time

    System.out.println(getUnixTimestamp("2020-06-17 02:59:59.000"));
    System.out.println(getSojTimestampToUnixTimestamp(3801622085446000L));
    //    System.out.println(getUnixTimestamp("2020-06-17 02:59:59.000"));
    System.out.println(getSojTimestampToUnixTimestamp(3807074683982000L));//1598111083982

    System.out.println(getSojTimestampToUnixTimestamp(3807076484397000L));//

  }
}
