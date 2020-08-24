package com.ebay.sojourner.flink.connectors.kafka.schema;

import com.ebay.sojourner.common.model.MiscEvent;
import com.ebay.sojourner.flink.connectors.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class MiscEventDeserializationSchema implements DeserializationSchema<MiscEvent> {

  @Override
  public MiscEvent deserialize(byte[] message) throws IOException {

    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    long eventCreateTimestamp = rheosEvent.getEventCreateTimestamp();
    String guid = getString(genericRecord.get("guid"));
    Long eventTimestamp = getLong(genericRecord.get("eventTimestamp"));
    Long eventCaptureTime = getLong(genericRecord.get("eventCaptureTime"));
    String requestCorrelationId = getString(genericRecord.get("requestCorrelationId"));
    String cguid = getString(genericRecord.get("cguid"));
    String sid = getString(genericRecord.get("sid"));
    Integer pageId = getInteger(genericRecord.get("pageId"));
    String pageName = getString(genericRecord.get("pageName"));
    String pageFamily = getString(genericRecord.get("pageFamily"));
    String eventFamily = getString(genericRecord.get("eventFamily"));
    String eventAction = getString(genericRecord.get("eventAction"));
    String userId = getString(genericRecord.get("userId"));
    String clickId = getString(genericRecord.get("clickId"));
    String siteId = getString(genericRecord.get("siteId"));
    String sessionId = getString(genericRecord.get("sessionId"));
    String seqNum = getString(genericRecord.get("seqNum"));
    String ciid = getString(genericRecord.get("ciid"));
    String siid = getString(genericRecord.get("siid"));
    Integer rdt = getInteger(genericRecord.get("rdt"));
    Integer regu = getInteger(genericRecord.get("regu"));
    Boolean iframe = getBoolean(genericRecord.get("iframe"));
    String refererHash = getString(genericRecord.get("refererHash"));
    String sqr = getString(genericRecord.get("sqr"));
    String itemId = getString(genericRecord.get("itemId"));
    String flags = getString(genericRecord.get("flags"));
    String urlQueryString = getString(genericRecord.get("urlQueryString"));
    String webServer = getString(genericRecord.get("webServer"));
    String cookies = getString(genericRecord.get("cookies"));
    Integer bot = getInteger(genericRecord.get("bot"));
    String clientIP = getString(genericRecord.get("clientIP"));
    String remoteIP = getString(genericRecord.get("remoteIP"));
    String agentInfo = getString(genericRecord.get("agentInfo"));
    String appId = getString(genericRecord.get("appId"));
    String appVersion = getString(genericRecord.get("appVersion"));
    String osVersion = getString(genericRecord.get("osVersion"));
    String trafficSource = getString(genericRecord.get("trafficSource"));
    String cobrand = getString(genericRecord.get("cobrand"));
    String deviceFamily = getString(genericRecord.get("deviceFamily"));
    String deviceType = getString(genericRecord.get("deviceType"));
    String browserVersion = getString(genericRecord.get("browserVersion"));
    String browserFamily = getString(genericRecord.get("browserFamily"));
    String osFamily = getString(genericRecord.get("osFamily"));
    String enrichedOsVersion = getString(genericRecord.get("enrichedOsVersion"));
    String applicationPayload = getString(genericRecord.get("applicationPayload"));
    String rlogid = getString(genericRecord.get("rlogid"));
    String clientData = getString(genericRecord.get("clientData"));

    return new MiscEvent(eventCreateTimestamp, guid, eventTimestamp, eventCaptureTime,
        requestCorrelationId, cguid, sid, pageId, pageName, pageFamily, eventFamily, eventAction,
        userId, clickId, siteId, sessionId, seqNum, ciid, siid, rdt, regu, iframe, refererHash, sqr,
        itemId, flags, urlQueryString, webServer, cookies, bot, clientIP, remoteIP, agentInfo,
        appId, appVersion, osVersion, trafficSource, cobrand, deviceFamily, deviceType,
        browserVersion, browserFamily, osFamily, enrichedOsVersion, applicationPayload, rlogid,
        clientData);
  }

  @Override
  public boolean isEndOfStream(MiscEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<MiscEvent> getProducedType() {
    return TypeInformation.of(MiscEvent.class);
  }

  private Integer getInteger(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return null;
    } else {
      return Integer.valueOf(getString(o));
    }
  }

  private boolean getBoolean(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return false;
    } else {
      return Boolean.valueOf(getString(o));
    }
  }

  private Long getLong(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return null;
    } else {
      return Long.valueOf(getString(o));
    }
  }

  private String getString(Object o) {
    return (o != null && !"null".equals(o.toString())) ? o.toString() : null;
  }
}
