package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.AgentIpAttribute;

public class BotRuleForNewBot extends AbstractBotRule<AgentIpAttribute> {

  @Override
  public int getBotFlag(AgentIpAttribute agentIpAttribute) {
    if (agentIpAttribute.isHasNewBot()) {
      return 224;
    }
    return 0;
  }
}
