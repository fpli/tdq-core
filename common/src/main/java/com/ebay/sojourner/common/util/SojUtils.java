package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import java.util.ArrayList;

public class SojUtils {

  public static boolean isRover3084Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }
    return 3084 == event.getPageId();
  }

  public static boolean isRover3085Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }
    return event.getPageId() == 3085;
  }

  public static boolean isRover3962Click(UbiEvent event) {
    if (event.getPageId() == Integer.MIN_VALUE) {
      return false;
    }
    return event.getPageId() == 3962;
  }

  public static boolean isRoverClick(UbiEvent event) {
    return IntermediateLkp.getInstance().getRoverPageSet().contains(event.getPageId());
  }

  public static boolean isScEvent(UbiEvent event) {
    Integer pageId = event.getPageId() == Integer.MIN_VALUE ? -99 : event.getPageId();
    return !event.isRdt()
        && !event.isIframe()
        // || urlQueryString.matches("(/roverimp|.*SojPageView).*")
        && !IntermediateLkp.getInstance().getScPageSet1().contains(pageId)
        && !IntermediateLkp.getInstance().getScPageSet2().contains(pageId);
  }

  public static SojEvent convertUbiEvent2SojEvent(UbiEvent ubiEvent){
    SojEvent sojEvent = new SojEvent();
    sojEvent.setGuid(ubiEvent.getGuid());
    sojEvent.setAppId(ubiEvent.getAppId());
    sojEvent.setApplicationPayload(ubiEvent.getApplicationPayload());
    sojEvent.setAppVersion(ubiEvent.getAppVersion());
    sojEvent.setBotFlags(new ArrayList<>(ubiEvent.getBotFlags()));
    sojEvent.setClientData(
        ubiEvent.getClientData() == null ? null : ubiEvent.getClientData().toString());
    sojEvent.setBrowserFamily(ubiEvent.getBrowserFamily());
    sojEvent.setBrowserVersion(ubiEvent.getBrowserVersion());
    sojEvent.setClickId(String.valueOf(ubiEvent.getClickId()));
    sojEvent.setClientIP(ubiEvent.getClientIP());
    sojEvent.setCobrand(String.valueOf(ubiEvent.getCobrand()));
    sojEvent.setCookies(ubiEvent.getCookies());
    sojEvent.setCurrentImprId(ubiEvent.getCurrentImprId());
    sojEvent.setDataCenter(ubiEvent.getDataCenter());
    sojEvent.setDeviceFamily(ubiEvent.getDeviceFamily());
    sojEvent.setDeviceType(ubiEvent.getDeviceType());
    sojEvent.setEnrichedOsVersion(ubiEvent.getEnrichedOsVersion());
    sojEvent.setEventAction(ubiEvent.getEventAction());
    sojEvent.setEventCaptureTime(ubiEvent.getEventCaptureTime());
    sojEvent.setEventAttr(ubiEvent.getEventAttr());
    sojEvent.setEventCnt(ubiEvent.getEventCnt());
    sojEvent.setEventFamily(ubiEvent.getEventFamily());
    sojEvent.setEventTimestamp(ubiEvent.getEventTimestamp());
    sojEvent.setFlags(ubiEvent.getFlags());
    sojEvent.setForwardedFor(ubiEvent.getForwardedFor());
    sojEvent.setGenerateTime(ubiEvent.getGenerateTime());
    sojEvent.setIcfBinary(ubiEvent.getIcfBinary());
    sojEvent.setIframe(ubiEvent.isIframe());
    sojEvent.setIngestTime(ubiEvent.getIngestTime());
    sojEvent.setItemId(ubiEvent.getItemId()==null? null:String.valueOf(ubiEvent.getItemId()));
    sojEvent.setOldSessionSkey(ubiEvent.getOldSessionSkey());
    sojEvent.setOsFamily(ubiEvent.getOsFamily());
    sojEvent.setOsVersion(ubiEvent.getOsVersion());
    sojEvent.setPageFamily(ubiEvent.getPageFamily());
    sojEvent.setPageId(ubiEvent.getPageId());
    sojEvent.setPageName(ubiEvent.getPageName());
    sojEvent.setAgentInfo(ubiEvent.getAgentInfo());
    sojEvent.setPartialValidPage(ubiEvent.isPartialValidPage());
    sojEvent.setRdt(ubiEvent.isRdt() ? 1 : 0);
    sojEvent.setRefererHash(ubiEvent.getRefererHash());
    sojEvent.setReferrer(ubiEvent.getReferrer());
    sojEvent.setRegu(ubiEvent.getRegu());
    sojEvent.setRemoteIP(ubiEvent.getRemoteIP());
    sojEvent.setRequestCorrelationId(ubiEvent.getRequestCorrelationId());
    sojEvent.setReservedForFuture(ubiEvent.getReservedForFuture());
    sojEvent.setRlogid(ubiEvent.getRlogid());
    sojEvent.setSeqNum(ubiEvent.getSeqNum());
    sojEvent.setSessionEndTime(ubiEvent.getSessionEndTime());
    sojEvent.setSessionSkey(ubiEvent.getSessionSkey());
    sojEvent.setSessionId(ubiEvent.getSessionId());
    sojEvent.setSessionStartDt(ubiEvent.getSessionStartDt());
    sojEvent.setSojDataDt(ubiEvent.getSojDataDt());
    sojEvent.setSessionStartTime(ubiEvent.getSessionStartTime());
    sojEvent.setSid(ubiEvent.getSid());
    sojEvent.setSiteId(String.valueOf(ubiEvent.getSiteId()));
    sojEvent.setSourceImprId(ubiEvent.getSourceImprId());
    sojEvent.setSqr(ubiEvent.getSqr());
    sojEvent.setStaticPageType(ubiEvent.getStaticPageType());
    sojEvent.setTrafficSource(ubiEvent.getTrafficSource());
    sojEvent.setUrlQueryString(ubiEvent.getUrlQueryString());
    sojEvent.setUserId(ubiEvent.getUserId());
    sojEvent.setVersion(ubiEvent.getVersion());
    sojEvent.setWebServer(ubiEvent.getWebServer());
    return sojEvent;
  }

  public static SojSession convertUbiSession2SojSession(UbiSession ubiSession){
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
    return sojSession;
  }
}
