package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class ServEventCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void init() throws Exception {
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setServEventCnt(0);
  }

  @Override
  public void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator) throws Exception {
    if (!ubiEvent.isIframe()
        && !ubiEvent.isRdt()
        && ubiEvent.getPageId() != -1) {
      sessionAccumulator.getUbiSession()
          .setServEventCnt(sessionAccumulator.getUbiSession().getServEventCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }
}
