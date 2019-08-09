package com.ebay.sojourner.ubd.common.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * @author weifang.
 */
public class UbiLookups {

    private static final Logger LOGGER = Logger.getLogger(UbiLookups.class);
    public static final String LKP_RECORD_DELIMITER = "\177";
    private final Set<Integer> mktgTrafficSrcIds;
    private final Set<Integer> nonbrowserCobrands;
    private final BotAgentMatcher agentMatcher;

    private static UbiLookups ubiLookups;

    public static UbiLookups getInstance() {
        if (ubiLookups == null) {
            synchronized (UbiLookups.class) {
                if (ubiLookups == null) {
                    ubiLookups = new UbiLookups(UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties")));
                }
            }
        }
        return ubiLookups;
    }

    private UbiLookups(UBIConfig ubiConfig) {
        setupConfiguration(ubiConfig);
        mktgTrafficSrcIds = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.MKTG_TRAFFIC_SOURCE_IDS), Property.PROPERTY_DELIMITER);
        nonbrowserCobrands = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.EBAY_NONBROWSER_COBRAND), Property.PROPERTY_DELIMITER);
        Collection<String> browerAgents = PropertyUtils.parseProperty(ubiConfig.getString(Property.BROWSER_AGENT_STRING), Property.PROPERTY_DELIMITER);
        Collection<String> botAgents = PropertyUtils.parseProperty(ubiConfig.getString(Property.BOT_AGENT_STRING), Property.PROPERTY_DELIMITER);
        agentMatcher = new BotAgentMatcher(browerAgents, botAgents);
    }

    private static void setupConfiguration(UBIConfig ubiConfig) {
        ubiConfig.setString(Property.MKTG_TRAFFIC_SOURCE_IDS, ubiConfig.getUBIProperty(Property.MKTG_TRAFFIC_SOURCE_IDS));
        ubiConfig.setString(Property.EBAY_NONBROWSER_COBRAND, ubiConfig.getUBIProperty(Property.EBAY_NONBROWSER_COBRAND));
        ubiConfig.setString(Property.BROWSER_AGENT_STRING, ubiConfig.getUBIProperty(Property.BROWSER_AGENT_STRING));
        ubiConfig.setString(Property.BOT_AGENT_STRING, ubiConfig.getUBIProperty(Property.BOT_AGENT_STRING));
        ubiConfig.setString(Property.INVALID_BOT_FILTER, ubiConfig.getUBIProperty(Property.INVALID_BOT_FILTER));
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

//    public BotFilter getBotFilter() {
//        return botFilter;
//    }

    //    public Collection<String> getBrowerAgents() {
//        return browerAgents;
//    }
//
//    public Collection<String> getBotAgents() {
//        return botAgents;
//    }

}
