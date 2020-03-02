package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionDwellMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private Long[] minMaxEventTimestamp;
  private Integer seqNum;

  @Override
  public void init() throws Exception {}

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    minMaxEventTimestamp = new Long[] {Long.MAX_VALUE, Long.MIN_VALUE};
    sessionAccumulator.getUbiSession().setMinMaxEventTimestamp(minMaxEventTimestamp);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    long eventTimestamp = event.getEventTimestamp();
    if (!event.isIframe() && !event.isRdt()) {
      minMaxEventTimestamp = sessionAccumulator.getUbiSession().getMinMaxEventTimestamp();
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
  public void end(SessionAccumulator sessionAccumulator) {}
}
