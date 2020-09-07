package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.Calendar;
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
    if (sessionAccumulator.getUbiSession().getAbsStartTimestamp() != null &&
        event.getEventTimestamp()
            .equals(sessionAccumulator.getUbiSession().getAbsStartTimestamp())) {
      if (sessionAccumulator.getUbiSession().getGuid() != null) {
        log.debug(
            Calendar.getInstance().getTime() + " debug MultiColsMetrics2 duplicate event==session:"
                + sessionAccumulator.getUbiSession()
                .getGuid() + " "
                + sessionAccumulator.getUbiSession()
                .getAbsStartTimestamp() + " " + sessionAccumulator.getUbiSession()
                .getClickId() + " " + sessionAccumulator.getUbiSession().getPageIdForUAIP() + " "
                + sessionAccumulator.getUbiSession().getHashCode());
        log.debug(Calendar.getInstance().getTime() +
            " debug MultiColsMetrics2 duplicate event==event:" + event.getGuid() + " " + event
            .getEventTimestamp() + " "
            + event
            .getClickId() + " " + event.getPageId() + " " + event.getHashCode());
      }
      if (event.getClickId() < sessionAccumulator.getUbiSession()
          .getClickId()) {
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
    } else if (sessionAccumulator.getUbiSession().getAbsStartTimestamp() == null) {
      sessionAccumulator.getUbiSession().setClickId(event.getClickId());
      sessionAccumulator.getUbiSession().setPageIdForUAIP(event.getPageId());
      sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
    } else if (event.getEventTimestamp() < sessionAccumulator.getUbiSession()
        .getAbsStartTimestamp()) {
      sessionAccumulator.getUbiSession().setClickId(event.getClickId());
      sessionAccumulator.getUbiSession().setPageIdForUAIP(event.getPageId());
      sessionAccumulator.getUbiSession().setHashCode(event.getHashCode());
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
