package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class IsRVMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setIsReturningVisitor(false);

  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (event.isRv() && !sessionAccumulator.getUbiSession().isReturningVisitor()) {
      sessionAccumulator.getUbiSession().setIsReturningVisitor(true);
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
