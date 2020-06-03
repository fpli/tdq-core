package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.Set;

public class CobrandMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private Set<Integer> invalidPageIds;

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
    sessionAccumulator.getUbiSession().setFirstCobrand(Integer.MIN_VALUE);
    sessionAccumulator.getUbiSession().setCobrand(Integer.MIN_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
    if (sessionAccumulator.getUbiSession().getCobrand() == Integer.MIN_VALUE
        && !event.isIframe()
        && !event.isRdt()
        && !invalidPageIds.contains(event.getPageId())) {
      sessionAccumulator.getUbiSession().setCobrand(event.getCobrand());
    }
    if (sessionAccumulator.getUbiSession().getFirstCobrand() == Integer.MIN_VALUE) {
      sessionAccumulator.getUbiSession().setFirstCobrand(event.getCobrand());
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {
    if (sessionAccumulator.getUbiSession().getCobrand() == Integer.MIN_VALUE) {
      sessionAccumulator
          .getUbiSession()
          .setCobrand(sessionAccumulator.getUbiSession().getFirstCobrand());
    }
  }

  @Override
  public void init() throws Exception {
    invalidPageIds =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.INVALID_PAGE_IDS), Property.PROPERTY_DELIMITER);
  }
}
