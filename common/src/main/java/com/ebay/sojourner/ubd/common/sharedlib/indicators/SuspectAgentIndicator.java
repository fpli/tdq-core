package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;


public class SuspectAgentIndicator<Source, Target> implements Indicator<Source, Target> {
    private BotFilter botFilter;

    public SuspectAgentIndicator(BotFilter botFilter) {
        this.botFilter = botFilter;
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(Target target) throws Exception {
        if (target instanceof AgentIpAttributeAccumulator) {
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getAttribute().clear(BotRules.DECLARED_AGENT);
        } else if (target instanceof AgentAttributeAccumulator) {
            AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
            agentAttributeAccumulator.getAttribute().clear();
        }
    }

    @Override
    public void feed(Source source, Target target) throws Exception {

        if (source instanceof UbiSession) {
            UbiSession ubiSession = (UbiSession) source;
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getAttribute().feed(ubiSession, BotRules.DECLARED_AGENT);
        }
        else if(source instanceof AgentIpAttribute)
        {
            AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
            AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
            agentAttributeAccumulator.getAttribute().feed(agentIpAttribute, BotRules.DECLARED_AGENT);

        }

    }

    @Override
    public void end(Target target) throws Exception {
        // to do nonthing;
    }


    @Override
    public boolean filter(Source source, Target target) throws Exception {
        if (source instanceof UbiSession) {
            UbiSession ubiSession = (UbiSession) source;
            int targetFlag = BotRules.DECLARED_AGENT;
            if (botFilter.filter(ubiSession, targetFlag)) {
                return true;
            }
            if (ubiSession.getUserAgent() == null) {
                return true;
            }
        }
        return false;

    }

    protected boolean isAgentBlank(String agent) {
        return agent == null || "".equals(agent);
    }
}