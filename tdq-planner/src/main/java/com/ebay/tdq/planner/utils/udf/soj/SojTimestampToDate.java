package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class SojTimestampToDate implements Serializable {

  private static final long OFFSET = ((long) 25567) * 24 * 3600 * 1000;
  private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
  private static final String UTC = "UTC";
  private final DateTimeFormatter formatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT).withZone(
      DateTimeZone.forTimeZone(TimeZone.getTimeZone(UTC)));

  public String evaluate(final Long sojTimestamp) {
    if (sojTimestamp == null || sojTimestamp == 0L) {
      return null;
    }

    long timestamp = (sojTimestamp / 1000);
    Date date = new Date(timestamp - OFFSET);

    return (formatter.print(date.getTime()));
  }
}