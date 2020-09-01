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


  @Override
  public void init() throws Exception {
  }
}
