package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.apache.flink.api.common.functions.RichMapFunction;

public class UbiSessionToSojSessionMapFunction extends RichMapFunction<UbiSession, SojSession> {
  @Override
  public SojSession map(UbiSession ubiSession) throws Exception {
    SojSession sojSession = new SojSession();
    sojSession.setGuid(ubiSession.getGuid());
    //        sojSession.setAgentString(ubiSession.getAgentString());
    sojSession.setSessionId(ubiSession.getSessionId());
    sojSession.setIp(ubiSession.getIp());
    sojSession.setUserAgent(sojSession.getUserAgent());
    //        sojSession.setExInternalIp(sessionAccumulator.getUbiSession().getExInternalIp());
    sojSession.setSojDataDt(sojSession.getSojDataDt());
    sojSession.setSessionStartDt(sojSession.getSessionStartDt());
    //        sojSession.setAgentCnt(sojSession.getAgentCnt());
    sojSession.setStartTimestamp(ubiSession.getStartTimestamp());
    sojSession.setEndTimestamp(ubiSession.getEndTimestamp());
    sojSession.setAbsStartTimestamp(ubiSession.getAbsStartTimestamp());
    sojSession.setAbsEndTimestamp(ubiSession.getAbsEndTimestamp());
    //        sojSession.setClientIp(sessionAccumulator.getUbiSession().getClientIp());
    //        sojSession.setInternalIp(sessionAccumulator.getUbiSession().getInternalIp());
    //
    // sojSession.setSingleClickSessionFlag(sessionAccumulator.getUbiSession().
    // getSingleClickSessionFlag());
    sojSession.setBotFlagList(ubiSession.getBotFlagList());
    sojSession.setNonIframeRdtEventCnt(ubiSession.getNonIframeRdtEventCnt());
    //
    // sojSession.setSingleClickSessionFlag(sessionAccumulator.getUbiSession().
    // getSingleClickSessionFlag());
    sojSession.setSessionReferrer(ubiSession.getSessionReferrer());
    sojSession.setBotFlag(ubiSession.getBotFlag());
    sojSession.setVersion(ubiSession.getVersion());
    sojSession.setFirstUserId(ubiSession.getFirstUserId());
    //        sojSession.setSiteFlagsSet(sessionAccumulator.getUbiSession().getSiteFlagsSet());
    sojSession.setSiteFlags(ubiSession.getSiteFlags());
    sojSession.setAttrFlags(ubiSession.getAttrFlags());
    sojSession.setBotFlags(ubiSession.getBotFlags());
    sojSession.setFindingFlags(ubiSession.getFindingFlags());
    sojSession.setStartPageId(ubiSession.getStartPageId());
    sojSession.setEndPageId(ubiSession.getEndPageId());
    sojSession.setDurationSec(ubiSession.getDurationSec());
    return sojSession;
  }
}
