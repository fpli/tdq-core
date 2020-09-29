package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class BrowserMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void init() throws Exception {

  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setBrowserFamily(null);
    sessionAccumulator.getUbiSession().setBrowserVersion(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    boolean isEarlyValidEvent = SojEventTimeUtil.isEarlyEvent(event.getEventTimestamp(),
        sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    if (!event.isIframe() && !event.isRdt()) {
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getBrowserFamily() == null)) {
        sessionAccumulator.getUbiSession().setBrowserFamily(event.getBrowserFamily());
      }
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getBrowserVersion() == null)) {
        sessionAccumulator.getUbiSession().setBrowserVersion(event.getBrowserVersion());
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }
}
