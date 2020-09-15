package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class SiteIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setFirstSiteId(Integer.MIN_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    boolean isEarlyValidEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    if ((isEarlyValidEvent ? isEarlyValidEvent
        : sessionAccumulator.getUbiSession().getFirstSiteId() == Integer.MIN_VALUE)
        && !event.isIframe()
        && !event.isRdt()
        && event.getSiteId() != -1) {
      sessionAccumulator.getUbiSession().setFirstSiteId(event.getSiteId());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (!ubiEvent.isIframe()
        && !ubiEvent.isRdt()) {
      ubiSession.setFirstSiteId(ubiEvent.getSiteId());
    }
  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }
}
