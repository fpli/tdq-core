package com.ebay.sojourner.rt.operators.session;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.RulePriorityUtils;
import java.util.ArrayList;
import org.apache.flink.api.common.functions.RichMapFunction;

public class UbiSessionToSojSessionMapFunction extends RichMapFunction<UbiSession, SojSession> {

  @Override
  public SojSession map(UbiSession ubiSession) throws Exception {
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
    return sojSession;
  }
}
