package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

public class TimestampMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private PageIndicator indicator;
  private EventListenerContainer eventListenerContainer;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setAbsStartTimestamp(null);
    sessionAccumulator.getUbiSession().setAbsEndTimestamp(null);
    sessionAccumulator.getUbiSession().setStartTimestamp(null);
    sessionAccumulator.getUbiSession().setEndTimestamp(null);

  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe() && (!event.isRdt() || indicator.isCorrespondingPageEvent(event))) {
      if (sessionAccumulator.getUbiSession().getStartTimestamp() == null) {
        sessionAccumulator.getUbiSession().setStartTimestamp(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getStartTimestamp() > event.getEventTimestamp()) {
        sessionAccumulator.getUbiSession().setStartTimestamp(event.getEventTimestamp());

      }
      if (sessionAccumulator.getUbiSession().getEndTimestamp() == null) {
        sessionAccumulator.getUbiSession().setEndTimestamp(event.getEventTimestamp());
      } else if (event.getEventTimestamp() != null
          && sessionAccumulator.getUbiSession().getEndTimestamp() < event.getEventTimestamp()) {
        sessionAccumulator.getUbiSession().setEndTimestamp(event.getEventTimestamp());
      }
    }
    if (sessionAccumulator.getUbiSession().getAbsStartTimestamp() == null) {
      sessionAccumulator.getUbiSession().setAbsStartTimestamp(event.getEventTimestamp());
    } else if (event.getEventTimestamp() != null
        && sessionAccumulator.getUbiSession().getAbsStartTimestamp() > event.getEventTimestamp()) {
      sessionAccumulator.getUbiSession().setAbsStartTimestamp(event.getEventTimestamp());
      System.out.println("event:"+event.getPageId()+" eventtime:"+event.getEventTimestamp());
      eventListenerContainer.onEarlyEventChange(event,sessionAccumulator.getUbiSession());
    }
    if (sessionAccumulator.getUbiSession().getAbsEndTimestamp() == null) {
      sessionAccumulator.getUbiSession().setAbsEndTimestamp(event.getEventTimestamp());
    } else if (event.getEventTimestamp() != null
        && sessionAccumulator.getUbiSession().getAbsEndTimestamp() < event.getEventTimestamp()) {
      sessionAccumulator.getUbiSession().setAbsEndTimestamp(event.getEventTimestamp());
      eventListenerContainer.onLateEventChange(event,sessionAccumulator.getUbiSession());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    sessionAccumulator
        .getUbiSession()
        .setSojDataDt(
            SOJTS2Date.castSojTimestampToDate(
                sessionAccumulator.getUbiSession().getAbsEndTimestamp()));
    // Fix bug HDMIT-3732 to avoid integer result overflow
    int durationSec =
        (sessionAccumulator.getUbiSession().getStartTimestamp() == null
            || sessionAccumulator.getUbiSession().getEndTimestamp() == null)
            ? 0
            : (int)
                ((sessionAccumulator.getUbiSession().getEndTimestamp()
                    - sessionAccumulator.getUbiSession().getStartTimestamp())
                    / 1000000);
    int absDuration =
        (int)
            ((sessionAccumulator.getUbiSession().getAbsEndTimestamp()
                - sessionAccumulator.getUbiSession().getAbsStartTimestamp())
                / 1000000);
    sessionAccumulator.getUbiSession().setDurationSec(durationSec);
    sessionAccumulator.getUbiSession().setAbsDuration(absDuration);
  }

  @Override
  public void init() throws Exception {
    setPageIndicator(new PageIndicator(UBIConfig.getString(Property.SEARCH_VIEW_PAGES)));
    eventListenerContainer=EventListenerContainer.getInstance();
  }

  void setPageIndicator(PageIndicator indicator) {
    this.indicator = indicator;
  }
}
