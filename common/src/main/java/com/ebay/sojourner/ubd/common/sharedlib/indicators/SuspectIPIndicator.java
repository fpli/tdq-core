package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;


public class SuspectIPIndicator<Source, Target> implements Indicator<Source, Target> {
    private BotFilter botFilter;

    public SuspectIPIndicator(BotFilter botFilter) {
        this.botFilter = botFilter;
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(Target target) throws Exception {
        if (target instanceof AgentIpAttributeAccumulator) {
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.SUSPECTED_IP_ON_AGENT);
        } else if (target instanceof IpAttributeAccumulator) {
            IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
            ipAttributeAccumulator.getIpAttribute().clear();
        }
    }

    @Override
    public void feed(Source source, Target target) throws Exception {

        if (source instanceof UbiSession) {
            UbiSession ubiSession = (UbiSession) source;
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getAgentIpAttribute().feed(ubiSession, BotRules.SUSPECTED_IP_ON_AGENT);
        }
        else if(source instanceof AgentIpAttribute)
        {
            AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
            IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
            ipAttributeAccumulator.getIpAttribute().feed(agentIpAttribute, BotRules.SUSPECTED_IP_ON_AGENT);

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
            if (ubiSession.getClientIp() == null) {
                return true;
            }
        }
        return false;

    }

    private boolean isValid(UbiSession ubiSession) {
        return !UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
                && !UbiSessionHelper.isSingleClickNull(ubiSession);
    }

    protected boolean isAgentBlank(String agent) {
        return agent == null || "".equals(agent);
    }
}