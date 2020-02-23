package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.IpSignatureBotDetector;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.IpIndicators;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class IpAttributeAgg implements AggregateFunction<AgentIpAttribute, IpAttributeAccumulator, IpAttributeAccumulator> {

    private IpIndicators ipIndicators;
    private IpSignatureBotDetector ipSignatureBotDetector;

    @Override
    public IpAttributeAccumulator createAccumulator() {

        IpAttributeAccumulator ipAttributeAccumulator = new IpAttributeAccumulator();
        ipIndicators = IpIndicators.getInstance();
        ipSignatureBotDetector = IpSignatureBotDetector.getInstance();

        try {
            ipIndicators.start(ipAttributeAccumulator);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return ipAttributeAccumulator;
    }

    @Override
    public IpAttributeAccumulator add(AgentIpAttribute agentIpAttribute, IpAttributeAccumulator ipAttributeAccumulator) {
        if (ipAttributeAccumulator.getIpAttribute().getClientIp() == null) {
            ipAttributeAccumulator.getIpAttribute().setClientIp(agentIpAttribute.getClientIp());
        }
        try {
            ipIndicators.feed(agentIpAttribute, ipAttributeAccumulator, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<Integer> ipBotFlag = null;

        try {
            if (ipAttributeAccumulator.getBotFlagStatus().containsValue(0) || ipAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
                ipBotFlag = ipSignatureBotDetector.getBotFlagList(ipAttributeAccumulator.getIpAttribute());
                if (ipBotFlag.contains(6)) {
                    switch (ipAttributeAccumulator.getBotFlagStatus().get(7)) {
                        case 0:
                            ipAttributeAccumulator.getBotFlagStatus().put(7, 1);
                            break;
                        case 1:
                            ipAttributeAccumulator.getBotFlagStatus().put(7, 2);
                            break;
                    }
                } else if (ipBotFlag.contains(222)) {
                    switch (ipAttributeAccumulator.getBotFlagStatus().get(222)) {
                        case 0:
                            ipAttributeAccumulator.getBotFlagStatus().put(222, 1);
                            break;
                        case 1:
                            ipAttributeAccumulator.getBotFlagStatus().put(222, 2);
                            break;
                    }
                } else if (ipBotFlag.contains(223)) {
                    switch (ipAttributeAccumulator.getBotFlagStatus().get(223)) {
                        case 0:
                            ipAttributeAccumulator.getBotFlagStatus().put(223, 1);
                            break;
                        case 1:
                            ipAttributeAccumulator.getBotFlagStatus().put(223, 2);
                            break;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("ip getBotFlagList error", e);
        }

        Set<Integer> botFlagList = ipAttributeAccumulator.getIpAttribute().getBotFlagList();

        if (ipBotFlag != null && ipBotFlag.size() > 0) {
            botFlagList.addAll(ipBotFlag);
        }

        ipAttributeAccumulator.getIpAttribute().setBotFlagList(botFlagList);

        return ipAttributeAccumulator;
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
