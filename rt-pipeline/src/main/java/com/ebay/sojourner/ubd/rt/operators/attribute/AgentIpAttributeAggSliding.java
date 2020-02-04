package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.AgentIpIndicators;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.AgentIpIndicatorsSliding;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

public class AgentIpAttributeAggSliding implements AggregateFunction<AgentIpAttribute, AgentIpAttributeAccumulator, AgentIpAttributeAccumulator> {
    private static final Logger logger = Logger.getLogger(AgentIpAttributeAggSliding.class);
    private AgentIpIndicatorsSliding agentIpIndicators;

    @Override
    public AgentIpAttributeAccumulator createAccumulator() {

        AgentIpAttributeAccumulator agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
        agentIpIndicators = AgentIpIndicatorsSliding.getInstance();

        try {
            agentIpIndicators.start(agentIpAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return agentIpAttributeAccumulator;
    }

    @Override
    public AgentIpAttributeAccumulator add(AgentIpAttribute agentIpAttribute, AgentIpAttributeAccumulator agentIpAttributeAccumulator) {
        if (agentIpAttributeAccumulator.getAgentIpAttribute().getClientIp() == null&&agentIpAttributeAccumulator.getAgentIpAttribute().getAgent()==null) {
            agentIpAttributeAccumulator.getAgentIpAttribute().setClientIp(agentIpAttribute.getClientIp());
            agentIpAttributeAccumulator.getAgentIpAttribute().setAgent(agentIpAttribute.getAgent());
        }
        try {

            agentIpIndicators.feed(agentIpAttribute,agentIpAttributeAccumulator,true);
//            System.out.println("agg:==========="+agentIpAttributeAccumulator.getAgentIpAttribute());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return agentIpAttributeAccumulator;
    }

    @Override
    public AgentIpAttributeAccumulator getResult(AgentIpAttributeAccumulator agentIpAttributeAccumulator) {
        return agentIpAttributeAccumulator;
    }

    @Override
    public AgentIpAttributeAccumulator merge(AgentIpAttributeAccumulator a, AgentIpAttributeAccumulator b) {
        return null;
    }
}
