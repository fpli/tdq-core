package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.business.parser.PageIndicator;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.FlagUtils;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.UBIConfig;

public class BidCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static PageIndicator indicator;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setBidCoreCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe()
        && !event.isRdt()
        && indicator.isCorrespondingPageEvent(event)
        && FlagUtils.matchFlag(event, 3, 1)) {
      sessionAccumulator
          .getUbiSession()
          .setBidCoreCnt(sessionAccumulator.getUbiSession().getBidCoreCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    setPageIndicator(new PageIndicator(UBIConfig.getString(Property.BID_PAGES)));
  }

  void setPageIndicator(PageIndicator indicator) {
    BidCntMetrics.indicator = indicator;
  }
}
