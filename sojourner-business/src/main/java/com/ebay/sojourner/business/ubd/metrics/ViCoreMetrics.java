package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.business.ubd.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.FlagUtils;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

public class ViCoreMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private PageIndicator indicator;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setViCoreCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (event.getItemId() != null
        && !event.isIframe()
        && !event.isRdt()
        && indicator.isCorrespondingPageEvent(event)
        && FlagUtils.matchFlag(event, 28, 0)) {
      sessionAccumulator
          .getUbiSession()
          .setViCoreCnt(sessionAccumulator.getUbiSession().getViCoreCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    setPageIndicator(new PageIndicator(UBIConfig.getString(Property.VIEW_ITEM_PAGES)));
  }

  void setPageIndicator(PageIndicator indicator) {
    this.indicator = indicator;
  }
}
