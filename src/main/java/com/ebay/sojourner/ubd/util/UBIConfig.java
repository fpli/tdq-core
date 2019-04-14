package com.ebay.sojourner.ubd.util;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * @author kofeng
 * 
 */
public class UBIConfig {
    private static final Logger log = Logger.getLogger(UBIConfig.class);
    private static Properties ubiProperties;

    private UBIConfig() {
    }

    public static void initAppConfiguration(File ubiConfig) {
        ubiProperties = initProperties(ubiConfig);
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

    public static String getUBIProperty(String property) {
        return ubiProperties.getProperty(property);
    }
}
