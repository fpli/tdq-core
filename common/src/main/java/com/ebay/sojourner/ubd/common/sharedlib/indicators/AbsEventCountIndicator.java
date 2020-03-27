package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionForGuidEnhancement;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class AbsEventCountIndicator
    extends AbstractIndicator<SessionForGuidEnhancement, GuidAttributeAccumulator> {

  @Override
  public void start(GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {
    guidAttributeAccumulator.getGuidAttribute().clear();
  }

  @Override
  public void feed(
      SessionForGuidEnhancement session, GuidAttributeAccumulator guidAttributeAccumulator,
      boolean isNeeded)
      throws Exception {
    guidAttributeAccumulator
        .getGuidAttribute()
        .feed(session, BotRules.MANY_EVENTS_BOT_FLAG, isNeeded);
  }

  @Override
  public boolean filter(SessionForGuidEnhancement session,
      GuidAttributeAccumulator guidAttributeAccumulator)
      throws Exception {
    return false;
  }
}
