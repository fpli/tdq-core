package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.sharedlib.util.SojEventTimeUtil;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

public class PageIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  private PageIndicator indicator = null;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setStartPageId(Integer.MIN_VALUE);
    sessionAccumulator.getUbiSession().setEndPageId(Integer.MIN_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    boolean isEarlyEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getAbsStartTimestamp());
    boolean isLateEvent=SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getAbsEndTimestamp());
    if (!event.isIframe()) {
      if (!event.isRdt() || indicator.isCorrespondingPageEvent(event)) {
        if (sessionAccumulator.getUbiSession().getStartPageId() == Integer.MIN_VALUE
            || isEarlyEvent) {
          sessionAccumulator.getUbiSession().setStartPageId(event.getPageId());
        }
        if(isLateEvent) {
          sessionAccumulator.getUbiSession().setEndPageId(event.getPageId());
        }
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getStartPageId() != Integer.MIN_VALUE) {

    } else {
      sessionAccumulator.getUbiSession().setStartPageId(0);
    }
    if (sessionAccumulator.getUbiSession().getEndPageId() != Integer.MIN_VALUE) {

    } else {
      sessionAccumulator.getUbiSession().setEndPageId(0);
    }
  }

  @Override
  public void init() throws Exception {
    setPageIndicator(new PageIndicator(UBIConfig.getString(Property.SEARCH_VIEW_PAGES)));
  }

  void setPageIndicator(PageIndicator indicator) {
    this.indicator = indicator;
  }

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (!ubiEvent.isIframe()) {
      if (!ubiEvent.isRdt() || indicator.isCorrespondingPageEvent(ubiEvent)) {
        ubiSession.setStartPageId(ubiEvent.getPageId());
      }
    }
  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {
    if (!ubiEvent.isIframe()) {
      if (!ubiEvent.isRdt() || indicator.isCorrespondingPageEvent(ubiEvent)) {
        ubiSession.setEndPageId(ubiEvent.getPageId());
      }
    }
  }
}
