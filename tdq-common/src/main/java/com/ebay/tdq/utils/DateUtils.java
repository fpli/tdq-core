package com.ebay.tdq.utils;

import java.util.TimeZone;
import org.apache.commons.lang3.time.FastDateFormat;
import scala.concurrent.duration.Duration;

/**
 * @author juntzhang
 */
public class DateUtils {
  public static String calculateIndexDate(long ts) {
    return FastDateFormat.getInstance("yyyy.MM.dd", TimeZone.getTimeZone("MST")).format(ts);
  }

  public static String getMinBuckets(long ts, int minute) {
    return FastDateFormat.getInstance("yyyyMMddHHmm",
        TimeZone.getTimeZone("CTT")).format(ts / 60_000 / minute * 60_000 * minute);
  }

  /**
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
