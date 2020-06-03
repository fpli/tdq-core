package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.business.ubd.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

public class EventCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private PageIndicator indicator;

  // botrule10 will reuse eventcnt but there is some different between this and botrule10's
  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setAbsEventCnt(0);
    sessionAccumulator.getUbiSession().setEventCnt(0);
    sessionAccumulator.getUbiSession().setNonIframeRdtEventCnt(0);
    sessionAccumulator.getUbiSession().setSearchViewPageCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    sessionAccumulator
        .getUbiSession()
        .setAbsEventCnt(sessionAccumulator.getUbiSession().getAbsEventCnt() + 1);
    if (!event.isIframe()) {
      if (!event.isRdt()) {
        sessionAccumulator.getUbiSession()
            .setEventCnt(sessionAccumulator.getUbiSession().getEventCnt() + 1);
        sessionAccumulator.getUbiSession().setNonIframeRdtEventCnt(
            sessionAccumulator.getUbiSession().getNonIframeRdtEventCnt() + 1);
        sessionAccumulator.getUbiSession()
            .setSearchViewPageCnt(sessionAccumulator.getUbiSession().getSearchViewPageCnt() + 1);
      } else if (indicator.isCorrespondingPageEvent(event)) {
        sessionAccumulator.getUbiSession()
            .setEventCnt(sessionAccumulator.getUbiSession().getEventCnt() + 1);
        sessionAccumulator.getUbiSession()
            .setSearchViewPageCnt(sessionAccumulator.getUbiSession().getSearchViewPageCnt() + 1);
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    setPageIndicator(new PageIndicator(UBIConfig.getString(Property.SEARCH_VIEW_PAGES)));
  }

  void setPageIndicator(PageIndicator indicator) {
    this.indicator = indicator;
  }
}
