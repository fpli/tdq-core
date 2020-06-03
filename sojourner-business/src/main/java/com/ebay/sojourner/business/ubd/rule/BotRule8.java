package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.util.BotRules;

public class BotRule8 extends AbstractBotRule<AgentIpAttribute> {

  private static final int BID_BIN_COUNT = 5;

  @Override
  public int getBotFlag(AgentIpAttribute agentIpAttribute) {
    if (agentIpAttribute.getScsCountForBot8() > 0
        && agentIpAttribute.getBbcCount() > BID_BIN_COUNT) {
      return BotRules.SCS_CONFIRM_ON_AGENTIP;
    } else {
      return BotRules.NON_BOT_FLAG;
    }
  }
}
