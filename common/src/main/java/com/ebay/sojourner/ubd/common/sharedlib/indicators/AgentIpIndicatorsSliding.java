package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentIpIndicatorsSliding extends AttributeIndicators<AgentIpAttribute, AgentIpAttributeAccumulator> {

    private static volatile AgentIpIndicatorsSliding agentIpIndicators;
    private static BotFilter botFilter;

    public static AgentIpIndicatorsSliding getInstance() {
        if (agentIpIndicators == null) {
            synchronized (AgentIpIndicatorsSliding.class) {
                if (agentIpIndicators == null) {
                    agentIpIndicators = new AgentIpIndicatorsSliding();
                }
            }
        }
        return agentIpIndicators;
    }

    public AgentIpIndicatorsSliding() {
        botFilter = new UbiBotFilter();
        initIndicators();
        try {
            init();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void initIndicators() {
        addIndicators(new <AgentIpAttribute, AgentIpAttributeAccumulator>ScsCountForBot5Indicator(botFilter));
        addIndicators(new <AgentIpAttribute, AgentIpAttributeAccumulator>ScsCountForBot8Indicator(botFilter));
    }

}
