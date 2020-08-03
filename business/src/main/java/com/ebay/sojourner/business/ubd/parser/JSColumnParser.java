package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.RaptorUAParser;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class JSColumnParser implements FieldParser<RawEvent, UbiEvent> {

  public static final String DEVICE_TAG = "dn";
  private static final String DEVICE_FAMILY = "dd_d";
  private static final String DEVICE_TYPE = "dd_dc";
  private static final String BROWSER_VERSION = "dd_bv";
  private static final String BROWSER_FAMILY = "dd_bf";
  private static final String OS_FAMILY = "dd_os";
  private static final String ENRICHED_OS_VERSION = "dd_osv";
  private static final String OS_VERSION = "osv";
  private static final String TRAFFIC_SOURCE = "trffc_src";
  private static final String APP_VERSION = "mav";
  private static final String EVENT_ACTION = "eactn";
  private static final String EVENT_FAMILY = "efam";
  private static final String SID = "sid";
  private static final String RQ = "rq";
  private static final String CITY = "cty";
  private static final String REGION = "rgn";
  private static final String COUNTRY = "cn";
  private static final String CONTINENT = "con";
  private static final String LINE_SPEED = "ls";
  private static final String RETURNING_VISITOR = "rv";
  private static final String STREAM_ID = "rv";
  private static final String BUSER_ID = "si";

  @Override
  public void init() throws Exception {

  }

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    Map<String, String> map = new HashMap<>();
    map.putAll(rawEvent.getSojA());
    map.putAll(rawEvent.getSojK());

    String dn = (map.get(DEVICE_TAG) == null ? null : map.get(DEVICE_TAG).toString());
    Map<String, String> result = RaptorUAParser.getInstance()
        .processUA(ubiEvent.getAgentInfo(), dn, false);
    String deviceFamily = result.get(DEVICE_FAMILY);
    String deviceType = result.get(DEVICE_TYPE);
    String browserVersion = result.get(BROWSER_VERSION);
    String browserFamily = result.get(BROWSER_FAMILY);
    String osFamily = result.get(OS_FAMILY);
    String enrichedOsVersion = result.get(ENRICHED_OS_VERSION);
    String osVersion = map.get(OS_VERSION);
    String trafficSource = map.get(TRAFFIC_SOURCE);
    String appVersion = map.get(APP_VERSION);
    String eventAction = map.get(EVENT_ACTION);
    String eventFamily = map.get(EVENT_FAMILY);
    String sid = map.get(SID);
    String rq = map.get(RQ);
    String city = map.get(CITY);
    String region = map.get(REGION);
    String country = map.get(COUNTRY);
    String continent = map.get(CONTINENT);
    String lineSpeed = map.get(LINE_SPEED);
    boolean returningVisitor = Boolean.parseBoolean(map.get(RETURNING_VISITOR));
    String streamId = map.get(STREAM_ID);
    String buserId = map.get(BUSER_ID);
    if (StringUtils.isNotEmpty(deviceFamily)) {
      ubiEvent.setDeviceFamily(deviceFamily);
    }
    if (StringUtils.isNotEmpty(deviceType)) {
      ubiEvent.setDeviceType(deviceType);
    }
    if (StringUtils.isNotEmpty(browserFamily)) {
      ubiEvent.setBrowserFamily(browserFamily);
    }
    if (StringUtils.isNotEmpty(browserVersion)) {
      ubiEvent.setBrowserVersion(browserVersion);
    }
    if (StringUtils.isNotEmpty(osFamily)) {
      ubiEvent.setOsFamily(osFamily);
    }
    if (StringUtils.isNotEmpty(enrichedOsVersion)) {
      ubiEvent.setEnrichedOsVersion(enrichedOsVersion);
    }
    if (StringUtils.isNotEmpty(osVersion)) {
      ubiEvent.setOsVersion(osVersion);
    }
    if (StringUtils.isNotEmpty(trafficSource)) {
      ubiEvent.setTrafficSource(trafficSource);
    }
    if (StringUtils.isNotEmpty(appVersion)) {
      ubiEvent.setAppVersion(appVersion);
    }
    if (StringUtils.isNotEmpty(eventAction)) {
      ubiEvent.setEventAction(eventAction);
    }
    if (StringUtils.isNotEmpty(eventFamily)) {
      ubiEvent.setEventFamily(eventFamily);
    }
    if (StringUtils.isNotEmpty(sid)) {
      ubiEvent.setSid(sid);
    }
    if (StringUtils.isNotEmpty(rq)) {
      ubiEvent.setRequestCorrelationId(rq);
    }
    if (StringUtils.isNotEmpty(city)) {
      ubiEvent.setCity(city);
    }
    if (StringUtils.isNotEmpty(region)) {
      ubiEvent.setRegion(region);
    }
    if (StringUtils.isNotEmpty(country)) {
      ubiEvent.setCountry(country);
    }
    if (StringUtils.isNotEmpty(continent)) {
      ubiEvent.setContinent(continent);
    }
    if (StringUtils.isNotEmpty(lineSpeed)) {
      ubiEvent.setLineSpeed(lineSpeed);
    }
    if (StringUtils.isNotEmpty(buserId)) {
      ubiEvent.setBuserId(buserId);
    }
    if (StringUtils.isNotEmpty(streamId)) {
      ubiEvent.setStreamId(streamId);
    }
    ubiEvent.setIsReturningVisitor(returningVisitor);
    ubiEvent.setRlogid(rawEvent.getClientData().getRlogid());
    ubiEvent.setRemoteIP(rawEvent.getClientData().getRemoteIP());
  }
}
