package com.ebay.sojourner.ubd.sojlib.parser;


import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.PropertyUtils;
import com.ebay.sojourner.ubd.sojlib.util.SOJNVL;
import com.ebay.sojourner.ubd.sojlib.util.SOJTS2Date;
import com.ebay.sojourner.ubd.sojlib.util.SOJURLDecodeEscape;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/*
 * Author: yunjzhang
 * Source code: /dw/etl/home/prod/sql/sg_ubi_freq.soj_event_wt1.ins.sql 
 * */
public class TimestampParser implements FieldParser<RawEvent, UbiEvent, Configuration,RuntimeContext> {
    private static final Logger log = Logger.getLogger(TimestampParser.class);
	private static final long UPPERLIMITMICRO = 1 * 60 * 1000000L; // 2 minutes
	private static final long LOWERLIMITMICRO = -30 * 60 * 1000000L; // 31 minutes
	//time zone is GMT-7
	private static final TimeZone timeZone = TimeZone.getTimeZone("GMT-7");
	private static final String P_TAG = "p";
	private static final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
	public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat formaterUtc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		StringBuilder buffer = new StringBuilder();
		Long abEventTimestamp = null;
		Long eventTimestamp = null;
		Long interval = null;
		String applicationPayload = null;
		String mtstsString = null;
		String pageId = null;
		formater.setTimeZone(timeZone);
		formaterUtc.setTimeZone(utcTimeZone);
//		abEventTimestamp = rawEvent.getAbEventTimestamp();
		// for cal2.0 abeventtimestamp format change(from soj timestamp to EPOCH timestamp)
		Long origEventTimeStamp = rawEvent.getRheosHeader().getEventCreateTimestamp();
		if (origEventTimeStamp != null) {
		    abEventTimestamp = SOJTS2Date.getSojTimestamp(origEventTimeStamp);
        }
		Map<String, String> map = new HashMap<>();
		map.putAll(rawEvent.getSojA());
		map.putAll(rawEvent.getSojK());
		map.putAll(rawEvent.getSojC());

		String mARecString = PropertyUtils.mapToString(rawEvent.getSojA());
		String mKRecString = PropertyUtils.mapToString(rawEvent.getSojK());
		String mCRecString = PropertyUtils.mapToString(rawEvent.getSojC());
		if (mARecString != null) {
			applicationPayload = mARecString;
		}
		if ((applicationPayload != null) && (mKRecString != null)) {
			applicationPayload = applicationPayload + "&" + mKRecString;
		}

		// else set C record
		if (applicationPayload == null)
			applicationPayload = mCRecString;

		if (StringUtils.isNotBlank(map.get(P_TAG))) {
			pageId=map.get(P_TAG);
		}

		if (pageId != null && !pageId.equals("5660")) {
//			applicationPayload = rawEvent.getApplicationPayload();
			if (!StringUtils.isBlank(applicationPayload)) {
				// get mtsts from payload
				mtstsString = SOJURLDecodeEscape.decodeEscapes(
						SOJNVL.getTagValue(applicationPayload, "mtsts"), '%');

				// compare ab_event_timestamp and mtsts
				if (!StringUtils.isBlank(mtstsString)
						&& mtstsString.trim().length() >= 21) {
					buffer.append(mtstsString.substring(0, 10)).append(" ")
							.append(mtstsString.substring(11, 19)).append(".")
							.append(mtstsString.substring(20));
					mtstsString = buffer.toString();
					buffer.setLength(0);
					try {
						if(mtstsString.endsWith("Z"))
						{
							eventTimestamp = SOJTS2Date.getSojTimestamp(formaterUtc
									.parse(mtstsString).getTime());
						}
						else {
							eventTimestamp = SOJTS2Date.getSojTimestamp(formater
									.parse(mtstsString).getTime());
						}
						interval = getMicroSecondInterval(eventTimestamp,abEventTimestamp);
						if (interval > UPPERLIMITMICRO || interval < LOWERLIMITMICRO) {
							eventTimestamp = abEventTimestamp;
						}
					} catch (Exception e) {
						log.error("Invalid mtsts: " + mtstsString);
						eventTimestamp = abEventTimestamp;
					}
				} else
					eventTimestamp = abEventTimestamp;
			} else
				eventTimestamp = abEventTimestamp;
		} else
			eventTimestamp = abEventTimestamp;

		if (eventTimestamp != null) {
			ubiEvent.setEventTimestamp(eventTimestamp);
			ubiEvent.setSojDataDt(SOJTS2Date.castSojTimestampToDate(eventTimestamp));
		}
        // Keep original session key from UBI Listener
        ubiEvent.setOldSessionSkey(null);
	}
	
	//ignore second during comparing
	Long getMicroSecondInterval(Long microts1, Long microts2) throws ParseException {
		Long v1,v2;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat formater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formater.setTimeZone(timeZone);
		formater1.setTimeZone(timeZone);
		v1 = formater.parse(formater.format(new Date(microts1/1000))).getTime();
		v2 = formater.parse(formater.format(new Date(microts2/1000))).getTime();
		return (v1 - v2) * 1000;
	}

    @Override
    public void init(Configuration context,RuntimeContext runtimeContext) throws Exception {
    }
}
