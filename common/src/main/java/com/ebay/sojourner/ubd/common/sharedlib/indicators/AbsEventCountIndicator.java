package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class AbsEventCountIndicator
    extends AbstractIndicator<SessionCore, GuidAttributeAccumulator> {

  @Override
  public void start(GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {
    guidAttributeAccumulator.getGuidAttribute().clear();
  }

  @Override
  public void feed(
      SessionCore intermediateSession, GuidAttributeAccumulator guidAttributeAccumulator,
      boolean isNeeded)
      throws Exception {
    guidAttributeAccumulator
        .getGuidAttribute()
        .feed(intermediateSession, BotRules.MANY_EVENTS_BOT_FLAG, isNeeded);
  }

  @Override
  public boolean filter(SessionCore intermediateSession,
      GuidAttributeAccumulator guidAttributeAccumulator)
      throws Exception {
    return false;
  }
}
