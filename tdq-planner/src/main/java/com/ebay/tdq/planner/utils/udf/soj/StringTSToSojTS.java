package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StringTSToSojTS implements Serializable {

  private static final DateTimeFormatter timestampFormatter =
      DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS")
          .withZone(
              DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT-7")));

  public Long evaluate(String strTS) {

    Date date = timestampFormatter.parseDateTime(strTS.substring(0, 23)).toDate();
    long ts = date.getTime();

    return (ts * 1000) + 2208963600000000L;
  }
}
