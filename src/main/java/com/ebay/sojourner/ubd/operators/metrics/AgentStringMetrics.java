package com.ebay.sojourner.ubd.operators.metrics;


import com.ebay.sojourner.ubd.common.sojlib.IsValidIPv4;
import com.ebay.sojourner.ubd.model.SessionAccumulator;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.Property;
import com.ebay.sojourner.ubd.util.PropertyUtils;
import com.ebay.sojourner.ubd.util.UBIConfig;
import org.apache.log4j.Logger;

import java.util.Set;

public class AgentStringMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    public static final String SHOCKWAVE_FLASH_AGENT = "Shockwave Flash";
    public static final int AGENT_MAX_LENGTH = 2000;
    private static Set<Integer> agentExcludeSet;
    private static final Logger logger = Logger.getLogger(AgentStringMetrics.class);
    private static UBIConfig ubiConfig ;
    @Override
    public void init() throws Exception {
        ubiConfig = UBIConfig.getInstance();
        agentExcludeSet = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.AGENT_EXCLUDE_PAGES), Property.PROPERTY_DELIMITER);
        logger.info("UBIConfig.getString(Property.AGENT_EXCLUDE_PAGES):"+ubiConfig.getString(Property.AGENT_EXCLUDE_PAGES));

    }

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        // do clear first as end method may not been invoked.

        sessionAccumulator.getUbiSession().setAgentString(null);
      //  feed(event, sessionAccumulator);
    }

    @Override
    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
        // Same logic implemented in IntermediaEventMetrics.java -> Line289
        String agentInfo = event.getAgentInfo();
        // logger.info("agentExcludeSet.size():"+agentExcludeSet.size());
        if (event.getRdt() == 0 && event.getIframe() == 0 && !agentExcludeSet.contains(event.getPageId()) && agentInfo != null
                && !agentInfo.equals(SHOCKWAVE_FLASH_AGENT) && !IsValidIPv4.isValidIP(agentInfo)) {
            if (agentInfo.length() > AGENT_MAX_LENGTH) {
                agentInfo = agentInfo.substring(0, AGENT_MAX_LENGTH);
            }
            
            if (sessionAccumulator.getUbiSession().getAgentString() == null) {
                sessionAccumulator.getUbiSession().setAgentString(agentInfo);
            }
            sessionAccumulator.getUbiSession().getAgentSets().add(agentInfo);
        }
    }

    @Override
    public void end(SessionAccumulator sessionAccumulator) throws Exception {
        int agentCnt = 0;
        if (!sessionAccumulator.getUbiSession().getAgentSets().isEmpty()) {
            // Use the first agent String
//            sessionAccumulator.getUbiSession().setAgentString(agentString);
            agentCnt = sessionAccumulator.getUbiSession().getAgentSets().size();
        }
        sessionAccumulator.getUbiSession().setAgentCnt(agentCnt);
    }
}
