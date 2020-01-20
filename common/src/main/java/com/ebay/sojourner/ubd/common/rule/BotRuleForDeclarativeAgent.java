package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;

import java.io.IOException;

public class BotRuleForDeclarativeAgent implements Rule<AgentAttribute> {

    @Override
    public int getBotFlag(AgentAttribute agentAttribute) throws IOException, InterruptedException {
        if (UbiSessionHelper.isAgentDeclarative(agentAttribute)) {
            return 221;
        }
        return 0;
    }

    @Override
    public void init() {

    }

}
