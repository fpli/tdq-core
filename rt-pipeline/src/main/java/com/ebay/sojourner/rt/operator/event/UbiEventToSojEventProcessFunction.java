package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class UbiEventToSojEventProcessFunction extends ProcessFunction<UbiEvent, SojEvent> {

  private OutputTag outputTag;
  private List<Integer> intermediateBotFlagList = Arrays.asList(220, 221, 222, 223);

  public UbiEventToSojEventProcessFunction(OutputTag outputTag) {
    this.outputTag = outputTag;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public void processElement(UbiEvent ubiEvent, Context context, Collector<SojEvent> out)
      throws Exception {
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
    sojEvent.setEventTimestamp(SojTimestamp.getDateStrWithMillis(ubiEvent.getEventTimestamp()));
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
        : SojTimestamp.getDateStr(ubiEvent.getSessionStartDt()));
    sojEvent.setSojDataDt(SojTimestamp.getDateStr(ubiEvent.getSojDataDt()));
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

    // split bot event and nonbot event
    if (sojEvent.getBotFlags().size() == 0 || CollectionUtils
        .subtract(sojEvent.getBotFlags(), intermediateBotFlagList).size() == 0) {
      out.collect(sojEvent);
    } else {
      context.output(outputTag, sojEvent);
    }
  }

  @Override
  public void close() throws Exception {
    super.close();
  }
}
