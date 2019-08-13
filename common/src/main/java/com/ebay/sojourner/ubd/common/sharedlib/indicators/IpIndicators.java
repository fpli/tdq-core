package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.*;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;

public class IpIndicators extends AttributeIndicators<AgentIpAttribute, IpAttributeAccumulator> {

    private static Logger logger = Logger.getLogger(SessionMetrics.class);

    private static IpIndicators ipIndicators;
    private static BotFilter botFilter ;

    public static IpIndicators getInstance() {
        if (ipIndicators == null) {
            synchronized (IpIndicators.class) {
                if (ipIndicators == null) {
                    ipIndicators = new IpIndicators();
                }
            }
        }
        return ipIndicators;
    }

    public IpIndicators() {
        InputStream resourceAsStream = IpIndicators.class.getResourceAsStream("/ubi.properties");
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

        addIndicators(new <AgentIpAttribute,IpAttributeAccumulator>ScsCountForBot8Indicator(botFilter));
        addIndicators(new <AgentIpAttribute,IpAttributeAccumulator>SuspectIPIndicator(botFilter));

    }

}
