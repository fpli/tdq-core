package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;

public class CguidMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  public void start(SessionAccumulator sessionAccumulator) throws Exception {

    sessionAccumulator.getUbiSession().setFirstCguid(null);
  }

  @Override
  public void feed(UbiEvent ubiEvent, SessionAccumulator sessionAccumulator) throws Exception {
    // AS per the sql, find first not null cguid from valid event
    // comparing the length of cguid to 32, based on the SQL.

    if (sessionAccumulator.getUbiSession().getFirstCguid() == null
        && (!ubiEvent.isIframe() && !ubiEvent.isRdt())) {
      String cGuidTemp = SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "n");
      // checking for NPE
      if (cGuidTemp != null && cGuidTemp.length() == 32) {
        sessionAccumulator.getUbiSession().setFirstCguid(cGuidTemp);
      }
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {}

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
