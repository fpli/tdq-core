package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class AppIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setFirstAppId(null);
    sessionAccumulator.getUbiSession().setAppId(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    boolean isEarlyEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getAbsStartTimestamp());
    boolean isEarlyValidEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    if ((isEarlyEvent ? isEarlyEvent : sessionAccumulator.getUbiSession().getFirstAppId() == null)
        && event.getAppId() != null) {
      sessionAccumulator.getUbiSession().setFirstAppId(event.getAppId());
    }
    if ((isEarlyValidEvent ? isEarlyValidEvent
        : sessionAccumulator.getUbiSession().getAppId() == null)
        && !event.isIframe()
        && !event.isRdt()
        && event.getAppId() != null) {
      sessionAccumulator.getUbiSession().setAppId(event.getAppId());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getAppId() != null) {
      sessionAccumulator
          .getUbiSession()
          .setFirstAppId(sessionAccumulator.getUbiSession().getAppId());
    }
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
