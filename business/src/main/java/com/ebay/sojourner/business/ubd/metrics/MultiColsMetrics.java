package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class MultiColsMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setClickId(Integer.MAX_VALUE);
    sessionAccumulator.getUbiSession().setPageId(Integer.MAX_VALUE);
    sessionAccumulator.getUbiSession().setHashCode(Integer.MAX_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (event.getClickId() != -1 && event.getClickId() < sessionAccumulator.getUbiSession()
        .getClickId()) {
      sessionAccumulator.getUbiSession().setClickId(event.getClickId());
    }
    if (event.getPageId() != -1 && event.getPageId() < sessionAccumulator.getUbiSession()
        .getPageId()) {
      sessionAccumulator.getUbiSession().setPageId(event.getPageId());
    }
    if (event.getHashCode() != -1 && event.getHashCode() < sessionAccumulator.getUbiSession()
        .getHashCode()) {
      sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
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
