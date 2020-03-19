package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

public class ReferrerMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setSessionReferrer(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getSessionReferrer() == null
        && event.getReferrer() != null) {
      sessionAccumulator.getUbiSession().setSessionReferrer(event.getReferrer());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
