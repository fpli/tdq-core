package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.util.TransformUtil;
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
    return intermediateSession;
  }
}
