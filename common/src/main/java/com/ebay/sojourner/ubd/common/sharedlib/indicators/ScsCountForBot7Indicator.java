package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;


public class ScsCountForBot7Indicator<Source, Target> implements Indicator<Source, Target> {
    private BotFilter botFilter;

    public ScsCountForBot7Indicator(BotFilter botFilter) {
        this.botFilter = botFilter;
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(Target target) throws Exception {
        if (target instanceof AgentIpAttributeAccumulator) {
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getAgentIpAttribute().clear();
            agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.SCS_ON_IP);
        } else if (target instanceof IpAttributeAccumulator) {
            IpAttributeAccumulator agentIpAttributeAccumulator = (IpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getIpAttribute().clear();
        }
    }


    @Override
    public void feed( Source source, Target target ) throws Exception {

    }

    @Override
    public void feed(Source source, Target target,boolean isNeeded) throws Exception {

        if (source instanceof UbiSession) {
            UbiSession ubiSession = (UbiSession) source;
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot7() < 0) {

            } else {
                if (isValid(ubiSession)) {
                    if (UbiSessionHelper.isSingleClickSession(ubiSession)) {
                        agentIpAttributeAccumulator.getAgentIpAttribute().feed(ubiSession, BotRules.SCS_ON_IP, isNeeded);
                    } else {
                        agentIpAttributeAccumulator.getAgentIpAttribute().revert(ubiSession, BotRules.SCS_ON_IP);
                    }
                }
            }

        } else {
            AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
            IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
            ipAttributeAccumulator.getIpAttribute().feed(agentIpAttribute, BotRules.SCS_ON_IP, isNeeded);
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
            int targetFlag = BotRules.SCS_ON_IP;
            if (botFilter.filter(ubiSession, targetFlag)) {
                return true;
            }
            if (ubiSession.getBotFlag() > 0 && ubiSession.getBotFlag() < 200) {
                return true;
            }

            if (ubiSession.getIp() == null) {
                return true;
            }

        }
        return false;

    }
    private boolean isValid(UbiSession ubiSession){
        return !UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
                && !isAgentBlank(ubiSession.getIp())
                && !UbiSessionHelper.isSingleClickNull(ubiSession);
    }

    protected boolean isAgentBlank(String agent) {
        return agent == null || "".equals(agent);
    }
}