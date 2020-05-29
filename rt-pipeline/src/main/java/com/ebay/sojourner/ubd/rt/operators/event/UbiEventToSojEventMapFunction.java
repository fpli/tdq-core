package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import java.util.ArrayList;
import org.apache.flink.api.common.functions.RichMapFunction;

public class UbiEventToSojEventMapFunction extends RichMapFunction<UbiEvent, SojEvent> {

  @Override
  public SojEvent map(UbiEvent ubiEvent) throws Exception {
    SojEvent sojEvent = new SojEvent();
    sojEvent.setGuid(ubiEvent.getGuid());
    sojEvent.setAppId(ubiEvent.getAppId());
    sojEvent.setApplicationPayload(ubiEvent.getApplicationPayload());
    sojEvent.setAppVersion(ubiEvent.getAppVersion());
    sojEvent.setBotFlags(new ArrayList<>(ubiEvent.getBotFlags()));
    sojEvent.setClientData(ubiEvent.getClientData().toString());
    sojEvent.setBrowserFamily(ubiEvent.getBrowserFamily());
    sojEvent.setBrowserVersion(ubiEvent.getBrowserVersion());
    sojEvent.setClickId(ubiEvent.getClickId());
    sojEvent.setClientIP(ubiEvent.getClientIP());
    sojEvent.setCobrand(ubiEvent.getCobrand());
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
    sojEvent.setEventTimestamp(SOJTS2Date.getDateStrWithMillis(ubiEvent.getEventTimestamp()));
    sojEvent.setFlags(ubiEvent.getFlags());
    sojEvent.setForwardedFor(ubiEvent.getForwardedFor());
    sojEvent.setGenerateTime(ubiEvent.getGenerateTime());
    sojEvent.setIcfBinary(ubiEvent.getIcfBinary());
    sojEvent.setIframe(ubiEvent.isIframe() ? 1 : 0);
    sojEvent.setIngestTime(ubiEvent.getIngestTime());
    sojEvent.setItemId(ubiEvent.getItemId());
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
    sojEvent.setSessionStartDt(ubiEvent.getSessionStartDt() == null ? null
        : SOJTS2Date.getDateStr(ubiEvent.getSessionStartDt()));
    sojEvent.setSojDataDt(SOJTS2Date.getDateStr(ubiEvent.getSojDataDt()));
    sojEvent.setSessionStartTime(ubiEvent.getSessionStartTime());
    sojEvent.setSid(ubiEvent.getSid());
    sojEvent.setSiteId(ubiEvent.getSiteId());
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
}
