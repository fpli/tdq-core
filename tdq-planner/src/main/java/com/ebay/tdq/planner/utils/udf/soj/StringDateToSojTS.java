package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StringDateToSojTS implements Serializable {

  private static DateTimeFormatter dateTimeFormatter =
      DateTimeFormat.forPattern("yyyy-MM-dd")
          .withZone(
              DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT-7")));

  public Long evaluate(String strDate) {

    Date date = dateTimeFormatter.parseDateTime(strDate.substring(0, 10)).toDate();
    long ts = date.getTime();

    return (ts * 1000) + 2208963600000000L;
  }
}
