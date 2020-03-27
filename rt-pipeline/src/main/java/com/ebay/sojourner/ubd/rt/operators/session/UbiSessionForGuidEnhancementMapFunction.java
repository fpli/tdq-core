package com.ebay.sojourner.ubd.rt.operators.session;

import com.ebay.sojourner.ubd.common.model.SessionForGuidEnhancement;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.util.TransformUtil;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

public class UbiSessionForGuidEnhancementMapFunction extends
    RichMapFunction<UbiSession, SessionForGuidEnhancement> {

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public SessionForGuidEnhancement map(UbiSession ubiSession) throws Exception {
    SessionForGuidEnhancement sessionForGuidEnhancement = new SessionForGuidEnhancement();
    Long[] guidEehance = TransformUtil.stringToLong(ubiSession.getGuid());
    sessionForGuidEnhancement.setGuid1(guidEehance[0]);
    sessionForGuidEnhancement.setGuid2(guidEehance[1]);
    sessionForGuidEnhancement.setAbsEventCnt(ubiSession.getAbsEventCnt());
    return sessionForGuidEnhancement;
  }
}
