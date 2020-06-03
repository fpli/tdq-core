package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;

public class BotRuleForNewBot extends AbstractBotRule<AgentIpAttribute> {

  @Override
  public int getBotFlag(AgentIpAttribute agentIpAttribute) {
    if (agentIpAttribute.isHasNewBot()) {
      return 224;
    }
    return 0;
  }
}
