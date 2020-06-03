package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;

public class FindingFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setFindingFlags(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getFindingFlags() == null
        && event.getBitVal() != Integer.MIN_VALUE) {
      sessionAccumulator.getUbiSession().setFindingFlags((long) event.getBitVal());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getFindingFlags() == null) {
      sessionAccumulator.getUbiSession().setFindingFlags(0L);
    }
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
