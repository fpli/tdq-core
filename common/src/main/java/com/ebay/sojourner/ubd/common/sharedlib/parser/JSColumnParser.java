package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class JSColumnParser implements FieldParser<RawEvent, UbiEvent> {

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

  @Override
  public void init() throws Exception {

  }

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {
    Map<String, String> map = new HashMap<>();
    map.putAll(rawEvent.getSojA());
    map.putAll(rawEvent.getSojK());
    map.putAll(rawEvent.getSojC());
    String deviceFamily = map.get(DEVICE_FAMILY);
    String deviceType = map.get(DEVICE_TYPE);
    String browserVersion = map.get(BROWSER_VERSION);
    String browserFamily = map.get(BROWSER_FAMILY);
    String osFamily = map.get(OS_FAMILY);
    String enrichedOsVersion = map.get(ENRICHED_OS_VERSION);
    String osVersion = map.get(OS_VERSION);
    String trafficSource = map.get(TRAFFIC_SOURCE);
    String appVersion = map.get(APP_VERSION);
    String eventAction = map.get(EVENT_ACTION);
    String eventFamily = map.get(EVENT_FAMILY);
    String sid = map.get(SID);
    String rq = map.get(RQ);
    if (StringUtils.isEmpty(deviceFamily)) {
      ubiEvent.setDeviceFamily(deviceFamily);
    }
    if (StringUtils.isEmpty(deviceType)) {
      ubiEvent.setDeviceType(deviceType);
    }
    if (StringUtils.isEmpty(browserFamily)) {
      ubiEvent.setBrowserFamily(browserFamily);
    }
    if (StringUtils.isEmpty(browserVersion)) {
      ubiEvent.setBrowserVersion(browserVersion);
    }
    if (StringUtils.isEmpty(osFamily)) {
      ubiEvent.setOsFamily(osFamily);
    }
    if (StringUtils.isEmpty(enrichedOsVersion)) {
      ubiEvent.setEnrichedOsVersion(enrichedOsVersion);
    }
    if (StringUtils.isEmpty(osVersion)) {
      ubiEvent.setOsVersion(osVersion);
    }
    if (StringUtils.isEmpty(trafficSource)) {
      ubiEvent.setTrafficSource(trafficSource);
    }
    if (StringUtils.isEmpty(appVersion)) {
      ubiEvent.setAppVersion(appVersion);
    }
    if (StringUtils.isEmpty(eventAction)) {
      ubiEvent.setEventAction(eventAction);
    }

    if (StringUtils.isEmpty(eventFamily)) {
      ubiEvent.setEventFamily(eventFamily);
    }
    if (StringUtils.isEmpty(sid)) {
      ubiEvent.setSid(sid);
    }
    if (StringUtils.isEmpty(rq)) {
      ubiEvent.setRequestCorrelationId(rq);
    }
    ubiEvent.setRlogid(rawEvent.getClientData().getRlogid());
    ubiEvent.setRemoteIP(rawEvent.getClientData().getRemoteIP());
  }
}
