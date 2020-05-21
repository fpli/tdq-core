package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.util.SojEventTimeUtil;

public class DeviceMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  @Override
  public void init() throws Exception {

  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setDeviceClass(null);
    sessionAccumulator.getUbiSession().setDeviceFamily(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    boolean isEarlyValidEvent = SojEventTimeUtil.isEarlyEvent(event.getEventTimestamp(),
        sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    if (!event.isIframe() && !event.isRdt()) {
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getDeviceClass() == null)) {
        sessionAccumulator.getUbiSession().setDeviceClass(event.getDeviceType());
      }
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getDeviceFamily() == null)) {
        sessionAccumulator.getUbiSession().setDeviceFamily(event.getDeviceFamily());
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (!ubiEvent.isIframe() && !ubiEvent.isRdt()) {
      if (ubiEvent.getOsFamily() != null) {
        ubiSession.setDeviceFamily(ubiEvent.getDeviceFamily());
      }
      if (ubiEvent.getOsVersion() != null) {
        ubiSession.setDeviceClass(ubiEvent.getDeviceType());
      }
    }
  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }
}
