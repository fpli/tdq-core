package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class SiidCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setSiidCnt(0);
    sessionAccumulator.getUbiSession().setSiidCnt2(0);
    sessionAccumulator.getUbiSession().setRefererNull(true);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe() && !event.isRdt() && event.getSourceImprId() != null) {
      sessionAccumulator
          .getUbiSession()
          .setSiidCnt(sessionAccumulator.getUbiSession().getSiidCnt() + 1);
    }
    if (!event.isIframe() && !event.isRdt() && sessionAccumulator.getUbiSession().isRefererNull()) {
      if (event.getSourceImprId() != null) {
        sessionAccumulator
            .getUbiSession()
            .setSiidCnt2(sessionAccumulator.getUbiSession().getSiidCnt2() + 1);
      }
      if (event.getReferrer() != null) {
        sessionAccumulator.getUbiSession().setRefererNull(false);
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
  }
}
