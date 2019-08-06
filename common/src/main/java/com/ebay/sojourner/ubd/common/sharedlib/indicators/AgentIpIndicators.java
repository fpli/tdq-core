package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import org.apache.log4j.Logger;

import java.io.File;

public class AgentIpIndicators extends AttributeIndicators<UbiSession, AgentIpAttributeAccumulator> {

    private static Logger logger = Logger.getLogger(SessionMetrics.class);

    private static AgentIpIndicators agentIpIndicators;
    private static BotFilter botFilter ;

    public static AgentIpIndicators getInstance() {
        if (agentIpIndicators == null) {
            synchronized (AgentIpIndicators.class) {
                if (agentIpIndicators == null) {
                    agentIpIndicators = new AgentIpIndicators();
                }
            }
        }
        return agentIpIndicators;
    }

    public AgentIpIndicators() {
        botFilter = new UbiBotFilter(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")));
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

        addIndicators(new <UbiSession, AgentIpAttributeAccumulator>ScsCountForBot5Indicator(botFilter));
        addIndicators(new <UbiSession, AgentIpAttributeAccumulator>ScsCountForBot6Indicator(botFilter));
        addIndicators(new <UbiSession, AgentIpAttributeAccumulator>ScsCountForBot7Indicator(botFilter));
        addIndicators(new <UbiSession, AgentIpAttributeAccumulator>ScsCountForBot8Indicator(botFilter));
        addIndicators(new <UbiSession, AgentIpAttributeAccumulator>SuspectAgentIndicator(botFilter));
        addIndicators(new <UbiSession, AgentIpAttributeAccumulator>SuspectIPIndicator(botFilter));

    }

}
