package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.util.SojEventTimeUtil;

public class ReferrerMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setSessionReferrer(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {

    // FIXME seems logic is correct, but some result is  incorrect;
    boolean isEarlyEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getStartTimestampForReferrer());
    if ((isEarlyEvent ? isEarlyEvent
        : sessionAccumulator.getUbiSession().getSessionReferrer() == null)
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

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (ubiEvent.getReferrer() != null) {
      ubiSession.setSessionReferrer(ubiEvent.getReferrer());
    }
  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }
}
