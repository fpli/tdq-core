package com.ebay.tdq.utils

import scala.concurrent.duration.Duration

/**
 * @author juntzhang
 */
object DateUtils {
  /**
   *
   * @param length
   * "d day",                    ->     DAYS
   * "h hour",                   ->     HOURS
   * "min minute",               ->     MINUTES
   * "s sec second",             ->     SECONDS
   * "ms milli millisecond",     ->     MILLISECONDS
   * "µs micro microsecond",     ->     MICROSECONDS
   * "ns nano nanosecond"        ->     NANOSECONDS
   * @return second
   */
  def toSeconds(length: String): Long = {
    Duration(length).toSeconds
  }
}
