package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.PropertyUtils;
import com.ebay.sojourner.common.util.UBIConfig;
import java.util.Set;

public class MaxScsSeqNumMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static Set<Integer> invalidPageIds;

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().setMaxScsSeqNum(Integer.MIN_VALUE);
  }

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    if (!event.isIframe() && !event.isRdt() && !invalidPageIds.contains(event.getPageId())) {
      if (event.getSeqNum() > sessionAccumulator.getUbiSession().getMaxScsSeqNum()) {
        sessionAccumulator.getUbiSession().setMaxScsSeqNum(event.getSeqNum());
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
  }

  @Override
  public void init() throws Exception {
    invalidPageIds =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.INVALID_PAGE_IDS), Property.PROPERTY_DELIMITER);
  }
}
