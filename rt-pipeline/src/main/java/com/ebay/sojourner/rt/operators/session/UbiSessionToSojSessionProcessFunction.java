package com.ebay.sojourner.rt.operators.session;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.RulePriorityUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class UbiSessionToSojSessionProcessFunction extends ProcessFunction<UbiSession, SojSession> {

  private OutputTag outputTag;
  private List<Integer> intermediateBotFlagList = Arrays.asList(220, 221, 222, 223);

  public UbiSessionToSojSessionProcessFunction(OutputTag outputTag) {
    this.outputTag = outputTag;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public void processElement(UbiSession ubiSession, Context context, Collector<SojSession> out)
      throws Exception {
    SojSession sojSession = new SojSession();
    sojSession.setGuid(ubiSession.getGuid());
    sojSession.setSessionId(ubiSession.getSessionId());
    sojSession.setSessionSkey(ubiSession.getSessionSkey());
    sojSession.setIp(ubiSession.getIp());
    sojSession.setUserAgent(ubiSession.getUserAgent());
    sojSession.setSojDataDt(ubiSession.getSojDataDt());
    sojSession.setSessionStartDt(ubiSession.getSessionStartDt());
    sojSession.setStartTimestamp(ubiSession.getStartTimestamp());
    sojSession.setEndTimestamp(ubiSession.getEndTimestamp());
    sojSession.setAbsStartTimestamp(ubiSession.getAbsStartTimestamp());
    sojSession.setAbsEndTimestamp(ubiSession.getAbsEndTimestamp());
    sojSession.setBotFlagList(new ArrayList<>(ubiSession.getBotFlagList()));
    sojSession.setNonIframeRdtEventCnt(ubiSession.getNonIframeRdtEventCnt());
    sojSession.setSessionReferrer(ubiSession.getSessionReferrer());
    sojSession.setBotFlag(RulePriorityUtils.getHighPriorityBotFlag(ubiSession.getBotFlagList()));
    sojSession.setVersion(ubiSession.getVersion());
    sojSession.setFirstUserId(ubiSession.getFirstUserId());
    sojSession.setSiteFlags(ubiSession.getSiteFlags());
    sojSession.setAttrFlags(ubiSession.getAttrFlags());
    sojSession.setBotFlags(ubiSession.getBotFlags());
    sojSession.setFindingFlags(ubiSession.getFindingFlags());
    sojSession.setStartPageId(ubiSession.getStartPageId());
    sojSession.setEndPageId(ubiSession.getEndPageId());
    sojSession.setDurationSec(ubiSession.getDurationSec());
    sojSession.setEventCnt(ubiSession.getEventCnt());
    sojSession.setAbsEventCnt(ubiSession.getAbsEventCnt());
    sojSession.setViCoreCnt(ubiSession.getViCoreCnt());
    sojSession.setBidCoreCnt(ubiSession.getBidCoreCnt());
    sojSession.setBinCoreCnt(ubiSession.getBinCoreCnt());
    sojSession.setWatchCoreCnt(ubiSession.getWatchCoreCnt());
    sojSession.setTrafficSrcId(ubiSession.getTrafficSrcId());
    sojSession.setAbsDuration(ubiSession.getAbsDuration());
    sojSession.setCobrand(ubiSession.getCobrand());
    sojSession.setFirstAppId(ubiSession.getFirstAppId());
    sojSession.setFirstSiteId(ubiSession.getFirstSiteId());
    sojSession.setFirstCguid(ubiSession.getFirstCguid());
    sojSession.setFirstMappedUserId(ubiSession.getFirstMappedUserId());
    sojSession.setHomepageCnt(ubiSession.getHomepageCnt());
    sojSession.setGr1Cnt(ubiSession.getGr1Cnt());
    sojSession.setGrCnt(ubiSession.getGrCnt());
    sojSession.setMyebayCnt(ubiSession.getMyebayCnt());
    sojSession.setSigninPageCnt(ubiSession.getSigninPageCnt());
    sojSession.setFirstSessionStartDt(ubiSession.getFirstSessionStartDt());
    sojSession.setSingleClickSessionFlag(ubiSession.getSingleClickSessionFlag());
    sojSession.setAsqCnt(ubiSession.getAsqCnt());
    sojSession.setAtcCnt(ubiSession.getAtcCnt());
    sojSession.setAtlCnt(ubiSession.getAtlCnt());
    sojSession.setBoCnt(ubiSession.getBoCnt());
    sojSession.setSrpCnt(ubiSession.getSrpCnt());
    sojSession.setServEventCnt(ubiSession.getServEventCnt());
    sojSession.setSearchViewPageCnt(ubiSession.getSearchViewPageCnt());
    sojSession.setBrowserFamily(ubiSession.getBrowserFamily());
    sojSession.setBrowserVersion(ubiSession.getBrowserVersion());
    sojSession.setCity(ubiSession.getCity());
    sojSession.setContinent(ubiSession.getContinent());
    sojSession.setCountry(ubiSession.getCountry());
    sojSession.setDeviceClass(ubiSession.getDeviceClass());
    sojSession.setDeviceFamily(ubiSession.getDeviceFamily());
    sojSession.setEndResourceId(ubiSession.getEndResourceId());
    sojSession.setIsReturningVisitor(ubiSession.isReturningVisitor());
    sojSession.setLineSpeed(ubiSession.getLineSpeed());
    sojSession.setOsFamily(ubiSession.getOsFamily());
    sojSession.setOsVersion(ubiSession.getOsVersion());
    sojSession.setPulsarEventCnt(ubiSession.getPulsarEventCnt());
    sojSession.setRegion(ubiSession.getRegion());
    sojSession.setSessionEndDt(ubiSession.getSessionEndDt());
    sojSession.setStartResourceId(ubiSession.getStartResourceId());
    sojSession.setStreamId(ubiSession.getStreamId());
    sojSession.setBuserId(ubiSession.getBuserId());
    sojSession.setIsOpen(ubiSession.isOpenEmit());

    // split bot session and nonbot session
    if (sojSession.getBotFlagList().size() == 0 || CollectionUtils
        .subtract(sojSession.getBotFlagList(), intermediateBotFlagList).size() == 0) {
      out.collect(sojSession);
    } else {
      context.output(outputTag, sojSession);
    }
  }

  @Override
  public void close() throws Exception {
    super.close();
  }
}
