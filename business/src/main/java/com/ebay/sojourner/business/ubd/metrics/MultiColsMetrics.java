package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class MultiColsMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setClickId(Integer.MAX_VALUE);
    sessionAccumulator.getUbiSession().setPageId(Integer.MAX_VALUE);
    sessionAccumulator.getUbiSession().setHashCode(Integer.MAX_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if(event.getEventTimestamp()==sessionAccumulator.getUbiSession().getAbsStartTimestamp()) {
      if (event.getClickId() < sessionAccumulator.getUbiSession()
          .getClickId()) {
        sessionAccumulator.getUbiSession().setClickId(event.getClickId());
        sessionAccumulator.getUbiSession().setPageId(event.getPageId());
        sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
      } else if (event.getClickId() == sessionAccumulator.getUbiSession().getClickId()) {
        if (event.getPageId() < sessionAccumulator.getUbiSession().getPageId()) {
          sessionAccumulator.getUbiSession().setPageId(event.getPageId());
          sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
        } else if (event.getPageId() == sessionAccumulator.getUbiSession().getPageId()) {
          if (event.getHashCode() < sessionAccumulator.getUbiSession().getHashCode()) {
            sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
          }
        }

      }
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
