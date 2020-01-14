package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;


public class ScsCountForBot5Indicator<Source, Target> implements Indicator<Source, Target> {
    private BotFilter botFilter;

    public ScsCountForBot5Indicator(BotFilter botFilter) {
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
        } else if (target instanceof IpAttributeAccumulator) {
            IpAttributeAccumulator ipAttributeAccumulator = (IpAttributeAccumulator) target;
            ipAttributeAccumulator.getIpAttribute().clear();
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
            if(agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot5()<0){
                return;
            }else {
                if (isValid(ubiSession)) {
                    if (UbiSessionHelper.isSingleClickSession(ubiSession)) {
                        agentIpAttributeAccumulator.getAgentIpAttribute().feed(ubiSession, BotRules.SCS_ON_AGENTIP,isNeeded);
                    } else {
                        agentIpAttributeAccumulator.getAgentIpAttribute().revert(ubiSession, BotRules.SCS_ON_AGENTIP);
                    }
                }
            }

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
            int targetFlag = BotRules.SCS_ON_AGENTIP;
            if (botFilter.filter(ubiSession, targetFlag)) {
                return true;
            }
            if (ubiSession.getBotFlag() > 0 && ubiSession.getBotFlag() < 200) {
                return true;
            }

        }
        return false;

    }
    private boolean isValid(UbiSession ubiSession){
        return !UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
                && ubiSession.getIp() != null
                && !UbiSessionHelper.isSingleClickNull(ubiSession);
    }



}