package com.ebay.sojourner.business.ubd.indicators;

import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotRules;

public class AbsEventCountIndicator
    extends AbstractIndicator<SessionCore, GuidAttributeAccumulator> {

  @Override
  public void start(GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {
    guidAttributeAccumulator.getGuidAttribute().clear();
  }

  @Override
  public void feed(
      SessionCore sessionCore, GuidAttributeAccumulator guidAttributeAccumulator)
      throws Exception {
    guidAttributeAccumulator
        .getGuidAttribute()
        .feed(sessionCore, BotRules.MANY_EVENTS_BOT_FLAG);
  }

  @Override
  public boolean filter(SessionCore sessionCore,
      GuidAttributeAccumulator guidAttributeAccumulator)
      throws Exception {
    return false;
  }
}
