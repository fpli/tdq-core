package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionDwellMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void init() throws Exception {
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    Long[] minMaxEventTimestamp = new Long[]{Long.MAX_VALUE, Long.MIN_VALUE};
    sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(minMaxEventTimestamp);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    long eventTimestamp = event.getEventTimestamp();
    if (!event.isIframe() && !event.isRdt()) {
      Long[] minMaxEventTimestamp = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
      if (minMaxEventTimestamp[0] > eventTimestamp) {
        minMaxEventTimestamp[0] = eventTimestamp;
      }
      if (minMaxEventTimestamp[1] < eventTimestamp) {
        minMaxEventTimestamp[1] = eventTimestamp;
      }
      sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(minMaxEventTimestamp);
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }
}
