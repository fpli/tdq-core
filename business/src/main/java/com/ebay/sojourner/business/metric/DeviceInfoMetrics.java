package com.ebay.sojourner.business.metric;

import static com.ebay.sojourner.common.util.Constants.DEFAULT_PAGE_ID;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class DeviceInfoMetrics extends AbstractSessionMetrics {

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setBrowserFamily(null);
    sessionAccumulator.getUbiSession().setBrowserVersion(null);
    sessionAccumulator.getUbiSession().setDeviceFamily(null);
    sessionAccumulator.getUbiSession().setDeviceClass(null);
    sessionAccumulator.getUbiSession().setOsFamily(null);
    sessionAccumulator.getUbiSession().setOsVersion(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    UbiSession ubiSession = sessionAccumulator.getUbiSession();
    boolean isEarlyValidEvent = SojEventTimeUtil.isEarlyEvent(
        event.getEventTimestamp(), ubiSession.getStartTimestampNOIFRAMERDT());

    boolean isEarlyEvent = SojEventTimeUtil.isEarlyEvent(
        event.getEventTimestamp(), ubiSession.getAbsStartTimestamp());

    // get first event's attribute
    if (ubiSession.getAbsEventCnt() == 1) {
      setMetrics(ubiSession, event);
    } else if (ubiSession.getAbsEventCnt() > 1 && isEarlyValidEvent) {
      if (!event.isIframe() && !event.isRdt() && event.getPageId() != DEFAULT_PAGE_ID
          || isEarlyEvent) {
        setMetrics(ubiSession, event);
      }
    }
  }

  private void setMetrics(UbiSession ubiSession, UbiEvent event) {
    ubiSession.setBrowserFamily(event.getBrowserFamily());
    ubiSession.setBrowserVersion(event.getBrowserVersion());
    ubiSession.setDeviceFamily(event.getDeviceFamily());
    ubiSession.setDeviceClass(event.getDeviceType());
    ubiSession.setOsFamily(event.getOsFamily());
    ubiSession.setOsVersion(event.getOsVersion());
  }
}
