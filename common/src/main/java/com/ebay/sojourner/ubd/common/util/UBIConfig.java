package com.ebay.sojourner.ubd.common.util;


import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author kofeng
 */
public class UBIConfig {
    private static final Logger log = Logger.getLogger(UBIConfig.class);
    private static UBIConfig ubiConfig;
    private Properties ubiProperties;
    private HashMap<String, Object> confData = new HashMap<String, Object>();

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    private boolean isInitialized = false;

    private UBIConfig(File configFile) {
        initAppConfiguration(configFile);
        try {
            initConfiguration(false);
            setConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static UBIConfig getInstance(File configFile) {
        if (ubiConfig == null) {
            synchronized (UBIConfig.class) {
                if (ubiConfig == null) {
                    ubiConfig = new UBIConfig(configFile);
                }
            }
        }
        return ubiConfig;
    }

    public void initAppConfiguration(File ubiConfig) {

        this.ubiProperties = initProperties(ubiConfig);
    }

    protected static Properties initProperties(String filePath, String resource) {
        try {
            return PropertyUtils.loadInProperties(filePath, resource);
        } catch (FileNotFoundException e) {
            log.error("Either unable to load resource either from " + filePath);
            log.error("Or unable to load from source from " + resource);
            throw new RuntimeException(e);
        }
    }

    protected static Properties initProperties(File filePath) {
        try {
            return PropertyUtils.loadInProperties(filePath);
        } catch (FileNotFoundException e) {
            log.error("Either unable to load resource either from " + filePath.getName());
            throw new RuntimeException(e);
        }
    }

    public void setBoolean(String key, Boolean value) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        synchronized (this.confData) {
            confData.put(key, value);
        }
    }

    public void setString(String key, String value) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        synchronized (confData) {
            confData.put(key, value);
        }
    }

