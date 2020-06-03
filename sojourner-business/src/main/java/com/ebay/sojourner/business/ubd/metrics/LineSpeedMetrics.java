package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.SojEventTimeUtil;

public class LineSpeedMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  @Override
  public void init() throws Exception {

  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setLineSpeed(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    boolean isEarlyValidEvent = SojEventTimeUtil.isEarlyEvent(event.getEventTimestamp(),
        sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getLineSpeed() == null)
        && !event.isIframe()
        && !event.isRdt()) {
      sessionAccumulator.getUbiSession().setLineSpeed(event.getLineSpeed());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (!ubiEvent.isIframe() && !ubiEvent.isRdt()) {
      if (ubiEvent.getLineSpeed() != null) {
        ubiSession.setLineSpeed(ubiEvent.getLineSpeed());
      }
    }
  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }
}
