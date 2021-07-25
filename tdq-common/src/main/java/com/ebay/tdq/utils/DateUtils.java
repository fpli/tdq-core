package com.ebay.tdq.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang3.time.FastDateFormat;
import scala.concurrent.duration.Duration;

/**
 * @author juntzhang
 */
public class DateUtils {

  public static Long parseDateTime(String str, TimeZone zone) throws ParseException {
    return parseDate(str, zone).getTime();
  }

  public static Date parseDate(String str) throws ParseException {
    return FastDateFormat
        .getInstance("yyyy-MM-dd HH:mm:ss")
        .parse(str);
  }

  public static Date parseDate(String str, TimeZone zone) throws ParseException {
    return FastDateFormat
        .getInstance("yyyy-MM-dd HH:mm:ss", zone)
        .parse(str);
  }

  public static String format(Long ts, TimeZone zone) {
    return FastDateFormat
        .getInstance("yyyy-MM-dd HH:mm:ss", zone)
        .format(ts);
  }

  public static String getMinBuckets(long ts, int minute, TimeZone zone) {
    return FastDateFormat
        .getInstance("yyyyMMddHHmm", zone)
        .format(ts / 60_000 / minute * 60_000 * minute);
  }

  public static String getMinBuckets(long ts, int minute) {
    return FastDateFormat.getInstance("yyyyMMddHHmm",
        TimeZone.getTimeZone("CTT")).format(ts / 60_000 / minute * 60_000 * minute);
  }

  /*
   * @param length "d day",                    ->     DAYS
   *               "h hour",                   ->     HOURS
   *               "min minute",               ->     MINUTES
   *               "s sec second",             ->     SECONDS
   *               "ms milli millisecond",     ->     MILLISECONDS
   *               "µs micro microsecond",     ->     MICROSECONDS
   *               "ns nano nanosecond"        ->     NANOSECONDS
   * @return second
   */
  public static long toSeconds(String length) {
    return Duration.apply(length).toSeconds();
  }
}
