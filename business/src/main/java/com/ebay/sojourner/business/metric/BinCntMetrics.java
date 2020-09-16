package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.business.parser.PageIndicator;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.FlagUtils;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.UBIConfig;

public class BinCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private PageIndicator indicator;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setBinCoreCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe()
        && !event.isRdt()
        && indicator.isCorrespondingPageEvent(event)
        && (FlagUtils.matchFlag(event, 6, 1) || FlagUtils.matchFlag(event, 48, 1))) {
      sessionAccumulator
          .getUbiSession()
          .setBinCoreCnt(sessionAccumulator.getUbiSession().getBinCoreCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    setPageIndicator(new PageIndicator(UBIConfig.getString(Property.BIN_PAGES)));
  }

  void setPageIndicator(PageIndicator indicator) {
    this.indicator = indicator;
  }
}
