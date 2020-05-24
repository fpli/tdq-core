package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.util.TransformUtil;
import java.util.ArrayList;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

public class UbiSessionToIntermediateSessionMapFunction extends
    RichMapFunction<UbiSession, IntermediateSession> {

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public IntermediateSession map(UbiSession ubiSession) throws Exception {
    IntermediateSession intermediateSession = new IntermediateSession();
    Long[] guidEehance = TransformUtil.stringToLong(ubiSession.getGuid());
    intermediateSession.setGuid(ubiSession.getGuid());
    intermediateSession.setGuid1(guidEehance[0]);
    intermediateSession.setGuid2(guidEehance[1]);
    intermediateSession.setAbsEventCnt(ubiSession.getAbsEventCnt());
    intermediateSession.setAbsStartTimestamp(ubiSession.getAbsStartTimestamp());
    intermediateSession.setAgentCnt(ubiSession.getAgentCnt());
    intermediateSession.setBidBinConfirmFlag(ubiSession.getBidBinConfirmFlag());
    intermediateSession.setBotFlag(ubiSession.getBotFlag());
    intermediateSession.setClientIp(ubiSession.getClientIp());
    intermediateSession.setCobrand(ubiSession.getCobrand());
    intermediateSession.setExInternalIp(ubiSession.getExInternalIp());
    intermediateSession.setFirstAppId(ubiSession.getFirstAppId());
    intermediateSession.setFirstCguid(ubiSession.getFirstCguid());
    intermediateSession.setFirstUserId(ubiSession.getFirstUserId());
    intermediateSession.setHomepageCnt(ubiSession.getHomepageCnt());
    intermediateSession.setIp(ubiSession.getIp());
    intermediateSession.setNonIframeRdtEventCnt(ubiSession.getNonIframeRdtEventCnt());
    intermediateSession.setSigninPageCnt(ubiSession.getSigninPageCnt());
    intermediateSession.setSingleClickSessionFlag(ubiSession.getSingleClickSessionFlag());
    intermediateSession.setTrafficSrcId(ubiSession.getTrafficSrcId());
    intermediateSession.setValidPageCnt(ubiSession.getValidPageCnt());
    intermediateSession.setUserAgent(ubiSession.getUserAgent());
    intermediateSession.setStartTimestamp(ubiSession.getStartTimestamp());
    intermediateSession.setFamilyViCnt(ubiSession.getFamilyViCnt());
    intermediateSession.setBotFlagList(new ArrayList<>(ubiSession.getBotFlagList()));
    intermediateSession.setEndTimestamp(ubiSession.getEndTimestamp());
    intermediateSession.setAbsEndTimestamp(ubiSession.getAbsEndTimestamp());
    intermediateSession.setSessionId(ubiSession.getSessionId());
    intermediateSession.setSessionSkey(ubiSession.getSessionSkey());
    intermediateSession.setSojDataDt(ubiSession.getSojDataDt());
    intermediateSession.setSessionStartDt(ubiSession.getSessionStartDt());
    intermediateSession.setAsqCnt(ubiSession.getAsqCnt());
    intermediateSession.setAtcCnt(ubiSession.getAtcCnt());
    intermediateSession.setAtlCnt(ubiSession.getAtlCnt());
    intermediateSession.setBoCnt(ubiSession.getBoCnt());
    intermediateSession.setSrpCnt(ubiSession.getSrpCnt());
    intermediateSession.setServEventCnt(ubiSession.getServEventCnt());
    intermediateSession.setSearchViewPageCnt(ubiSession.getSearchViewPageCnt());
    intermediateSession.setBrowserFamily(ubiSession.getBrowserFamily());
    intermediateSession.setBrowserVersion(ubiSession.getBrowserVersion());
    intermediateSession.setCity(ubiSession.getCity());
    intermediateSession.setContinent(ubiSession.getContinent());
    intermediateSession.setCountry(ubiSession.getCountry());
    intermediateSession.setDeviceClass(ubiSession.getDeviceClass());
    intermediateSession.setDeviceFamily(ubiSession.getDeviceFamily());
    intermediateSession.setEndResourceId(ubiSession.getEndResourceId());
    intermediateSession.setIsReturningVisitor(ubiSession.isReturningVisitor());
    intermediateSession.setLineSpeed(ubiSession.getLineSpeed());
    intermediateSession.setOsFamily(ubiSession.getOsFamily());
    intermediateSession.setOsVersion(ubiSession.getOsVersion());
    intermediateSession.setPulsarEventCnt(ubiSession.getPulsarEventCnt());
    intermediateSession.setRegion(ubiSession.getRegion());
    intermediateSession.setSessionEndDt(ubiSession.getSessionEndDt());
    intermediateSession.setStartResourceId(ubiSession.getStartResourceId());
    intermediateSession.setStreamId(ubiSession.getStreamId());
    intermediateSession.setBuserId(ubiSession.getBuserId());
    intermediateSession.setGr1Cnt(ubiSession.getGr1Cnt());
    intermediateSession.setGrCnt(ubiSession.getGrCnt());
    intermediateSession.setMyebayCnt(ubiSession.getMyebayCnt());
    intermediateSession.setFirstSessionStartDt(ubiSession.getFirstSessionStartDt());
    intermediateSession.setSessionEndDt(ubiSession.getSessionEndDt());
    intermediateSession.setSessionReferrer(ubiSession.getSessionReferrer());
    intermediateSession.setSiteFlags(ubiSession.getSiteFlags());
    intermediateSession.setAttrFlags(ubiSession.getAttrFlags());
    intermediateSession.setVersion(ubiSession.getVersion());
    intermediateSession.setBotFlags(ubiSession.getBotFlags());
    intermediateSession.setFindingFlags(ubiSession.getFindingFlags());
    intermediateSession.setStartPageId(ubiSession.getStartPageId());
    intermediateSession.setEndPageId(ubiSession.getEndPageId());
    intermediateSession.setDurationSec(ubiSession.getDurationSec());
    intermediateSession.setEventCnt(ubiSession.getEventCnt());
    intermediateSession.setViCoreCnt(ubiSession.getViCoreCnt());
    intermediateSession.setBidCoreCnt(ubiSession.getBidCoreCnt());
    intermediateSession.setBinCoreCnt(ubiSession.getBinCoreCnt());
    intermediateSession.setWatchCoreCnt(ubiSession.getWatchCoreCnt());
    intermediateSession.setFirstSiteId(ubiSession.getFirstSiteId());
    intermediateSession.setFirstMappedUserId(ubiSession.getFirstMappedUserId());
    intermediateSession.setAbsDuration(ubiSession.getAbsDuration());
    return intermediateSession;
  }
}
