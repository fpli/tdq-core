package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

public class UserIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setFirstUserId(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getFirstUserId() == null && event.getUserId() != null) {
      sessionAccumulator.getUbiSession().setFirstUserId(event.getUserId());
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
