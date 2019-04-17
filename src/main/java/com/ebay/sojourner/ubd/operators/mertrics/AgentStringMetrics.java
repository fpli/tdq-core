package com.ebay.sojourner.ubd.operators.mertrics;


import com.ebay.sojourner.ubd.common.sojlib.IsValidIPv4;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.Property;
import com.ebay.sojourner.ubd.util.PropertyUtils;

import java.util.HashSet;
import java.util.Set;

public class AgentStringMetrics implements FieldMetrics<UbiEvent, UbiEvent> {
    public static final String SHOCKWAVE_FLASH_AGENT = "Shockwave Flash";
    public static final int AGENT_MAX_LENGTH = 2000;
    private Set<String> agentSets;
    private Set<Integer> agentExcludeSet;
    private String agentString;

    @Override
    public void start(UbiEvent event, UbiEvent session) throws Exception {
        // do clear first as end method may not been invoked.
        agentSets.clear();
        agentString = null;
        agentExcludeSet = PropertyUtils.getIntegerSet(event.getConfiguration().getString(Property.AGENT_EXCLUDE_PAGES,null), Property.PROPERTY_DELIMITER);
        agentSets = new HashSet<String>();
        feed(event, session);
    }

    @Override
    public void feed(UbiEvent event, UbiEvent session) throws Exception {
        // Same logic implemented in IntermediaEventMetrics.java -> Line289
        String agentInfo = event.getAgentInfo();
        if (event.getRdt() == 0 && event.getIframe() == 0 && !agentExcludeSet.contains(event.getPageId()) && agentInfo != null
                && !agentInfo.equals(SHOCKWAVE_FLASH_AGENT) && !IsValidIPv4.isValidIP(agentInfo)) {
            if (agentInfo.length() > AGENT_MAX_LENGTH) {
                agentInfo = agentInfo.substring(0, AGENT_MAX_LENGTH);
            }
            
            if (agentString == null) {
                agentString = agentInfo;
            }

            agentSets.add(agentInfo);
        }
    }

    @Override
    public void end(UbiEvent event) throws Exception {
        int agentCnt = 0;
        if (!agentSets.isEmpty()) {
            // Use the first agent String
            event.getUbiSession().setAgentString(agentString);
            agentCnt = agentSets.size();
        }
        event.getUbiSession().setAgentCnt(agentCnt);
    }
}
