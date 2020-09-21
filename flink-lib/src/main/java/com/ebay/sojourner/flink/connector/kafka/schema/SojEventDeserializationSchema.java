package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.util.TypeTransformUtil;
import com.ebay.sojourner.flink.connector.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class SojEventDeserializationSchema implements DeserializationSchema<SojEvent> {

  @Override
  public SojEvent deserialize(byte[] message) throws IOException {

    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    String guid = TypeTransformUtil.getString(genericRecord.get("guid"));
    String sessionId = TypeTransformUtil.getString(genericRecord.get("sessionId"));
    Long sessionSkey = TypeTransformUtil.getLong(genericRecord.get("sessionSkey"));
    Integer seqNum = TypeTransformUtil.getInteger(genericRecord.get("seqNum"));
    Long sessionStartDt = TypeTransformUtil.getLong(genericRecord.get("sessionStartDt"));
    Long sojDataDt = TypeTransformUtil.getLong(genericRecord.get("sojDataDt"));
    Integer version = TypeTransformUtil.getInteger(genericRecord.get("version"));
    Integer staticPageType = TypeTransformUtil.getInteger(genericRecord.get("staticPageType"));
    Integer reservedForFuture = TypeTransformUtil
        .getInteger(genericRecord.get("reservedForFuture"));
    String eventAttr = TypeTransformUtil.getString(genericRecord.get("eventAttr"));
    Long currentImprId = TypeTransformUtil.getLong(genericRecord.get("currentImprId"));
    Long sourceImprId = TypeTransformUtil.getLong(genericRecord.get("sourceImprId"));
    Long eventTimestamp = TypeTransformUtil.getLong(genericRecord.get("eventTimestamp"));
    Long eventCaptureTime = TypeTransformUtil.getLong(genericRecord.get("eventCaptureTime"));
    String requestCorrelationId = TypeTransformUtil
        .getString(genericRecord.get("requestCorrelationId"));
    String cguid = TypeTransformUtil.getString(genericRecord.get("cguid"));
    String sid = TypeTransformUtil.getString(genericRecord.get("sid"));
    Integer pageId = TypeTransformUtil.getInteger(genericRecord.get("pageId"));
    String pageName = TypeTransformUtil.getString(genericRecord.get("pageName"));
    String pageFamily = TypeTransformUtil.getString(genericRecord.get("pageFamily"));
    String eventFamily = TypeTransformUtil.getString(genericRecord.get("eventFamily"));
    String eventAction = TypeTransformUtil.getString(genericRecord.get("eventAction"));
    String userId = TypeTransformUtil.getString(genericRecord.get("userId"));
    String clickId = TypeTransformUtil.getString(genericRecord.get("clickId"));
    String siteId = TypeTransformUtil.getString(genericRecord.get("siteId"));
    String ciid = TypeTransformUtil.getString(genericRecord.get("ciid"));
    String siid = TypeTransformUtil.getString(genericRecord.get("siid"));
    Long oldSessionSkey = TypeTransformUtil.getLong(genericRecord.get("oldSessionSkey"));
    Boolean partialValidPage = TypeTransformUtil.getBoolean(genericRecord.get("partialValidPage"));
    Long sessionStartTime = TypeTransformUtil.getLong(genericRecord.get("sessionStartTime"));
    Long sessionEndTime = TypeTransformUtil.getLong(genericRecord.get("sessionEndTime"));
    List<Integer> botFlags = (List) genericRecord.get("botFlags");
    Long icfBinary = TypeTransformUtil.getLong(genericRecord.get("icfBinary"));
    Long ingestTime = TypeTransformUtil.getLong(genericRecord.get("ingestTime"));
    Long generateTime = TypeTransformUtil.getLong(genericRecord.get("generateTime"));
    Long eventCnt = TypeTransformUtil.getLong(genericRecord.get("eventCnt"));
    String dataCenter = TypeTransformUtil.getString(genericRecord.get("dataCenter"));
    Integer rdt = TypeTransformUtil.getInteger(genericRecord.get("rdt"));
    Integer regu = TypeTransformUtil.getInteger(genericRecord.get("regu"));
    Boolean iframe = TypeTransformUtil.getBoolean(genericRecord.get("iframe"));
    Long refererHash = TypeTransformUtil.getLong(genericRecord.get("refererHash"));
    String sqr = TypeTransformUtil.getString(genericRecord.get("sqr"));
    String itemId = TypeTransformUtil.getString(genericRecord.get("itemId"));
    String flags = TypeTransformUtil.getString(genericRecord.get("flags"));
    String urlQueryString = TypeTransformUtil.getString(genericRecord.get("urlQueryString"));
    String webServer = TypeTransformUtil.getString(genericRecord.get("webServer"));
    String cookies = TypeTransformUtil.getString(genericRecord.get("cookies"));
    String referrer = TypeTransformUtil.getString(genericRecord.get("referrer"));
    Integer bot = TypeTransformUtil.getInteger(genericRecord.get("bot"));
    String clientIP = TypeTransformUtil.getString(genericRecord.get("clientIP"));
    String remoteIP = TypeTransformUtil.getString(genericRecord.get("remoteIP"));
    String agentInfo = TypeTransformUtil.getString(genericRecord.get("agentInfo"));
    String forwardedFor = TypeTransformUtil.getString(genericRecord.get("forwardedFor"));
    Integer appId = TypeTransformUtil.getInteger(genericRecord.get("appId"));
    String appVersion = TypeTransformUtil.getString(genericRecord.get("appVersion"));
    String osVersion = TypeTransformUtil.getString(genericRecord.get("osVersion"));
    String trafficSource = TypeTransformUtil.getString(genericRecord.get("trafficSource"));
    String cobrand = TypeTransformUtil.getString(genericRecord.get("cobrand"));
    String deviceFamily = TypeTransformUtil.getString(genericRecord.get("deviceFamily"));
    String deviceType = TypeTransformUtil.getString(genericRecord.get("deviceType"));
    String browserVersion = TypeTransformUtil.getString(genericRecord.get("browserVersion"));
    String browserFamily = TypeTransformUtil.getString(genericRecord.get("browserFamily"));
    String osFamily = TypeTransformUtil.getString(genericRecord.get("osFamily"));
    String enrichedOsVersion = TypeTransformUtil.getString(genericRecord.get("enrichedOsVersion"));
    Map<String, String> applicationPayload = (Map) genericRecord.get("applicationPayload");
    String rlogid = TypeTransformUtil.getString(genericRecord.get("rlogid"));
    Map<String, String> clientData = (Map) genericRecord.get("clientData");

    return new SojEvent(guid, sessionId, sessionSkey, seqNum, sessionStartDt, sojDataDt, version,
        staticPageType, reservedForFuture, eventAttr, currentImprId, sourceImprId, eventTimestamp,
        eventCaptureTime, requestCorrelationId, cguid, sid, pageId, pageName, pageFamily,
        eventFamily, eventAction, userId, clickId, siteId, ciid, siid, oldSessionSkey,
        partialValidPage, sessionStartTime, sessionEndTime, botFlags, icfBinary, ingestTime,
        generateTime, eventCnt, dataCenter, rdt, regu, iframe, refererHash, sqr, itemId, flags,
        urlQueryString, webServer, cookies, referrer, bot, clientIP, remoteIP, agentInfo,
        forwardedFor, appId, appVersion, osVersion, trafficSource, cobrand, deviceFamily,
        deviceType, browserVersion, browserFamily, osFamily, enrichedOsVersion, applicationPayload,
        rlogid, clientData);
  }

  @Override
  public boolean isEndOfStream(SojEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<SojEvent> getProducedType() {
    return TypeInformation.of(SojEvent.class);
  }
}
