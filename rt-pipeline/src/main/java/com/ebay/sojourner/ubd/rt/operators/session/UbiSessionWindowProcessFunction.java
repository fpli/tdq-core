package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

@Slf4j
public class UbiSessionWindowProcessFunction
    extends ProcessWindowFunction<SessionAccumulator, UbiSession, Tuple, TimeWindow> {

  private static SessionMetrics sessionMetrics;
  private OutputTag outputTag = null;

  public UbiSessionWindowProcessFunction() {

  }

  public UbiSessionWindowProcessFunction(OutputTag outputTag) {
    this.outputTag = outputTag;
  }

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<SessionAccumulator> elements,
      Collector<UbiSession> out)
      throws Exception {

    if (sessionMetrics == null) {
      sessionMetrics = SessionMetrics.getInstance();
    }

    SessionAccumulator sessionAccumulator = elements.iterator().next();
    endSessionEvent(sessionAccumulator);
    UbiSession ubiSession = new UbiSession();
    ubiSession.setGuid(sessionAccumulator.getUbiSession().getGuid());
    ubiSession.setAgentString(sessionAccumulator.getUbiSession().getAgentString());
    ubiSession.setSessionId(sessionAccumulator.getUbiSession().getSessionId());
    ubiSession.setSessionSkey(sessionAccumulator.getUbiSession().getSessionSkey());
    ubiSession.setIp(sessionAccumulator.getUbiSession().getIp());
    ubiSession.setUserAgent(sessionAccumulator.getUbiSession().getUserAgent());
    ubiSession.setExInternalIp(sessionAccumulator.getUbiSession().getExInternalIp());
    ubiSession.setSojDataDt(sessionAccumulator.getUbiSession().getSojDataDt());
    ubiSession.setSessionStartDt(sessionAccumulator.getUbiSession().getSessionStartDt());
    ubiSession.setAgentCnt(sessionAccumulator.getUbiSession().getAgentCnt());
    ubiSession.setStartTimestamp(sessionAccumulator.getUbiSession().getStartTimestamp());
    ubiSession.setEndTimestamp(sessionAccumulator.getUbiSession().getEndTimestamp());
    ubiSession.setAbsStartTimestamp(sessionAccumulator.getUbiSession().getAbsStartTimestamp());
    ubiSession.setAbsEndTimestamp(sessionAccumulator.getUbiSession().getAbsEndTimestamp());
    ubiSession.setClientIp(sessionAccumulator.getUbiSession().getClientIp());
    ubiSession.setInternalIp(sessionAccumulator.getUbiSession().getInternalIp());
    ubiSession.setSingleClickSessionFlag(
        sessionAccumulator.getUbiSession().getSingleClickSessionFlag());
    ubiSession.setBotFlagList(sessionAccumulator.getUbiSession().getBotFlagList());
    ubiSession.setNonIframeRdtEventCnt(
        sessionAccumulator.getUbiSession().getNonIframeRdtEventCnt());
    ubiSession.setSessionReferrer(sessionAccumulator.getUbiSession().getSessionReferrer());
    ubiSession.setBotFlag(sessionAccumulator.getUbiSession().getBotFlag());
    ubiSession.setVersion(sessionAccumulator.getUbiSession().getVersion());
    ubiSession.setFirstUserId(sessionAccumulator.getUbiSession().getFirstUserId());
    ubiSession.setSiteFlagsSet(sessionAccumulator.getUbiSession().getSiteFlagsSet());
    ubiSession.setSiteFlags(sessionAccumulator.getUbiSession().getSiteFlags());
    ubiSession.setAttrFlags(sessionAccumulator.getUbiSession().getAttrFlags());
    ubiSession.setBotFlags(sessionAccumulator.getUbiSession().getBotFlags());
    ubiSession.setFindingFlags(sessionAccumulator.getUbiSession().getFindingFlags());
    ubiSession.setStartPageId(sessionAccumulator.getUbiSession().getStartPageId());
    ubiSession.setEndPageId(sessionAccumulator.getUbiSession().getEndPageId());
    ubiSession.setDurationSec(sessionAccumulator.getUbiSession().getDurationSec());
    ubiSession.setEventCnt(sessionAccumulator.getUbiSession().getEventCnt());
    ubiSession.setViCoreCnt(sessionAccumulator.getUbiSession().getViCoreCnt());
    ubiSession.setBidCoreCnt(sessionAccumulator.getUbiSession().getBidCoreCnt());
    ubiSession.setBinCoreCnt(sessionAccumulator.getUbiSession().getBinCoreCnt());
    ubiSession.setWatchCoreCnt(sessionAccumulator.getUbiSession().getWatchCoreCnt());
    ubiSession.setTrafficSrcId(sessionAccumulator.getUbiSession().getTrafficSrcId());
    ubiSession.setAbsDuration(sessionAccumulator.getUbiSession().getAbsDuration());
    ubiSession.setCobrand(sessionAccumulator.getUbiSession().getCobrand());
    ubiSession.setFirstAppId(sessionAccumulator.getUbiSession().getFirstAppId());
    ubiSession.setFirstSiteId(sessionAccumulator.getUbiSession().getFirstSiteId());
    ubiSession.setFirstCguid(sessionAccumulator.getUbiSession().getFirstCguid());
    ubiSession.setFirstMappedUserId(sessionAccumulator.getUbiSession().getFirstMappedUserId());
    ubiSession.setHomepageCnt(sessionAccumulator.getUbiSession().getHomepageCnt());
    ubiSession.setGr1Cnt(sessionAccumulator.getUbiSession().getGr1Cnt());
    ubiSession.setGrCnt(sessionAccumulator.getUbiSession().getGrCnt());
    ubiSession.setMyebayCnt(sessionAccumulator.getUbiSession().getMyebayCnt());
    ubiSession.setSigninPageCnt(sessionAccumulator.getUbiSession().getSigninPageCnt());

    out.collect(ubiSession);
  }

  private void endSessionEvent(SessionAccumulator sessionAccumulator) throws Exception {
    sessionMetrics.end(sessionAccumulator);
  }

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
  }
}
