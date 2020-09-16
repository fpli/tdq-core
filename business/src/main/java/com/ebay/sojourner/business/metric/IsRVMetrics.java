package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;

public class IsRVMetrics implements FieldMetrics<UbiEvent, SessionAccumulator>, EventListener {

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setIsReturningVisitor(false);

  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (event.isRv() && !sessionAccumulator.getUbiSession().isReturningVisitor()) {
      sessionAccumulator.getUbiSession().setIsReturningVisitor(true);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {

  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }

  @Override
  public void onEarlyEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }

  @Override
  public void onLateEventChange(UbiEvent ubiEvent, UbiSession ubiSession) {

  }
}
