package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class GUID2DateHive implements Serializable {

  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
//  private static SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

  //  private static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {
//    @Override
//    protected SimpleDateFormat initialValue() {
//      SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
//      // Make consistent with getCalender()
//      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//      return dateFormat;
//    }
//  };
  private DateTimeFormatter formatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT).withZone(
      DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT")));

  public String evaluate(String guid) {

    if (guid == null) {
      //  return new Text(formatter.format(GUID2Date.getDate(null)));
      return null;
    }

    Date newdate = null;
    try {
      newdate = GUID2Date.getDate(guid);
    } catch (Exception e) {
    }
    //      return new Text(simpleDateFormatThreadLocal.get().format(newdate));
    if (newdate != null) {
      return formatter.print(newdate.getTime());
    } else {
      return null;
    }
  }
}
