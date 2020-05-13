package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.model.UbiSessionForDQ;
import java.util.ArrayList;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

public class UbiSessionToUbiSessionForDQMapFunction extends
    RichMapFunction<UbiSession, UbiSessionForDQ> {

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public UbiSessionForDQ map(UbiSession ubiSession) throws Exception {
    UbiSessionForDQ ubiSessionForDQ = new UbiSessionForDQ();
    ubiSessionForDQ.setSojDataDt(ubiSession.getSojDataDt());
    ubiSessionForDQ.setGuid(ubiSession.getGuid());
    ubiSessionForDQ.setSessionId(ubiSession.getSessionId());
    ubiSessionForDQ.setSessionSkey(ubiSession.getSessionSkey());
    ubiSessionForDQ.setSessionStartDt(ubiSession.getSessionStartDt());
    ubiSessionForDQ.setIp(ubiSession.getIp());
    ubiSessionForDQ.setUserAgent(ubiSession.getUserAgent());
    ubiSessionForDQ.setSessionReferrer(ubiSession.getSessionReferrer());
    ubiSessionForDQ.setBotFlag(ubiSession.getBotFlag());
    ubiSessionForDQ.setVersion(ubiSession.getVersion());
    ubiSessionForDQ.setFirstUserId(ubiSession.getFirstUserId());
    ubiSessionForDQ.setSiteFlags(ubiSession.getSiteFlags());
    ubiSessionForDQ.setAttrFlags(ubiSession.getAttrFlags());
    ubiSessionForDQ.setBotFlags(ubiSession.getBotFlags());
    ubiSessionForDQ.setFindingFlags(ubiSession.getFindingFlags());
    ubiSessionForDQ.setStartPageId(ubiSession.getStartPageId());
    ubiSessionForDQ.setEndPageId(ubiSession.getEndPageId());
    ubiSessionForDQ.setStartTimestamp(ubiSession.getStartTimestamp());
    ubiSessionForDQ.setDurationSec(ubiSession.getDurationSec());
    ubiSessionForDQ.setEventCnt(ubiSession.getEventCnt());
    ubiSessionForDQ.setViCoreCnt(ubiSession.getViCoreCnt());
    ubiSessionForDQ.setBidCoreCnt(ubiSession.getBidCoreCnt());
    ubiSessionForDQ.setBinCoreCnt(ubiSession.getBinCoreCnt());
    ubiSessionForDQ.setWatchCoreCnt(ubiSession.getWatchCoreCnt());
    ubiSessionForDQ.setTrafficSrcId(ubiSession.getTrafficSrcId());
    ubiSessionForDQ.setAbsStartTimestamp(ubiSession.getAbsStartTimestamp());
    ubiSessionForDQ.setAbsEndTimestamp(ubiSession.getAbsEndTimestamp());
    ubiSessionForDQ.setAbsDuration(ubiSession.getAbsDuration());
    ubiSessionForDQ.setCobrand(ubiSession.getCobrand());
    ubiSessionForDQ.setFirstSiteId(ubiSession.getFirstSiteId());
    ubiSessionForDQ.setFirstCguid(ubiSession.getFirstCguid());
    ubiSessionForDQ.setFirstMappedUserId(ubiSession.getFirstMappedUserId());
    ubiSessionForDQ.setEndTimestamp(ubiSession.getEndTimestamp());
    ubiSessionForDQ.setHomepageCnt(ubiSession.getHomepageCnt());
    ubiSessionForDQ.setGrCnt(ubiSession.getGrCnt());
    ubiSessionForDQ.setGr1Cnt(ubiSession.getGr1Cnt());
    ubiSessionForDQ.setMyebayCnt(ubiSession.getMyebayCnt());
    ubiSessionForDQ.setSigninPageCnt(ubiSession.getMyebayCnt());
    ubiSessionForDQ.setSigninPageCnt(ubiSession.getSigninPageCnt());
    ubiSessionForDQ.setNonIframeRdtEventCnt(ubiSession.getNonIframeRdtEventCnt());
    ubiSessionForDQ.setFirstSessionStartDt(ubiSession.getFirstSessionStartDt());
    ubiSessionForDQ.setBotFlagList(new ArrayList<>(ubiSession.getBotFlagList()));
    ubiSessionForDQ.setSingleClickSessionFlag(ubiSession.getSingleClickSessionFlag());
    ubiSessionForDQ.setBidBinConfirmFlag(ubiSession.getBidBinConfirmFlag());
    ubiSessionForDQ.setSessionEndedFlag(ubiSession.getSessionEndedFlag());
    ubiSessionForDQ.setOldSessionSkey(ubiSession.getOldSessionSkey());
    ubiSessionForDQ.setAbsEventCnt(ubiSession.getAbsEventCnt());
    ubiSessionForDQ.setValidPageCnt(ubiSession.getValidPageCnt());
    ubiSessionForDQ.setAgentCnt(ubiSession.getAgentCnt());
    ubiSessionForDQ.setAgentString(ubiSession.getAgentString());
    ubiSessionForDQ.setLndgPageId(ubiSession.getLndgPageId());
    ubiSessionForDQ.setExInternalIp(ubiSession.getExInternalIp());
    ubiSessionForDQ.setFamilyViCnt(ubiSession.getFamilyViCnt());
    ubiSessionForDQ.setPageCnt(ubiSession.getPageCnt());
    ubiSessionForDQ.setSearchCnt(ubiSession.getSearchCnt());
    ubiSessionForDQ.setViewCnt(ubiSession.getViewCnt());
    ubiSessionForDQ.setRefererNull(ubiSession.isRefererNull());
    ubiSessionForDQ.setSiidCnt2(ubiSession.getSiidCnt2());
    ubiSessionForDQ.setViCnt(ubiSession.getViCnt());
    ubiSessionForDQ.setAgentInfo(ubiSession.getAgentInfo());
    ubiSessionForDQ.setClientIp(ubiSession.getClientIp());
    ubiSessionForDQ.setFindFirst(ubiSession.isFindFirst());
    ubiSessionForDQ.setInternalIp(ubiSession.getInternalIp());
    ubiSessionForDQ.setExternalIp(ubiSession.getExternalIp());
    ubiSessionForDQ.setExternalIp2(ubiSession.getExternalIp2());
    ubiSessionForDQ.setAppId(ubiSession.getAppId());
    ubiSessionForDQ.setSiidCnt(ubiSession.getSiidCnt());
    ubiSessionForDQ.setMaxScsSeqNum(ubiSession.getMaxScsSeqNum());
    ubiSessionForDQ.setFirstCobrand(ubiSession.getFirstCobrand());
    ubiSessionForDQ.setMinSCSeqNum(ubiSession.getMinSCSeqNum());
    ubiSessionForDQ.setSeqNum(ubiSession.getSeqNum());
    return ubiSessionForDQ;
  }
}
