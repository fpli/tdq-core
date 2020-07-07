package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.util.BotRules;

public class BotRule5 extends AbstractBotRule<AgentIpAttribute> {

  public static final int MAX_COUNT = 50;

  @Override
  public int getBotFlag(AgentIpAttribute agentIpAttribute) {
    if (agentIpAttribute.getScsCountForBot5() > MAX_COUNT) {
      return BotRules.SCS_ON_AGENTIP;
    } else {
      return BotRules.NON_BOT_FLAG;
    }
  }
}
