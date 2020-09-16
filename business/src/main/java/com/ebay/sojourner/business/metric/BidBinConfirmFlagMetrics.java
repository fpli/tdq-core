package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class BidBinConfirmFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    // nothing to do
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    // nothing to do
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    sessionAccumulator
        .getUbiSession()
        .setBidBinConfirmFlag(
            sessionAccumulator.getUbiSession().getBidCoreCnt() > 0
                || sessionAccumulator.getUbiSession().getBinCoreCnt() > 0);
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
