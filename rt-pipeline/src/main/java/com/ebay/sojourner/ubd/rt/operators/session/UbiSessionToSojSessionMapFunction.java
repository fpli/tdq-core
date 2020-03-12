package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.common.model.UbiSession;
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
    sojSession.setBotFlagList(ubiSession.getBotFlagList());
    sojSession.setNonIframeRdtEventCnt(ubiSession.getNonIframeRdtEventCnt());
    sojSession.setSessionReferrer(ubiSession.getSessionReferrer());
    sojSession.setBotFlag(ubiSession.getBotFlag());
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
    return sojSession;
  }
}
