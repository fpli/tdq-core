package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiColsMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setClickId(Integer.MAX_VALUE);
    sessionAccumulator.getUbiSession().setPageIdForUAIP(Integer.MAX_VALUE);
    sessionAccumulator.getUbiSession().setHashCode(Integer.MAX_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getAbsStartTimestamp() != null
        && event.getEventTimestamp().equals(
            sessionAccumulator.getUbiSession().getAbsStartTimestamp())) {
      if (event.getClickId() < sessionAccumulator.getUbiSession().getClickId()) {
        sessionAccumulator.getUbiSession().setClickId(event.getClickId());
        sessionAccumulator.getUbiSession().setPageIdForUAIP(event.getPageId());
        sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
      } else if (event.getClickId() == sessionAccumulator.getUbiSession().getClickId()) {
        if (event.getPageId() < sessionAccumulator.getUbiSession().getPageIdForUAIP()) {
          sessionAccumulator.getUbiSession().setPageIdForUAIP(event.getPageId());
          sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
        } else if (event.getPageId() == sessionAccumulator.getUbiSession().getPageIdForUAIP()) {
          if (event.getHashCode() < sessionAccumulator.getUbiSession().getHashCode()) {
            sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
          }
        }
      }
    } else if (sessionAccumulator.getUbiSession().getAbsStartTimestamp() == null
              || event.getEventTimestamp() < sessionAccumulator.getUbiSession()
                                                               .getAbsStartTimestamp()) {
      sessionAccumulator.getUbiSession().setClickId(event.getClickId());
      sessionAccumulator.getUbiSession().setPageIdForUAIP(event.getPageId());
      sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    // nothing to do
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
