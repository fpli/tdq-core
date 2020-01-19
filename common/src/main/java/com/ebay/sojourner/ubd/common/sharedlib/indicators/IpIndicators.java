package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpIndicators extends AttributeIndicators<AgentIpAttribute, IpAttributeAccumulator> {

    private static volatile IpIndicators ipIndicators;
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
//        addIndicators(new <AgentIpAttribute,IpAttributeAccumulator>ScsCountForBot8Indicator(botFilter));
        addIndicators(new <AgentIpAttribute,IpAttributeAccumulator>ScsCountForBot7Indicator(botFilter));
        addIndicators(new <AgentIpAttribute,IpAttributeAccumulator>SuspectIPIndicator(botFilter));
    }

}
