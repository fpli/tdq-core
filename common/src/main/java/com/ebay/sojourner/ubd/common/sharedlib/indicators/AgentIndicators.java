package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentIndicators extends AttributeIndicators<AgentIpAttribute, AgentAttributeAccumulator> {

    private static volatile AgentIndicators agentIpIndicators;
    private BotFilter botFilter;

    public static AgentIndicators getInstance() {
        if (agentIpIndicators == null) {
            synchronized (AgentIndicators.class) {
                if (agentIpIndicators == null) {
                    agentIpIndicators = new AgentIndicators();
                }
            }
        }
        return agentIpIndicators;
    }

    public AgentIndicators() {
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
        addIndicators(new ScsCountForBot6Indicator<>(botFilter));
        addIndicators(new SuspectAgentIndicator<>(botFilter));
    }

}
