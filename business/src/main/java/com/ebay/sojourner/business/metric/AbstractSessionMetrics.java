package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public abstract class AbstractSessionMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void init() throws Exception {
    // default empty implementation
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    // default empty implementation
  }

  @Override
  public void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator) throws Exception {
    // default empty implementation
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
    // default empty implementation
  }
}
