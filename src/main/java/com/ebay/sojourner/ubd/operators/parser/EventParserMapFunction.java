package com.ebay.sojourner.ubd.operators.parser;

import com.ebay.sojourner.ubd.util.Property;
import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.UBIConfig;
import com.ebay.sojourner.ubd.common.sojlib.SOJTS2Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.io.File;

public class EventParserMapFunction extends RichMapFunction<RawEvent,UbiEvent> {
    private EventParser parser;
    @Override
    public void open(Configuration conf) throws Exception {
        super.open(conf);
        File configFile = getRuntimeContext().getDistributedCache().getFile("configFile");
        UBIConfig.initAppConfiguration(configFile);
        initConfiguration(conf,false);
        setConfiguration(conf);
        parser = new EventParser(conf,getRuntimeContext());

    }

    @Override
    public UbiEvent map(RawEvent rawEvent) throws Exception {
        UbiEvent event = new UbiEvent();
        parser.parse(rawEvent, event);
       return event;
    }

    public static void initConfiguration(Configuration conf, boolean enableTest) throws Exception {


        if (UBIConfig.getUBIProperty(Property.LOG_LEVEL) != null) {
            conf.setString(Property.LOG_LEVEL, UBIConfig.getUBIProperty(Property.LOG_LEVEL));
        }

        if (enableTest) {
            conf.setBoolean(Property.IS_TEST_ENABLE, true);
            loadLookupTableLocally(conf);
        } else {
            conf.setString(Property.IFRAME_PAGE_IDS, UBIConfig.getUBIProperty(Property.IFRAME_PAGE_IDS));

            conf.setString(Property.FINDING_FLAGS, UBIConfig.getUBIProperty(Property.FINDING_FLAGS));
            conf.setString(Property.VTNEW_IDS, UBIConfig.getUBIProperty(Property.VTNEW_IDS));
            conf.setString(Property.IAB_AGENT, UBIConfig.getUBIProperty(Property.IAB_AGENT ));
            conf.setString(Property.APP_ID, UBIConfig.getUBIProperty(Property.APP_ID));
            conf.setString(Property.TEST_USER_IDS, UBIConfig.getUBIProperty(Property.TEST_USER_IDS));
            conf.setString(Property.LARGE_SESSION_GUID, UBIConfig.getUBIProperty(Property.LARGE_SESSION_GUID));
            conf.setString(Property.PAGE_FMLY, UBIConfig.getUBIProperty(Property.PAGE_FMLY));
            conf.setString(Property.MPX_ROTATION, UBIConfig.getUBIProperty(Property.MPX_ROTATION));

//            conf.setString(Property.IFRAME_PAGE_IDS4Bot12, UBIConfig.getUBIProperty(Property.IFRAME_PAGE_IDS4Bot12));
//            conf.setString(Property.SELECTED_IPS, UBIConfig.getUBIProperty(Property.SELECTED_IPS));
//            conf.setString(Property.SELECTED_AGENTS, UBIConfig.getUBIProperty(Property.SELECTED_AGENTS));

        }
    }
    protected static void loadLookupTableLocally(Configuration conf) throws Exception {
        LkpFetcher fetcher = new LkpFetcher();
        fetcher.loadLocally();
        for (String key : fetcher.getResult().keySet()) {
            if (null == conf.getString(key,null)) {
                String value = fetcher.getResult().get(key);
                conf.setString(key, value);
            }
        }
    }
    public void setConfiguration(Configuration conf) throws Exception {

        // Set session properties
        conf.setLong(Property.EVENT_WAITING_PERIOD, Long.valueOf(UBIConfig.getUBIProperty(Property.EVENT_WAITING_PERIOD)) * SOJTS2Date.MILLI2MICRO);
        conf.setLong(Property.SESSION_IDLE_TIMEOUT, Long.valueOf(UBIConfig.getUBIProperty(Property.SESSION_IDLE_TIMEOUT)) * SOJTS2Date.MILLI2MICRO);
        conf.setLong(Property.SESSION_MAX_DURATION, Long.valueOf(UBIConfig.getUBIProperty(Property.SESSION_MAX_DURATION)) * SOJTS2Date.MILLI2MICRO);
        conf.setLong(Property.EVENT_DELAY_THRESHOLD, Long.valueOf(UBIConfig.getUBIProperty(Property.EVENT_DELAY_THRESHOLD)) * SOJTS2Date.MILLI2MICRO);


        // Set event properties
        conf.setLong(Property.EVENT_POOL_THRESHOLD, Long.valueOf(UBIConfig.getUBIProperty(Property.EVENT_POOL_THRESHOLD)));
        // Set Cobrand properties
        conf.setString(Property.EXPRESS_SITE, UBIConfig.getUBIProperty(Property.EXPRESS_SITE));
        conf.setString(Property.HALF_SITE, UBIConfig.getUBIProperty(Property.HALF_SITE));
        conf.setString(Property.EXPRESS_PARTNER, UBIConfig.getUBIProperty(Property.EXPRESS_PARTNER));
        conf.setString(Property.SHOPPING_PARTNER, UBIConfig.getUBIProperty(Property.SHOPPING_PARTNER));
        conf.setString(Property.HALF_PARTNER, UBIConfig.getUBIProperty(Property.HALF_PARTNER));
        conf.setString(Property.ARTISAN_PARTNER, UBIConfig.getUBIProperty(Property.ARTISAN_PARTNER));
        conf.setString(Property.MOBILE_AGENT_START, UBIConfig.getUBIProperty(Property.MOBILE_AGENT_START));
        conf.setString(Property.MOBILE_AGENT_INDEX, UBIConfig.getUBIProperty(Property.MOBILE_AGENT_INDEX));
        conf.setString(Property.MOBILE_AGENT_OTHER, UBIConfig.getUBIProperty(Property.MOBILE_AGENT_OTHER));
        // Set page indicators
        conf.setString(Property.SEARCH_VIEW_PAGES, UBIConfig.getUBIProperty(Property.SEARCH_VIEW_PAGES));
        conf.setString(Property.VIEW_ITEM_PAGES, UBIConfig.getUBIProperty(Property.VIEW_ITEM_PAGES));
        conf.setString(Property.BID_PAGES, UBIConfig.getUBIProperty(Property.BID_PAGES));
        conf.setString(Property.BIN_PAGES, UBIConfig.getUBIProperty(Property.BIN_PAGES));
        conf.setString(Property.CAPTCHA_PAGES, UBIConfig.getUBIProperty(Property.CAPTCHA_PAGES));
        conf.setString(Property.HALF_PAGES, UBIConfig.getUBIProperty(Property.HALF_PAGES));
        conf.setString(Property.CORESITE_PAGES, UBIConfig.getUBIProperty(Property.CORESITE_PAGES));
        conf.setString(Property.CLASSIFIED_PAGES, UBIConfig.getUBIProperty(Property.CLASSIFIED_PAGES));
        // New metrics need page list
        conf.setString(Property.ROVER_PAGES, UBIConfig.getUBIProperty(Property.ROVER_PAGES));
        conf.setString(Property.LAND_PAGES1, UBIConfig.getUBIProperty(Property.LAND_PAGES1));
        conf.setString(Property.LAND_PAGES2, UBIConfig.getUBIProperty(Property.LAND_PAGES2));
        conf.setString(Property.SCEVENT_EXCLUDE_PAGES1, UBIConfig.getUBIProperty(Property.SCEVENT_EXCLUDE_PAGES1));
        conf.setString(Property.SCEVENT_EXCLUDE_PAGES2, UBIConfig.getUBIProperty(Property.SCEVENT_EXCLUDE_PAGES2));
        conf.setString(Property.AGENT_EXCLUDE_PAGES, UBIConfig.getUBIProperty(Property.AGENT_EXCLUDE_PAGES));
        conf.setString(Property.NOTIFY_CLICK_PAGES, UBIConfig.getUBIProperty(Property.NOTIFY_CLICK_PAGES));
        conf.setString(Property.NOTIFY_VIEW_PAGES, UBIConfig.getUBIProperty(Property.NOTIFY_VIEW_PAGES));
        conf.setString(Property.LNDG_PAGE_IDS, UBIConfig.getUBIProperty(Property.LNDG_PAGE_IDS));
        conf.setString(Property.IP_EXCLUDE_PAGES, UBIConfig.getUBIProperty(Property.IP_EXCLUDE_PAGES));
        conf.setString(Property.EXCLUDE_IP_PATTERN, UBIConfig.getUBIProperty(Property.EXCLUDE_IP_PATTERN));
        conf.setString(Property.EBAY_SITE_COBRAND, UBIConfig.getUBIProperty(Property.EBAY_SITE_COBRAND));
        conf.setString(Property.INVALID_BOT_FILTER, UBIConfig.getUBIProperty(Property.INVALID_BOT_FILTER));
        // Extra metrics for page list
        conf.setString(Property.INVALID_PAGE_IDS, UBIConfig.getUBIProperty(Property.INVALID_PAGE_IDS));
        // Set APP PAYLOAD KV Property
        conf.setString(Property.SWD_VALUES, UBIConfig.getUBIProperty(Property.SWD_VALUES));
        conf.setString(Property.ROT_VALUES, UBIConfig.getUBIProperty(Property.ROT_VALUES));
        conf.setString(Property.VI_EVENT_VALUES, UBIConfig.getUBIProperty(Property.VI_EVENT_VALUES));
        // Set APP ID property
        conf.setString(Property.MOBILE_APP, UBIConfig.getUBIProperty(Property.MOBILE_APP));
        conf.setString(Property.DESKTOP_APP, UBIConfig.getUBIProperty(Property.DESKTOP_APP));
        conf.setString(Property.EIM_APP, UBIConfig.getUBIProperty(Property.EIM_APP));
        // Set BOT rules
        conf.setString(Property.BOT_RULE_PACKAGE, UBIConfig.getUBIProperty(Property.BOT_RULE_PACKAGE));
        conf.setString(Property.BOT_RULE_CLASSES, UBIConfig.getUBIProperty(Property.BOT_RULE_CLASSES));
        // Set large session properties
        conf.setLong(Property.LARGE_SESSION_EVENT_NUMBER, Long.valueOf(UBIConfig.getUBIProperty(Property.LARGE_SESSION_EVENT_NUMBER)));
        conf.setLong(Property.LARGE_SESSION_TIMES_OF_BOT15, Long.valueOf(UBIConfig.getUBIProperty(Property.LARGE_SESSION_TIMES_OF_BOT15)));
        // Set disabled filters
        String disabledFilterNames = UBIConfig.getUBIProperty(Property.DISABLED_FILTER_NAMES);
        if (StringUtils.isNotBlank(disabledFilterNames)) {
            conf.setString(Property.DISABLED_FILTER_NAMES, disabledFilterNames);
        }
    }
}