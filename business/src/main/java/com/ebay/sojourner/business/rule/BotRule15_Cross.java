package com.ebay.sojourner.business.rule;

import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.util.BotRules;

public class BotRule15_Cross extends AbstractBotRule<GuidAttribute> {

  public static final int MANY_EVENTS_BOUND = 10000;

  @Override
  public int getBotFlag(GuidAttribute guidAttribute) {
    if (guidAttribute.getAbsEventCount() >= MANY_EVENTS_BOUND) {
      return BotRules.MANY_EVENTS_BOT_FLAG;
    } else {
      return BotRules.NON_BOT_FLAG;
    }
  }
}
