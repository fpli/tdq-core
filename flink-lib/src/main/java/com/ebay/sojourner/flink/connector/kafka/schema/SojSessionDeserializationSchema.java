package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.util.TypeTransformUtil;
import com.ebay.sojourner.flink.connector.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

@Slf4j
public class SojSessionDeserializationSchema implements DeserializationSchema<SojSession> {

  @Override
  public SojSession deserialize(byte[] message) throws IOException {

    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    log.error("======debug kafka topic record sojSession=======");
    log.error(genericRecord.toString());
    String guid = TypeTransformUtil.getString(genericRecord.get("guid"));
    String sessionReferrer = TypeTransformUtil.getString(genericRecord.get("sessionReferrer"));
    Long siteFlags = TypeTransformUtil.getLong(genericRecord.get("siteFlags"));
    Integer attrFlags = TypeTransformUtil.getInteger(genericRecord.get("attrFlags"));
    Integer version = TypeTransformUtil.getInteger(genericRecord.get("version"));
    Integer botFlags = TypeTransformUtil.getInteger(genericRecord.get("botFlags"));
    String ip = TypeTransformUtil.getString(genericRecord.get("ip"));
    String userAgent = TypeTransformUtil.getString(genericRecord.get("userAgent"));
    Long findingFlags = TypeTransformUtil.getLong(genericRecord.get("findingFlags"));
    Integer startPageId = TypeTransformUtil.getInteger(genericRecord.get("startPageId"));
    Integer endPageId = TypeTransformUtil.getInteger(genericRecord.get("endPageId"));
    Integer botFlag = TypeTransformUtil.getInteger(genericRecord.get("botFlag"));
    Integer durationSec = TypeTransformUtil.getInteger(genericRecord.get("durationSec"));
    String userId = TypeTransformUtil.getString(genericRecord.get("userId"));
    Integer eventCnt = TypeTransformUtil.getInteger(genericRecord.get("eventCnt"));
    Integer trafficSrcId = TypeTransformUtil.getInteger(genericRecord.get("trafficSrcId"));
    Integer viCnt = TypeTransformUtil.getInteger(genericRecord.get("viCnt"));
    Integer bidCnt = TypeTransformUtil.getInteger(genericRecord.get("bidCnt"));
    Integer binCnt = TypeTransformUtil.getInteger(genericRecord.get("binCnt"));
    Integer watchCnt = TypeTransformUtil.getInteger(genericRecord.get("watchCnt"));
    Integer homepageCnt = TypeTransformUtil.getInteger(genericRecord.get("homepageCnt"));
    String siteId = TypeTransformUtil.getString(genericRecord.get("siteId"));
    Integer firstSiteId = TypeTransformUtil.getInteger(genericRecord.get("firstSiteId"));
    String cguid = TypeTransformUtil.getString(genericRecord.get("cguid"));
    Integer cobrand = TypeTransformUtil.getInteger(genericRecord.get("cobrand"));
    Long startTimestamp = TypeTransformUtil.getLong(genericRecord.get("startTimestamp"));
    Long firstMappedUserId = TypeTransformUtil.getLong(genericRecord.get("firstMappedUserId"));
    Integer appId = TypeTransformUtil.getInteger(genericRecord.get("appId"));
    Long absDuration = TypeTransformUtil.getLong(genericRecord.get("absDuration"));
    Integer grCnt = TypeTransformUtil.getInteger(genericRecord.get("grCnt"));
    Integer gr1Cnt = TypeTransformUtil.getInteger(genericRecord.get("gr1Cnt"));
    Integer myebayCnt = TypeTransformUtil.getInteger(genericRecord.get("myebayCnt"));
    Long absStartTimestamp = TypeTransformUtil.getLong(genericRecord.get("absStartTimestamp"));
    Integer signinPageCnt = TypeTransformUtil.getInteger(genericRecord.get("signinPageCnt"));
    Integer nonIframeRdtEventCnt = TypeTransformUtil
        .getInteger(genericRecord.get("nonIframeRdtEventCnt"));
    List<Integer> botFlagList = (List) genericRecord.get("botFlagList");
    Long absEndTimestamp = TypeTransformUtil.getLong(genericRecord.get("absEndTimestamp"));
    Long endTimestamp = TypeTransformUtil.getLong(genericRecord.get("endTimestamp"));
    Long sojDataDt = TypeTransformUtil.getLong(genericRecord.get("sojDataDt"));
    String sessionId = TypeTransformUtil.getString(genericRecord.get("sessionId"));
    Long sessionSkey = TypeTransformUtil.getLong(genericRecord.get("sessionSkey"));
    Long sessionStartDt = TypeTransformUtil.getLong(genericRecord.get("sessionStartDt"));
    Long firstSessionStartDt = TypeTransformUtil.getLong(genericRecord.get("firstSessionStartDt"));
    Long sessionEndDt = TypeTransformUtil.getLong(genericRecord.get("sessionEndDt"));
    Integer absEventCnt = TypeTransformUtil.getInteger(genericRecord.get("absEventCnt"));
    Boolean singleClickSessionFlag = TypeTransformUtil
        .getBoolean(genericRecord.get("singleClickSessionFlag"));
    Integer asqCnt = TypeTransformUtil.getInteger(genericRecord.get("asqCnt"));
    Integer atcCnt = TypeTransformUtil.getInteger(genericRecord.get("atcCnt"));
    Integer atlCnt = TypeTransformUtil.getInteger(genericRecord.get("atlCnt"));
    Integer boCnt = TypeTransformUtil.getInteger(genericRecord.get("boCnt"));
    Integer srpCnt = TypeTransformUtil.getInteger(genericRecord.get("srpCnt"));
    Integer servEventCnt = TypeTransformUtil.getInteger(genericRecord.get("servEventCnt"));
    Integer searchViewPageCnt = TypeTransformUtil
        .getInteger(genericRecord.get("searchViewPageCnt"));
    String browserFamily = TypeTransformUtil.getString(genericRecord.get("browserFamily"));
    String browserVersion = TypeTransformUtil.getString(genericRecord.get("browserVersion"));
    String city = TypeTransformUtil.getString(genericRecord.get("city"));
    String region = TypeTransformUtil.getString(genericRecord.get("region"));
    String country = TypeTransformUtil.getString(genericRecord.get("country"));
    String continent = TypeTransformUtil.getString(genericRecord.get("continent"));
    String deviceClass = TypeTransformUtil.getString(genericRecord.get("deviceClass"));
    String deviceFamily = TypeTransformUtil.getString(genericRecord.get("deviceFamily"));
    Integer endResourceId = TypeTransformUtil.getInteger(genericRecord.get("endResourceId"));
    Integer startResourceId = TypeTransformUtil.getInteger(genericRecord.get("startResourceId"));
    Boolean isReturningVisitor = TypeTransformUtil
        .getBoolean(genericRecord.get("isReturningVisitor"));
    String lineSpeed = TypeTransformUtil.getString(genericRecord.get("lineSpeed"));
    String osFamily = TypeTransformUtil.getString(genericRecord.get("osFamily"));
    String osVersion = TypeTransformUtil.getString(genericRecord.get("osVersion"));
    Integer pulsarEventCnt = TypeTransformUtil.getInteger(genericRecord.get("pulsarEventCnt"));
    Integer sojEventCnt = TypeTransformUtil.getInteger(genericRecord.get("sojEventCnt"));
    String streamId = TypeTransformUtil.getString(genericRecord.get("streamId"));
    Integer viewEventCnt = TypeTransformUtil.getInteger(genericRecord.get("viewEventCnt"));
    String referer = TypeTransformUtil.getString(genericRecord.get("referer"));
    Integer pageId = TypeTransformUtil.getInteger(genericRecord.get("pageId"));
    String buserId = TypeTransformUtil.getString(genericRecord.get("buserId"));
    Long oldsessionskey = TypeTransformUtil.getLong(genericRecord.get("oldsessionskey"));
    Boolean isOpen = TypeTransformUtil.getBoolean(genericRecord.get("isOpen"));


    return new SojSession(guid, sessionReferrer, siteFlags, attrFlags, version, botFlags, ip,
        userAgent, findingFlags, startPageId, endPageId, botFlag, durationSec, userId, eventCnt,
        trafficSrcId, viCnt, bidCnt, binCnt, watchCnt, homepageCnt, siteId,firstSiteId,cguid,
        cobrand,
        startTimestamp, firstMappedUserId, appId, absDuration, grCnt, gr1Cnt, myebayCnt,
        absStartTimestamp, signinPageCnt, nonIframeRdtEventCnt, botFlagList, absEndTimestamp,
        endTimestamp, sojDataDt, sessionId, sessionSkey, sessionStartDt, firstSessionStartDt,
        sessionEndDt, absEventCnt, singleClickSessionFlag, asqCnt, atcCnt, atlCnt, boCnt, srpCnt,
        servEventCnt, searchViewPageCnt, browserFamily, browserVersion, city, region, country,
        continent, deviceClass, deviceFamily, endResourceId, startResourceId, isReturningVisitor,
        lineSpeed, osFamily, osVersion, pulsarEventCnt, sojEventCnt, streamId, viewEventCnt,
        referer, pageId, buserId, oldsessionskey, isOpen);
  }

  @Override
  public boolean isEndOfStream(SojSession nextElement) {
    return false;
  }

  @Override
  public TypeInformation<SojSession> getProducedType() {
    return TypeInformation.of(SojSession.class);
  }
}
