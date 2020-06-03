package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.util.UbiSessionHelper;
import java.io.IOException;

public class BotRuleForDeclarativeAgent extends AbstractBotRule<AgentAttribute> {

  @Override
  public int getBotFlag(AgentAttribute agentAttribute) throws IOException, InterruptedException {
    if (UbiSessionHelper.isAgentDeclarative(agentAttribute)) {
      return 221;
    }
    return 0;
  }
}
