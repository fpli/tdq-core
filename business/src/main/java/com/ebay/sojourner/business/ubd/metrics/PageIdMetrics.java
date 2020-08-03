package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.business.ubd.parser.PageIndicator;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.SojEventTimeUtil;
import com.ebay.sojourner.common.util.UBIConfig;

public class PageIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  private PageIndicator indicator = null;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setStartPageId(Integer.MIN_VALUE);
    sessionAccumulator.getUbiSession().setEndPageId(Integer.MIN_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    boolean isEarlyValidEvent = SojEventTimeUtil
        .isEarlyEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getStartTimestamp());
    boolean isLateEvent = SojEventTimeUtil
        .isLateEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getAbsEndTimestamp());
    boolean isLateValidEvent = SojEventTimeUtil
        .isLateEvent(event.getEventTimestamp(),
            sessionAccumulator.getUbiSession().getEndTimestamp());
    if (!event.isIframe()) {
      if (!event.isRdt() || indicator.isCorrespondingPageEvent(event)) {
        if (sessionAccumulator.getUbiSession().getStartPageId() == Integer.MIN_VALUE
            || isEarlyValidEvent) {
          if (event.getPageId() != -1) {
            sessionAccumulator.getUbiSession().setStartPageId(event.getPageId());
          }
        }
        if (isLateValidEvent) {
          if (event.getPageId() != -1) {
            sessionAccumulator.getUbiSession().setEndPageId(event.getPageId());
          }
        }
      }
    }
    if (isLateEvent) {

      sessionAccumulator.getUbiSession().setPageId(event.getPageId());

    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getStartPageId() == Integer.MIN_VALUE) {
      sessionAccumulator.getUbiSession().setStartPageId(0);
    }
    if (sessionAccumulator.getUbiSession().getEndPageId() == Integer.MIN_VALUE) {
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
