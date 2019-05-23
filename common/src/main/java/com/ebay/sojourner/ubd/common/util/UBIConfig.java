package com.ebay.sojourner.ubd.common.util;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author kofeng
 * 
 */
public class UBIConfig {
    private static final Logger log = Logger.getLogger(UBIConfig.class);
    private static UBIConfig ubiConfig ;
    private Properties ubiProperties;
    private HashMap<String, Object> confData = new HashMap<String,Object>();

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    private boolean isInitialized=false;
    private UBIConfig() {

    }
    public static UBIConfig getInstance() {
        if (ubiConfig == null) {
            synchronized (UBIConfig.class) {
                if (ubiConfig == null) {
                    ubiConfig = new UBIConfig();
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

    public void setBoolean(String key,Boolean value)
    {
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

    public void setString(String key,String value)
    {
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

    public void setLong(String key,Long value)
    {
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

    private Object getRawValue(String key) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }

        synchronized (confData) {
            return confData.get(key);
        }
    }


    public String getUBIProperty(String property) {
        return ubiProperties.getProperty(property);
    }
}
