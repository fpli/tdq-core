package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class ReferrerMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

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
}
