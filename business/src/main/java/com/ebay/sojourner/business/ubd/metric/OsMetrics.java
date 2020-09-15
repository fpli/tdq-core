package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class OsMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

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

    UbiSession ubiSession = sessionAccumulator.getUbiSession();
    if (ubiSession.getAbsEventCnt() == 1) {
      ubiSession.setOsVersion(event.getEnrichedOsVersion());
      ubiSession.setOsFamily(event.getOsFamily());
    } else if (ubiSession.getAbsEventCnt() > 1) {
      boolean isEarlyEvent = SojEventTimeUtil
          .isEarlyEvent(event.getEventTimestamp(),
              sessionAccumulator.getUbiSession().getAbsStartTimestamp());
      boolean isEarlyValidEvent = SojEventTimeUtil
          .isEarlyEvent(event.getEventTimestamp(),
              sessionAccumulator.getUbiSession().getStartTimestamp());
      if (isEarlyEvent) {
        if (!ubiSession.isFindFirstForOs()) {
          ubiSession.setOsVersion(event.getEnrichedOsVersion());
          ubiSession.setOsFamily(event.getOsFamily());
        }

      }
      if (isEarlyValidEvent) {
        if (!event.isIframe() && !event.isRdt() && event.getPageId() != -1) {
          ubiSession.setOsVersion(event.getEnrichedOsVersion());
          ubiSession.setOsFamily(event.getOsFamily());
          ubiSession.setFindFirstForOs(true);
        }
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }
}
