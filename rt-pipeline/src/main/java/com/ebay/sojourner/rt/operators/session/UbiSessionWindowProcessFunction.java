package com.ebay.sojourner.rt.operators.session;

import com.ebay.sojourner.business.ubd.detectors.SessionEndBotDetector;
import com.ebay.sojourner.business.ubd.metrics.SessionMetrics;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.api.common.typeutils.base.StringSerializer;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

@Slf4j
public class UbiSessionWindowProcessFunction
    extends ProcessWindowFunction<SessionAccumulator, UbiSession, Tuple, TimeWindow> {

  private static final MapStateDescriptor<String, Long> lastTimestampStateDescriptor =
      new MapStateDescriptor<String, Long>("lastTimestamp",
          StringSerializer.INSTANCE, LongSerializer.INSTANCE);

  private OutputTag outputTag = null;
  private SessionEndBotDetector sessionEndBotDetector;

  public UbiSessionWindowProcessFunction() {

  }

  public UbiSessionWindowProcessFunction(OutputTag outputTag) {
    this.outputTag = outputTag;
  }

  private static void outputSession(UbiSession ubiSessionTmp,
      Collector<UbiSession> out, boolean isOpen, String type) {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setGuid(ubiSessionTmp.getGuid());
    ubiSession.setAgentString(ubiSessionTmp.getAgentString());
    ubiSession.setSessionId(ubiSessionTmp.getSessionId());
    ubiSession.setSessionSkey(ubiSessionTmp.getSessionSkey());
    ubiSession.setIp(ubiSessionTmp.getIp());
    ubiSession.setUserAgent(ubiSessionTmp.getUserAgent());
    ubiSession.setExInternalIp(ubiSessionTmp.getExInternalIp());
    ubiSession.setSojDataDt(ubiSessionTmp.getSojDataDt());
    ubiSession.setSessionStartDt(ubiSessionTmp.getSessionStartDt());
    ubiSession.setAgentCnt(ubiSessionTmp.getAgentCnt());
    ubiSession.setStartTimestamp(ubiSessionTmp.getStartTimestamp());
    ubiSession.setEndTimestamp(ubiSessionTmp.getEndTimestamp());
    ubiSession.setAbsStartTimestamp(ubiSessionTmp.getAbsStartTimestamp());
    ubiSession.setAbsEndTimestamp(ubiSessionTmp.getAbsEndTimestamp());
    ubiSession.setClientIp(ubiSessionTmp.getClientIp());
    ubiSession.setInternalIp(ubiSessionTmp.getInternalIp());
    ubiSession.setSingleClickSessionFlag(
        ubiSessionTmp.getSingleClickSessionFlag());
    ubiSession.setBotFlagList(ubiSessionTmp.getBotFlagList());
    ubiSession.setNonIframeRdtEventCnt(
        ubiSessionTmp.getNonIframeRdtEventCnt());
    ubiSession.setSessionReferrer(ubiSessionTmp.getSessionReferrer());
    ubiSession.setBotFlag(ubiSessionTmp.getBotFlag());
    ubiSession.setVersion(ubiSessionTmp.getVersion());
    ubiSession.setFirstUserId(ubiSessionTmp.getFirstUserId());
    ubiSession.setSiteFlags(ubiSessionTmp.getSiteFlags());
    ubiSession.setAttrFlags(ubiSessionTmp.getAttrFlags());
    ubiSession.setBotFlags(ubiSessionTmp.getBotFlags());
    ubiSession.setFindingFlags(ubiSessionTmp.getFindingFlags());
    ubiSession.setStartPageId(ubiSessionTmp.getStartPageId());
    ubiSession.setEndPageId(ubiSessionTmp.getEndPageId());
    ubiSession.setDurationSec(ubiSessionTmp.getDurationSec());
    ubiSession.setEventCnt(ubiSessionTmp.getEventCnt());
    ubiSession.setAbsEventCnt(ubiSessionTmp.getAbsEventCnt());
    ubiSession.setViCoreCnt(ubiSessionTmp.getViCoreCnt());
    ubiSession.setBidCoreCnt(ubiSessionTmp.getBidCoreCnt());
    ubiSession.setBinCoreCnt(ubiSessionTmp.getBinCoreCnt());
    ubiSession.setWatchCoreCnt(ubiSessionTmp.getWatchCoreCnt());
    ubiSession.setTrafficSrcId(ubiSessionTmp.getTrafficSrcId());
    ubiSession.setAbsDuration(ubiSessionTmp.getAbsDuration());
    ubiSession.setCobrand(ubiSessionTmp.getCobrand());
    ubiSession.setFirstAppId(ubiSessionTmp.getFirstAppId());
    ubiSession.setFirstSiteId(ubiSessionTmp.getFirstSiteId());
    ubiSession.setFirstCguid(ubiSessionTmp.getFirstCguid());
    ubiSession.setFirstMappedUserId(ubiSessionTmp.getFirstMappedUserId());
    ubiSession.setHomepageCnt(ubiSessionTmp.getHomepageCnt());
    ubiSession.setGr1Cnt(ubiSessionTmp.getGr1Cnt());
    ubiSession.setGrCnt(ubiSessionTmp.getGrCnt());
    ubiSession.setMyebayCnt(ubiSessionTmp.getMyebayCnt());
    ubiSession.setSigninPageCnt(ubiSessionTmp.getSigninPageCnt());
    ubiSession.setAsqCnt(ubiSessionTmp.getAsqCnt());
    ubiSession.setAtcCnt(ubiSessionTmp.getAtcCnt());
    ubiSession.setAtlCnt(ubiSessionTmp.getAtlCnt());
    ubiSession.setBoCnt(ubiSessionTmp.getBoCnt());
    ubiSession.setSrpCnt(ubiSessionTmp.getSrpCnt());
    ubiSession.setServEventCnt(ubiSessionTmp.getServEventCnt());
    ubiSession.setSearchViewPageCnt(ubiSessionTmp.getSearchViewPageCnt());
    ubiSession.setCity(ubiSessionTmp.getCity());
    ubiSession.setRegion(ubiSessionTmp.getRegion());
    ubiSession.setCountry(ubiSessionTmp.getCountry());
    ubiSession.setContinent(ubiSessionTmp.getContinent());
    ubiSession.setBrowserFamily(ubiSessionTmp.getBrowserFamily());
    ubiSession.setBrowserVersion(ubiSessionTmp.getBrowserVersion());
    ubiSession.setDeviceFamily(ubiSessionTmp.getDeviceFamily());
    ubiSession.setDeviceClass(ubiSessionTmp.getDeviceClass());
    ubiSession.setOsFamily(ubiSessionTmp.getOsFamily());
    ubiSession.setOsVersion(ubiSessionTmp.getOsVersion());
    ubiSession.setStartResourceId(ubiSessionTmp.getStartResourceId());
    ubiSession.setEndResourceId(ubiSessionTmp.getEndResourceId());
    ubiSession.setIsReturningVisitor(ubiSessionTmp.isReturningVisitor());
    ubiSession.setLineSpeed(ubiSessionTmp.getLineSpeed());
    ubiSession.setPulsarEventCnt(ubiSessionTmp.getPulsarEventCnt());
    ubiSession.setSessionEndDt(ubiSessionTmp.getSessionEndDt());
    ubiSession.setStreamId(type);
    ubiSession.setBuserId(ubiSessionTmp.getBuserId());
    ubiSession.setOpenEmit(isOpen);
    out.collect(ubiSession);
  }

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<SessionAccumulator> elements,
      Collector<UbiSession> out)
      throws Exception {
    SessionAccumulator sessionAccumulator = elements.iterator().next();
    if (context.currentWatermark() >= context.window().maxTimestamp()) {
      endSessionEvent(sessionAccumulator);
      Set<Integer> botFlagList = sessionEndBotDetector
          .getBotFlagList(sessionAccumulator.getUbiSession());
      sessionAccumulator.getUbiSession().getBotFlagList().addAll(botFlagList);
      outputSession(sessionAccumulator.getUbiSession(), out, false, "0");
    } else if (sessionAccumulator.getUbiSessionSplit() != null) {
      endSessionEvent(sessionAccumulator);
      Set<Integer> botFlagList = sessionEndBotDetector
          .getBotFlagList(sessionAccumulator.getUbiSessionSplit());
      sessionAccumulator.getUbiSessionSplit().getBotFlagList().addAll(botFlagList);
      outputSession(sessionAccumulator.getUbiSessionSplit(), out, false, "0");
    } else {
      long absStartDate = SojTimestamp
          .getUnixDateFromSOjTimestamp(sessionAccumulator.getUbiSession().getAbsStartTimestamp());
      long absEndDate =
          SojTimestamp
              .getUnixDateFromSOjTimestamp(sessionAccumulator.getUbiSession().getAbsEndTimestamp());
      long currentWaterMark = SojTimestamp.getUnixDateFromUnixTimestamp(context.currentWatermark());
      //      String absStartDateStr = SojTimestamp.getDateStrWithUnixTimestamp(absStartDate);
      //      String absEndDateStr = SojTimestamp.getDateStrWithUnixTimestamp(absEndDate);
      //      String currentWaterMarkStr = SojTimestamp.getDateStrWithUnixTimestamp
      //      (currentWaterMark);
      MapState<String, Long> lastTimestampState =
          context.globalState().getMapState(lastTimestampStateDescriptor);
      Long lastTimstamp =
          lastTimestampState.get(sessionAccumulator.getUbiSession().getGuid()) == null
              ? absStartDate :
              lastTimestampState.get(sessionAccumulator.getUbiSession().getGuid());

      if (lastTimstamp < absEndDate) {
        StringBuffer sb = new StringBuffer();
        sb.append("=============Senario 1============ ");
        sb.append(" GUID： " + sessionAccumulator.getUbiSession().getGuid());
        sb.append(
            " getAbsEndTimestamp：" + sessionAccumulator.getUbiSession().getAbsEndTimestamp());
        sb.append(
            " getAbsStartTimestamp: " + sessionAccumulator.getUbiSession().getAbsStartTimestamp());
        sb.append(" absStartDate：" + absStartDate);
        sb.append(" lastTimstamp: " + lastTimstamp);
        sb.append(" absEndDate: " + absEndDate);
        sb.append(" type: " + 1);
        endSessionEvent(sessionAccumulator);
        Set<Integer> botFlagList = sessionEndBotDetector
            .getBotFlagList(sessionAccumulator.getUbiSession());
        sessionAccumulator.getUbiSession().getBotFlagList().addAll(botFlagList);
        outputSession(sessionAccumulator.getUbiSession(), out, true, sb.toString());
        lastTimestampState.put(sessionAccumulator.getUbiSession().getGuid(), absEndDate);
      } else if (currentWaterMark > absEndDate && currentWaterMark > lastTimstamp) {
        StringBuffer sb = new StringBuffer();
        sb.append("=============Senario 2============ ");
        sb.append(" GUID： " + sessionAccumulator.getUbiSession().getGuid());
        sb.append(" currentWaterMark before ：" + context.currentWatermark());
        sb.append(" currentWaterMark：" + currentWaterMark);
        sb.append(" lastTimstamp: " + lastTimstamp);
        sb.append(" absEndDate: " + absEndDate);
        sb.append(" type: " + 2);
        endSessionEvent(sessionAccumulator);
        Set<Integer> botFlagList = sessionEndBotDetector
            .getBotFlagList(sessionAccumulator.getUbiSession());
        sessionAccumulator.getUbiSession().getBotFlagList().addAll(botFlagList);
        outputSession(sessionAccumulator.getUbiSession(), out, true, sb.toString());
        lastTimestampState.put(sessionAccumulator.getUbiSession().getGuid(), currentWaterMark);
      }

    }
  }

  private void endSessionEvent(SessionAccumulator sessionAccumulator) throws Exception {
    SessionMetrics.getInstance().end(sessionAccumulator);
  }

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    sessionEndBotDetector = SessionEndBotDetector.getInstance();
  }

  @Override
  public void clear(Context context) throws Exception {
    context.globalState().getMapState(lastTimestampStateDescriptor).clear();
  }
}
