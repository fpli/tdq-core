package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class AbsEventCountIndicator
    extends AbstractIndicator<UbiSession, GuidAttributeAccumulator> {

  @Override
  public void start(GuidAttributeAccumulator guidAttributeAccumulator) throws Exception {
    guidAttributeAccumulator.getGuidAttribute().clear();
  }

  @Override
  public void feed(
      UbiSession ubiSession, GuidAttributeAccumulator guidAttributeAccumulator, boolean isNeeded)
      throws Exception {
    guidAttributeAccumulator
        .getGuidAttribute()
        .feed(ubiSession, BotRules.MANY_EVENTS_BOT_FLAG, isNeeded);
  }

  @Override
  public boolean filter(UbiSession ubiSession, GuidAttributeAccumulator guidAttributeAccumulator)
      throws Exception {
    return false;
  }
}
