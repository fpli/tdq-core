package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;


public class ScsCountForBot6Indicator<Source, Target> implements Indicator<Source, Target> {
    private BotFilter botFilter;

    public ScsCountForBot6Indicator(BotFilter botFilter) {
        this.botFilter = botFilter;
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(Target target) throws Exception {
        if (target instanceof AgentIpAttributeAccumulator) {
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getAttribute().clear(BotRules.SCS_ON_AGENT);
            agentIpAttributeAccumulator.getAttribute().setIpCount(0);
        } else if (target instanceof IpAttributeAccumulator) {
            IpAttributeAccumulator agentIpAttributeAccumulator = (IpAttributeAccumulator) target;
            agentIpAttributeAccumulator.getAttribute().clear();
        }
    }

    @Override
    public void feed(Source source, Target target) throws Exception {

        if (source instanceof UbiSession) {
            UbiSession ubiSession = (UbiSession) source;
            AgentIpAttributeAccumulator agentIpAttributeAccumulator = (AgentIpAttributeAccumulator) target;
            if (agentIpAttributeAccumulator.getAttribute().getScsCountForBot6() < 0) {

            } else {
                if (isValid(ubiSession)) {
                    if (UbiSessionHelper.isSingleClickSession(ubiSession)) {
                        agentIpAttributeAccumulator.getAttribute().feed(ubiSession, BotRules.SCS_ON_AGENT);
                    } else {
                        agentIpAttributeAccumulator.getAttribute().revert(ubiSession, BotRules.SCS_ON_AGENT);
                    }
                }
            }

            if (UbiSessionHelper.isNonIframRdtCountZero(ubiSession) && !isIpBlank(ubiSession.getIp())&&agentIpAttributeAccumulator.getAttribute().getIpCount()<=0) {
                agentIpAttributeAccumulator.getAttribute().setIpCount(1);
            }
        } else {
            AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
            AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
            agentAttributeAccumulator.getAttribute().feed(agentIpAttribute,BotRules.SCS_ON_AGENT);
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
            int targetFlag = BotRules.SCS_ON_AGENT;
            if (botFilter.filter(ubiSession, targetFlag)) {
                return true;
            }
            if (ubiSession.getBotFlag() != null && ubiSession.getBotFlag() > 0 && ubiSession.getBotFlag() < 200) {
                return true;
            }
            if (ubiSession.getUserAgent() == null) {
                return true;
            }

        }
        return false;

    }

    private boolean isValid(UbiSession ubiSession) {
        return !UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
                && !isIpBlank(ubiSession.getIp())
                && !UbiSessionHelper.isSingleClickNull(ubiSession);
    }

    protected boolean isIpBlank(String ip) {
        return ip == null || "".equals(ip);
    }
}