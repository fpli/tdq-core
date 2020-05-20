package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.util.SojEventTimeUtil;

public class OsMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  @Override
  public void init() throws Exception {

  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setOsFamily(null);
    sessionAccumulator.getUbiSession().setOsVersion(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    boolean isEarlyValidEvent = SojEventTimeUtil.isEarlyEvent(event.getEventTimestamp(),
        sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getOsFamily() == null)
        && !event.isIframe()
        && !event.isRdt()) {
      sessionAccumulator.getUbiSession().setOsFamily(event.getOsFamily());
    }
    if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getOsVersion() == null)
        && !event.isIframe()
        && !event.isRdt()) {
      sessionAccumulator.getUbiSession().setOsVersion(event.getOsVersion());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (ubiEvent.getOsFamily() != null) {
      ubiSession.setOsFamily(ubiEvent.getOsFamily());
    }
    if (ubiEvent.getOsVersion() != null) {
      ubiSession.setOsVersion(ubiEvent.getOsVersion());
    }
  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }
}
