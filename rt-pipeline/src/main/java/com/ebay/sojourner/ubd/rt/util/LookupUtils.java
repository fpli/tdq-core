package com.ebay.sojourner.ubd.rt.util;

import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class LookupUtils {
    /**
     * Upload lookup files to Flink distributed cache.
     * @param executionEnvironment
     * @param params
     * @param ubiConfig
     */
    public static void uploadFiles(
            StreamExecutionEnvironment executionEnvironment,
            ParameterTool params,
            UBIConfig ubiConfig) {
        String lookupBase = "/opt/sojourner-ubd/conf/lookup";
        String configFile = params.get("config","/opt/sojourner-ubd/conf/ubi.properties");
        executionEnvironment.registerCachedFile(configFile,"configFile");

//        String iframePageIds = params.get("lookup/iframePageIds",lookupBase +  "/iframePageIds");
//        executionEnvironment.registerCachedFile(iframePageIds, ubiConfig.getUBIProperty(Property.IFRAME_PAGE_IDS));
//
//        String findingFlag = params.get("lookup/findingFlag",lookupBase + "/findingFlag");
//        executionEnvironment.registerCachedFile(findingFlag, ubiConfig.getUBIProperty(Property.FINDING_FLAGS));
//
//        String vtNewIdSource = params.get("lookup/vtNewIdSource",lookupBase + "/vtNewIdSource");
//        executionEnvironment.registerCachedFile(vtNewIdSource, ubiConfig.getUBIProperty(Property.VTNEW_IDS));
//
//        String iabAgentRex = params.get("lookup/iabAgentRex",lookupBase + "/iabAgentRex");
//        executionEnvironment.registerCachedFile(iabAgentRex, ubiConfig.getUBIProperty(Property.IAB_AGENT));
//
//        String appid = params.get("lookup/appid",lookupBase + "/appid");
//        executionEnvironment.registerCachedFile(appid, ubiConfig.getUBIProperty(Property.APP_ID));
//
//
//        String testUserIds = params.get("lookup/testUserIds",lookupBase + "/testUserIds");
//        executionEnvironment.registerCachedFile(testUserIds, ubiConfig.getUBIProperty(Property.TEST_USER_IDS));
//
//        String largeSessionGuid = params.get("lookup/largeSessionGuid",lookupBase + "/largeSessionGuid");
//        executionEnvironment.registerCachedFile(largeSessionGuid, ubiConfig.getUBIProperty(Property.LARGE_SESSION_GUID));
//
//        String pageFmly = params.get("lookup/pageFmly",lookupBase + "/pageFmly");
//        executionEnvironment.registerCachedFile(pageFmly, ubiConfig.getUBIProperty(Property.PAGE_FMLY));
//
//        String mpx = params.get("lookup/mpx",lookupBase + "/mpx");
//        executionEnvironment.registerCachedFile(mpx, ubiConfig.getUBIProperty(Property.MPX_ROTATION));
    }
}
