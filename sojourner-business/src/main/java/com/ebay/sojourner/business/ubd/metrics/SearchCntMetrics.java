package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class SearchCntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setViewCnt(0);
    sessionAccumulator.getUbiSession().setSearchCnt(0);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    int staticPageType = event.getStaticPageType();
    if (staticPageType == 2) {
      sessionAccumulator
          .getUbiSession()
          .setSearchCnt(sessionAccumulator.getUbiSession().getSearchCnt() + 1);
    } else if (staticPageType == 3) {
      sessionAccumulator
          .getUbiSession()
          .setViewCnt(sessionAccumulator.getUbiSession().getViewCnt() + 1);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
  }
}
