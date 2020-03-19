package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJURLDecodeEscape;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/*
 * Author: yunjzhang
 * Source code: /dw/etl/home/prod/sql/sg_ubi_freq.soj_event_wt1.ins.sql
 * */
public class TimestampParser implements FieldParser<RawEvent, UbiEvent> {

  private static final Logger log = Logger.getLogger(TimestampParser.class);
  private static final long UPPERLIMITMICRO = 1 * 60 * 1000000L; // 2 minutes
  private static final long LOWERLIMITMICRO = -30 * 60 * 1000000L; // 31 minutes
  // time zone is GMT-7
  private static final TimeZone timeZone = TimeZone.getTimeZone("GMT-7");
  private static final String P_TAG = "p";
  private static final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");

  public static void main(String[] args) {
    System.out.println(SOJTS2Date.getSojTimestamp(1584039617189L));

  }

  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    SimpleDateFormat formaterUtc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    StringBuilder buffer = new StringBuilder();
    Long abEventTimestamp = null;
    Long eventTimestamp = null;
    Long interval = null;
    String applicationPayload = ubiEvent.getApplicationPayload();
    String mtstsString = null;
    String pageId = null;
    formater.setTimeZone(timeZone);
    formaterUtc.setTimeZone(utcTimeZone);
    // abEventTimestamp = rawEvent.getAbEventTimestamp();
    // for cal2.0 abeventtimestamp format change(from soj timestamp to EPOCH timestamp)
    String tstamp = rawEvent.getClientData().getTStamp();
    if (tstamp != null) {
      try {
        abEventTimestamp = Long.valueOf(rawEvent.getClientData().getTStamp());
        abEventTimestamp = SOJTS2Date.getSojTimestamp(abEventTimestamp);
      } catch (NumberFormatException e) {
        Long origEventTimeStamp = rawEvent.getRheosHeader().getEventCreateTimestamp();
        if (origEventTimeStamp != null) {
          abEventTimestamp = SOJTS2Date.getSojTimestamp(origEventTimeStamp);
        }
      }
    } else {
      Long origEventTimeStamp = rawEvent.getRheosHeader().getEventCreateTimestamp();
      if (origEventTimeStamp != null) {
        abEventTimestamp = SOJTS2Date.getSojTimestamp(origEventTimeStamp);
      }
    }
    Map<String, String> map = new HashMap<>();
    map.putAll(rawEvent.getSojA());
    map.putAll(rawEvent.getSojK());
    map.putAll(rawEvent.getSojC());

    if (StringUtils.isNotBlank(map.get(P_TAG))) {
      pageId = map.get(P_TAG);
    }

    if (pageId != null && !pageId.equals("5660")) {
      if (!StringUtils.isBlank(applicationPayload)) {
        // get mtsts from payload
        mtstsString =
            SOJURLDecodeEscape.decodeEscapes(SOJNVL.getTagValue(applicationPayload, "mtsts"), '%');

        // compare ab_event_timestamp and mtsts
        if (!StringUtils.isBlank(mtstsString) && mtstsString.trim().length() >= 21) {
          buffer
              .append(mtstsString, 0, 10)
              .append(" ")
              .append(mtstsString, 11, 19)
              .append(".")
              .append(mtstsString.substring(20));
          mtstsString = buffer.toString();
          buffer.setLength(0);
          try {
            if (mtstsString.endsWith("Z")) {
              eventTimestamp = SOJTS2Date.getSojTimestamp(formaterUtc.parse(mtstsString).getTime());
            } else {
              eventTimestamp = SOJTS2Date.getSojTimestamp(formater.parse(mtstsString).getTime());
            }
            interval = getMicroSecondInterval(eventTimestamp, abEventTimestamp);
            if (interval > UPPERLIMITMICRO || interval < LOWERLIMITMICRO) {
              eventTimestamp = abEventTimestamp;
            }
          } catch (Exception e) {
            log.error("Invalid mtsts: " + mtstsString);
            eventTimestamp = abEventTimestamp;
          }
        } else {
          eventTimestamp = abEventTimestamp;
        }
      } else {
        eventTimestamp = abEventTimestamp;
      }
    } else {
      eventTimestamp = abEventTimestamp;
    }

    if (eventTimestamp != null) {
      ubiEvent.setEventTimestamp(eventTimestamp);
      ubiEvent.setSojDataDt(SOJTS2Date.castSojTimestampToDate(eventTimestamp));
    }

    // Keep original session key from UBI Listener
    ubiEvent.setIngestTime(rawEvent.getIngestTime());
    ubiEvent.setGenerateTime(rawEvent.getRheosHeader().getEventCreateTimestamp());
    ubiEvent.setOldSessionSkey(null);
    ubiEvent.setDataCenter(rawEvent.getDataCenter());
  }

  // ignore second during comparing
  Long getMicroSecondInterval(Long microts1, Long microts2) throws ParseException {
    Long v1, v2;
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat formater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    formater.setTimeZone(timeZone);
    formater1.setTimeZone(timeZone);
    v1 = formater.parse(formater.format(new Date(microts1 / 1000))).getTime();
    v2 = formater.parse(formater.format(new Date(microts2 / 1000))).getTime();
    return (v1 - v2) * 1000;
  }

  @Override
  public void init() throws Exception {
  }
}
