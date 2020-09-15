package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import org.apache.log4j.Logger;

public class SingleClickFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static final Logger logger = Logger.getLogger(SingleClickFlagMetrics.class);

  @Override
  public void init() throws Exception {
    // nothing to do
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().getDistinctClickIdSet().clear();
    //        feed(event, sessionAccumulator);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe()) {
      Integer clickId = event.getClickId();
      if (!event.isRdt()) {
        if (clickId != null) {
          if (sessionAccumulator.getUbiSession().getDistinctClickIdSet() != null
              && sessionAccumulator.getUbiSession().getDistinctClickIdSet().size() < 10) {
            sessionAccumulator.getUbiSession().getDistinctClickIdSet().add(clickId);
          }
        }
      } else {
        if (clickId != null) {
          sessionAccumulator.getUbiSession().getDistinctClickIdSet().remove(clickId);
        }
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getDistinctClickIdSet().size() == 1) {
      sessionAccumulator.getUbiSession().setSingleClickSessionFlag(true);
    } else if (sessionAccumulator.getUbiSession().getDistinctClickIdSet().size() == 0) {
      sessionAccumulator.getUbiSession().setSingleClickSessionFlag(null);
    } else {
      sessionAccumulator.getUbiSession().setSingleClickSessionFlag(false);
    }
  }
}
