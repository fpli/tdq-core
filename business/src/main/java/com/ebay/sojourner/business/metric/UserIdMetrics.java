package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class UserIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setFirstUserId(null);
    sessionAccumulator.getUbiSession().setBuserId(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    boolean isEarlyEvent = SojEventTimeUtil.isEarlyEvent(
        event.getEventTimestamp(), sessionAccumulator.getUbiSession().getAbsStartTimestamp());

    if ((isEarlyEvent || sessionAccumulator.getUbiSession().getFirstUserId() == null)
        && event.getUserId() != null) {
      sessionAccumulator.getUbiSession().setFirstUserId(event.getUserId());
    }

    if ((isEarlyEvent || sessionAccumulator.getUbiSession().getBuserId() == null)
        && event.getBuserId() != null) {
      sessionAccumulator.getUbiSession().setBuserId(event.getBuserId());
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
