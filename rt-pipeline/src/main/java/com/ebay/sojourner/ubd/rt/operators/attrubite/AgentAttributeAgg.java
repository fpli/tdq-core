package com.ebay.sojourner.ubd.rt.operators.attrubite;

import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.IpIndicator;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.log4j.Logger;

public class AgentAttributeAgg implements AggregateFunction<UbiSession, IpAttributeAccumulator, IpAttributeAccumulator> {
    private static final Logger logger = Logger.getLogger(AgentAttributeAgg.class);
    private IpIndicator ipIndicator;

    @Override
    public IpAttributeAccumulator createAccumulator() {

        IpAttributeAccumulator ipAttributeAccumulator = new IpAttributeAccumulator();
        ipIndicator = IpIndicator.getInstance();

        try {
            ipIndicator.start(ipAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return ipAttributeAccumulator;
    }

    @Override
    public IpAttributeAccumulator add(UbiSession session, IpAttributeAccumulator ipAttr) {
        if (ipAttr.getAttribute().getClientIp() == null) {
            ipAttr.getAttribute().setClientIp(session.getClientIp());
        }
        try {
            ipIndicator.feed(session,ipAttr);
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
