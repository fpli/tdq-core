package com.ebay.sojourner.ubd.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Set;

/**
 * @author weifang.
 */
@Slf4j
public class UbiLookups {

    public static final String LKP_RECORD_DELIMITER = "\177";
    private final Set<Integer> mktgTrafficSrcIds;
    private final Set<Integer> nonbrowserCobrands;
    private final BotAgentMatcher agentMatcher;

    private static UbiLookups ubiLookups;

    public static UbiLookups getInstance() {
        if (ubiLookups == null) {
            synchronized (UbiLookups.class) {
                if (ubiLookups == null) {
                    ubiLookups = new UbiLookups();
                }
            }
        }
        return ubiLookups;
    }

    private UbiLookups() {
        setupConfiguration();
        mktgTrafficSrcIds = PropertyUtils.getIntegerSet(UBIConfig.getString(Property.MKTG_TRAFFIC_SOURCE_IDS), Property.PROPERTY_DELIMITER);
        nonbrowserCobrands = PropertyUtils.getIntegerSet(UBIConfig.getString(Property.EBAY_NONBROWSER_COBRAND), Property.PROPERTY_DELIMITER);
        Collection<String> browerAgents = PropertyUtils.parseProperty(UBIConfig.getString(Property.BROWSER_AGENT_STRING), Property.PROPERTY_DELIMITER);
        Collection<String> botAgents = PropertyUtils.parseProperty(UBIConfig.getString(Property.BOT_AGENT_STRING), Property.PROPERTY_DELIMITER);
        agentMatcher = new BotAgentMatcher(browerAgents, botAgents);
    }

    private static void setupConfiguration() {
        UBIConfig.setString(Property.MKTG_TRAFFIC_SOURCE_IDS, UBIConfig.getUBIProperty(Property.MKTG_TRAFFIC_SOURCE_IDS));
        UBIConfig.setString(Property.EBAY_NONBROWSER_COBRAND, UBIConfig.getUBIProperty(Property.EBAY_NONBROWSER_COBRAND));
        UBIConfig.setString(Property.BROWSER_AGENT_STRING, UBIConfig.getUBIProperty(Property.BROWSER_AGENT_STRING));
        UBIConfig.setString(Property.BOT_AGENT_STRING, UBIConfig.getUBIProperty(Property.BOT_AGENT_STRING));
        UBIConfig.setString(Property.INVALID_BOT_FILTER, UBIConfig.getUBIProperty(Property.INVALID_BOT_FILTER));
    }

    public Set<Integer> getMktgTraficSrcIds() {
        return mktgTrafficSrcIds;
    }

    public Set<Integer> getNonbrowserCobrands() {
        return nonbrowserCobrands;
    }

    public BotAgentMatcher getAgentMatcher() {
        return agentMatcher;
    }

}
