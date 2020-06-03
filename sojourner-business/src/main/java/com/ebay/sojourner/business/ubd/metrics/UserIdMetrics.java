package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.util.SojEventTimeUtil;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;

public class UserIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setFirstUserId(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    boolean isEarlyEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getAbsStartTimestamp());
    if ((isEarlyEvent ? isEarlyEvent : sessionAccumulator.getUbiSession().getFirstUserId() == null)
        && event.getUserId() != null) {
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

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (ubiEvent.getUserId() != null) {
      ubiSession.setFirstUserId(ubiEvent.getUserId());
    }
  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }
}
