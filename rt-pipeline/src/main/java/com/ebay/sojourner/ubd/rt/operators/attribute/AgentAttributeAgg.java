package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.AgentIndicators;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

public class AgentAttributeAgg implements AggregateFunction<AgentIpAttribute, AgentAttributeAccumulator, AgentAttributeAccumulator> {
    private static final Logger logger = Logger.getLogger(AgentAttributeAgg.class);
    private AgentIndicators agentIndicators;

    @Override
    public AgentAttributeAccumulator createAccumulator() {

        AgentAttributeAccumulator agentAttributeAccumulator = new AgentAttributeAccumulator();
        agentIndicators = AgentIndicators.getInstance();

        try {
            agentIndicators.start(agentAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return agentAttributeAccumulator;
    }

    @Override
    public AgentAttributeAccumulator add(AgentIpAttribute agentIpAttribute, AgentAttributeAccumulator agentAttributeAccumulator) {
        if (agentAttributeAccumulator.getAttribute().getAgent() == null) {

            agentAttributeAccumulator.getAttribute().setAgent(agentIpAttribute.getAgent());
        }
        try {
            agentIndicators.feed(agentIpAttribute, agentAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return agentAttributeAccumulator;
    }

    @Override
    public AgentAttributeAccumulator getResult(AgentAttributeAccumulator agentAttributeAccumulator) {
        return agentAttributeAccumulator;
    }

    @Override
    public AgentAttributeAccumulator merge(AgentAttributeAccumulator a, AgentAttributeAccumulator b) {
        return null;
    }
}