    public void setLong(String key, Long value) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        synchronized (confData) {
            confData.put(key, value);
        }
    }

    public String getString(String key) {
        Object o = getRawValue(key);
        if (o == null) {
            return null;
        } else {
            return o.toString();
        }
    }

    public Boolean getBoolean(String key, Boolean bool) {
        Object o = getRawValue(key);
        if (o == null) {
            return bool;
        } else {
            return Boolean.valueOf(o.toString());
        }
    }

    private Object getRawValue(String key) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }

        synchronized (confData) {
            return confData.get(key);
        }
    }

    public void initConfiguration(boolean enableTest) throws Exception {


        if (this.getUBIProperty(Property.LOG_LEVEL) != null) {
            this.setString(Property.LOG_LEVEL, this.getUBIProperty(Property.LOG_LEVEL));
        }

        if (enableTest) {
            this.setBoolean(Property.IS_TEST_ENABLE, true);
            loadLookupTableLocally();
        } else {
            this.setString(Property.IFRAME_PAGE_IDS, this.getUBIProperty(Property.IFRAME_PAGE_IDS));

            this.setString(Property.FINDING_FLAGS, this.getUBIProperty(Property.FINDING_FLAGS));
            this.setString(Property.VTNEW_IDS, this.getUBIProperty(Property.VTNEW_IDS));
            this.setString(Property.IAB_AGENT, this.getUBIProperty(Property.IAB_AGENT));
            this.setString(Property.APP_ID, this.getUBIProperty(Property.APP_ID));
            this.setString(Property.TEST_USER_IDS, this.getUBIProperty(Property.TEST_USER_IDS));
            this.setString(Property.LARGE_SESSION_GUID, this.getUBIProperty(Property.LARGE_SESSION_GUID));
            this.setString(Property.PAGE_FMLY, this.getUBIProperty(Property.PAGE_FMLY));
            this.setString(Property.MPX_ROTATION, this.getUBIProperty(Property.MPX_ROTATION));

//            conf.setString(Property.IFRAME_PAGE_IDS4Bot12, this.getUBIProperty(Property.IFRAME_PAGE_IDS4Bot12));
            this.setString(Property.SELECTED_IPS, this.getUBIProperty(Property.SELECTED_IPS));
            this.setString(Property.SELECTED_AGENTS, this.getUBIProperty(Property.SELECTED_AGENTS));

        }
    }

    protected void loadLookupTableLocally() throws Exception {
        LkpFetcher fetcher = new LkpFetcher();
        fetcher.loadLocally();
        for (String key : fetcher.getResult().keySet()) {
            if (null == this.getString(key)) {
                String value = fetcher.getResult().get(key);
                this.setString(key, value);
            }
        }
    }

    public void setConfiguration() throws Exception {

        // Set session properties
        this.setLong(Property.EVENT_WAITING_PERIOD, Long.valueOf(this.getUBIProperty(Property.EVENT_WAITING_PERIOD)) * SOJTS2Date.MILLI2MICRO);
        this.setLong(Property.SESSION_IDLE_TIMEOUT, Long.valueOf(this.getUBIProperty(Property.SESSION_IDLE_TIMEOUT)) * SOJTS2Date.MILLI2MICRO);
        this.setLong(Property.SESSION_MAX_DURATION, Long.valueOf(this.getUBIProperty(Property.SESSION_MAX_DURATION)) * SOJTS2Date.MILLI2MICRO);
        this.setLong(Property.EVENT_DELAY_THRESHOLD, Long.valueOf(this.getUBIProperty(Property.EVENT_DELAY_THRESHOLD)) * SOJTS2Date.MILLI2MICRO);


        // Set event properties
        this.setLong(Property.EVENT_POOL_THRESHOLD, Long.valueOf(this.getUBIProperty(Property.EVENT_POOL_THRESHOLD)));
        // Set Cobrand properties
        this.setString(Property.EXPRESS_SITE, this.getUBIProperty(Property.EXPRESS_SITE));
        this.setString(Property.HALF_SITE, this.getUBIProperty(Property.HALF_SITE));
        this.setString(Property.EXPRESS_PARTNER, this.getUBIProperty(Property.EXPRESS_PARTNER));
        this.setString(Property.SHOPPING_PARTNER, this.getUBIProperty(Property.SHOPPING_PARTNER));
        this.setString(Property.HALF_PARTNER, this.getUBIProperty(Property.HALF_PARTNER));
        this.setString(Property.ARTISAN_PARTNER, this.getUBIProperty(Property.ARTISAN_PARTNER));
        this.setString(Property.MOBILE_AGENT_START, this.getUBIProperty(Property.MOBILE_AGENT_START));
        this.setString(Property.MOBILE_AGENT_INDEX, this.getUBIProperty(Property.MOBILE_AGENT_INDEX));
        this.setString(Property.MOBILE_AGENT_OTHER, this.getUBIProperty(Property.MOBILE_AGENT_OTHER));
        // Set page indicators
        this.setString(Property.SEARCH_VIEW_PAGES, this.getUBIProperty(Property.SEARCH_VIEW_PAGES));
        this.setString(Property.VIEW_ITEM_PAGES, this.getUBIProperty(Property.VIEW_ITEM_PAGES));
        this.setString(Property.BID_PAGES, this.getUBIProperty(Property.BID_PAGES));
        this.setString(Property.BIN_PAGES, this.getUBIProperty(Property.BIN_PAGES));
        this.setString(Property.CAPTCHA_PAGES, this.getUBIProperty(Property.CAPTCHA_PAGES));
        this.setString(Property.HALF_PAGES, this.getUBIProperty(Property.HALF_PAGES));
        this.setString(Property.CORESITE_PAGES, this.getUBIProperty(Property.CORESITE_PAGES));
        this.setString(Property.CLASSIFIED_PAGES, this.getUBIProperty(Property.CLASSIFIED_PAGES));
        // New metrics need page list
        this.setString(Property.ROVER_PAGES, this.getUBIProperty(Property.ROVER_PAGES));
        this.setString(Property.LAND_PAGES1, this.getUBIProperty(Property.LAND_PAGES1));
        this.setString(Property.LAND_PAGES2, this.getUBIProperty(Property.LAND_PAGES2));
        this.setString(Property.SCEVENT_EXCLUDE_PAGES1, this.getUBIProperty(Property.SCEVENT_EXCLUDE_PAGES1));
        this.setString(Property.SCEVENT_EXCLUDE_PAGES2, this.getUBIProperty(Property.SCEVENT_EXCLUDE_PAGES2));
        this.setString(Property.AGENT_EXCLUDE_PAGES, this.getUBIProperty(Property.AGENT_EXCLUDE_PAGES));
        this.setString(Property.NOTIFY_CLICK_PAGES, this.getUBIProperty(Property.NOTIFY_CLICK_PAGES));
        this.setString(Property.NOTIFY_VIEW_PAGES, this.getUBIProperty(Property.NOTIFY_VIEW_PAGES));
        this.setString(Property.LNDG_PAGE_IDS, this.getUBIProperty(Property.LNDG_PAGE_IDS));
        this.setString(Property.IP_EXCLUDE_PAGES, this.getUBIProperty(Property.IP_EXCLUDE_PAGES));
        this.setString(Property.EXCLUDE_IP_PATTERN, this.getUBIProperty(Property.EXCLUDE_IP_PATTERN));
        this.setString(Property.EBAY_SITE_COBRAND, this.getUBIProperty(Property.EBAY_SITE_COBRAND));
        this.setString(Property.INVALID_BOT_FILTER, this.getUBIProperty(Property.INVALID_BOT_FILTER));
        // Extra metrics for page list
        this.setString(Property.INVALID_PAGE_IDS, this.getUBIProperty(Property.INVALID_PAGE_IDS));
        // Set APP PAYLOAD KV Property
        this.setString(Property.SWD_VALUES, this.getUBIProperty(Property.SWD_VALUES));
        this.setString(Property.ROT_VALUES, this.getUBIProperty(Property.ROT_VALUES));
        this.setString(Property.VI_EVENT_VALUES, this.getUBIProperty(Property.VI_EVENT_VALUES));
        // Set APP ID property
        this.setString(Property.MOBILE_APP, this.getUBIProperty(Property.MOBILE_APP));
        this.setString(Property.DESKTOP_APP, this.getUBIProperty(Property.DESKTOP_APP));
        this.setString(Property.EIM_APP, this.getUBIProperty(Property.EIM_APP));
        // Set BOT rules
        this.setString(Property.BOT_RULE_PACKAGE, this.getUBIProperty(Property.BOT_RULE_PACKAGE));
        this.setString(Property.BOT_RULE_CLASSES, this.getUBIProperty(Property.BOT_RULE_CLASSES));
        // Set large session properties
        this.setLong(Property.LARGE_SESSION_EVENT_NUMBER, Long.valueOf(this.getUBIProperty(Property.LARGE_SESSION_EVENT_NUMBER)));
        this.setLong(Property.LARGE_SESSION_TIMES_OF_BOT15, Long.valueOf(this.getUBIProperty(Property.LARGE_SESSION_TIMES_OF_BOT15)));
        // Set disabled filters
        String disabledFilterNames = this.getUBIProperty(Property.DISABLED_FILTER_NAMES);
        if (StringUtils.isNotBlank(disabledFilterNames)) {
            this.setString(Property.DISABLED_FILTER_NAMES, disabledFilterNames);
        }

    }

    public String getUBIProperty(String property) {
        return ubiProperties.getProperty(property);
    }
}
