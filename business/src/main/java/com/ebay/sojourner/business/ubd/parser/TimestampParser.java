package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/*
 * Source code: /dw/etl/home/prod/sql/sg_ubi_freq.soj_event_wt1.ins.sql
 * */
public class TimestampParser implements FieldParser<RawEvent, UbiEvent> {

  private static final Logger log = Logger.getLogger(TimestampParser.class);
<<<<<<< HEAD
  private static final long UPPERLIMITMICRO = 1 * 60 * 1000000L; // 2 minutes
  private static final long LOWERLIMITMICRO = -30 * 60 * 1000000L; // 31 minutes
  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  // time zone is GMT-7
  private static final TimeZone timeZone = TimeZone.getTimeZone("GMT-7");
  private static final String P_TAG = "p";
  private static final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
  private static DateTimeFormatter formater =
      DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT).withZone(
          DateTimeZone.forTimeZone(timeZone));
  private static DateTimeFormatter formaterUtc =
      DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT).withZone(
      DateTimeZone.forTimeZone(utcTimeZone));

  public static void main(String[] args) throws ParseException {
    DateTimeFormatter formaterUtcTest =
        DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT).withZone(
            DateTimeZone.forTimeZone(utcTimeZone));
    String mtstsString = "2020-08-31 13:28:48.586";
    //    mtstsString = mtstsString.replaceAll("T", " ")
    //        .replaceAll("Z", "");
    System.out.println(mtstsString);
    System.out.println(
        SOJTS2Date.getSojTimestamp(formaterUtc.parseDateTime(mtstsString).getMillis()));
    System.out.println(SOJTS2Date.getSojTimestamp(formater.parseDateTime(mtstsString).getMillis()));
    System.out.println(getMicroSecondInterval(
       Long.valueOf(SOJTS2Date.getSojTimestamp(formater.parseDateTime(mtstsString).getMillis())),
        Long.valueOf(SOJTS2Date.getSojTimestamp(formater.parseDateTime(mtstsString).getMillis()))));
=======
  private static final DateTimeFormatter dateMinsFormatter = DateTimeFormat.forPattern(
      Constants.DEFAULT_DATE_MINS_FORMAT)
      .withZone(
          DateTimeZone.forTimeZone(Constants.PST_TIMEZONE));

  public static void main(String[] args) throws ParseException {
    DateTimeFormatter formaterUtcTest =
        DateTimeFormat.forPattern(Constants.DEFAULT_TIMESTAMP_FORMAT).withZone(
            DateTimeZone.forTimeZone(Constants.UTC_TIMEZONE));
    String mtstsString = "2020-08-13T09:45:47.079Z";
    mtstsString = mtstsString.replaceAll("T", " ")
        .replaceAll("Z", "");
    System.out.println(mtstsString);
    System.out.println(
        SojTimestamp.getSojTimestamp(formaterUtcTest.parseDateTime(mtstsString).getMillis()));
>>>>>>> 3e00cfad... centralize constants and fix timestamp format issue
  }

  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
    Long eventTimestamp = rawEvent.getEventTimestamp();
    if (eventTimestamp != null) {
      ubiEvent.setEventTimestamp(eventTimestamp);
      ubiEvent.setSojDataDt(SojTimestamp.castSojTimestampToDate(eventTimestamp));
    }

    // Keep original session key from UBI Listener
    ubiEvent.setIngestTime(rawEvent.getIngestTime());
    ubiEvent.setGenerateTime(rawEvent.getRheosHeader().getEventCreateTimestamp());
    ubiEvent.setOldSessionSkey(null);
  }

<<<<<<< HEAD
  // ignore second during comparing
  private static Long getMicroSecondInterval(Long microts1, Long microts2) throws ParseException {
    Long v1, v2;
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat formater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    formater.setTimeZone(timeZone);
    formater1.setTimeZone(timeZone);
    v1 = formater.parse(formater.format(new Date(microts1 / 1000))).getTime();
    v2 = formater.parse(formater.format(new Date(microts2 / 1000))).getTime();
    return (v1 - v2) * 1000;
  }
=======
>>>>>>> 3e00cfad... centralize constants and fix timestamp format issue

  @Override
  public void init() throws Exception {
  }
}
