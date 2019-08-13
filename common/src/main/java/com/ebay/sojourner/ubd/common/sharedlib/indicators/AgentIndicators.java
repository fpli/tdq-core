package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;

public class AgentIndicators extends AttributeIndicators<AgentIpAttribute, AgentAttributeAccumulator> {

    private static Logger logger = Logger.getLogger(SessionMetrics.class);

    private static AgentIndicators agentIpIndicators;
    private static BotFilter botFilter ;

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
        InputStream resourceAsStream = AgentIndicator.class.getResourceAsStream("/ubi.properties");
        botFilter = new UbiBotFilter(UBIConfig.getInstance(resourceAsStream));
        initIndicators();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void initIndicators() {

        addIndicators(new <AgentIpAttribute, AgentIpAttributeAccumulator>ScsCountForBot6Indicator(botFilter));
        addIndicators(new <AgentIpAttribute, AgentIpAttributeAccumulator>SuspectAgentIndicator(botFilter));

    }

}
