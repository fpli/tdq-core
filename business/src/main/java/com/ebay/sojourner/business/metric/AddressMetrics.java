package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojEventTimeUtil;

public class AddressMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  @Override
  public void init() throws Exception {

  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setCity(null);
    sessionAccumulator.getUbiSession().setRegion(null);
    sessionAccumulator.getUbiSession().setCountry(null);
    sessionAccumulator.getUbiSession().setContinent(null);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    boolean isEarlyValidEvent = SojEventTimeUtil.isEarlyEvent(event.getEventTimestamp(),
        sessionAccumulator.getUbiSession().getStartTimestampNOIFRAMERDT());
    if (!event.isIframe() && !event.isRdt()) {
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getCity() == null)) {
        sessionAccumulator.getUbiSession().setCity(event.getCity());
      }
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getRegion() == null)) {
        sessionAccumulator.getUbiSession().setRegion(event.getRegion());
      }
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getCountry() == null)) {
        sessionAccumulator.getUbiSession().setCountry(event.getCountry());
      }
      if ((isEarlyValidEvent || sessionAccumulator.getUbiSession().getContinent() == null)) {
        sessionAccumulator.getUbiSession().setContinent(event.getContinent());
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

  }
}
