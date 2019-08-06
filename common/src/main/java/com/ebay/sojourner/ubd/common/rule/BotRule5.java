package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class BotRule5 implements Rule<AgentIpAttribute> {
    public static final int MAX_COUNT = 50;

    @Override
    public int getBotFlag(AgentIpAttribute agentIpAttribute) {
        if (agentIpAttribute.getScsCountForBot5() >= MAX_COUNT) {
            return BotRules.SCS_ON_AGENTIP;
        } else {
            return BotRules.NON_BOT_FLAG;
        }
    }

    @Override
    public void init() {

    }

}
