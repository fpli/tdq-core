package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.IpIndicators;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

public class IpAttributeAgg implements AggregateFunction<AgentIpAttribute, IpAttributeAccumulator, IpAttributeAccumulator> {
    private static final Logger logger = Logger.getLogger(IpAttributeAgg.class);
    private IpIndicators ipIndicators;

    @Override
    public IpAttributeAccumulator createAccumulator() {

        IpAttributeAccumulator ipAttributeAccumulator = new IpAttributeAccumulator();
        ipIndicators = IpIndicators.getInstance();

        try {
            ipIndicators.start(ipAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return ipAttributeAccumulator;
    }

    @Override
    public IpAttributeAccumulator add(AgentIpAttribute agentIpAttribute, IpAttributeAccumulator ipAttr) {
        if (ipAttr.getAttribute().getClientIp() == null) {
            ipAttr.getAttribute().setClientIp(agentIpAttribute.getClientIp());
        }
        try {
            ipIndicators.feed(agentIpAttribute, ipAttr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipAttr;
    }

    @Override
    public IpAttributeAccumulator getResult(IpAttributeAccumulator ipAttr) {
        return ipAttr;
    }

    @Override
    public IpAttributeAccumulator merge(IpAttributeAccumulator a, IpAttributeAccumulator b) {
        return null;
    }
}
