package com.ebay.sojourner.ubd.util;

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
        String configFile = params.get("config","./src/main/resources/ubi.properties");
        executionEnvironment.registerCachedFile(configFile,"configFile");

        String iframePageIds = params.get("lookup/iframePageIds","./src/main/resources/lookup/iframePageIds");
        executionEnvironment.registerCachedFile(iframePageIds, ubiConfig.getUBIProperty(Property.IFRAME_PAGE_IDS));

        String findingFlag = params.get("lookup/findingFlag","./src/main/resources/lookup/findingFlag");
        executionEnvironment.registerCachedFile(findingFlag, ubiConfig.getUBIProperty(Property.FINDING_FLAGS));

        String vtNewIdSource = params.get("lookup/vtNewIdSource","./src/main/resources/lookup/vtNewIdSource");
        executionEnvironment.registerCachedFile(vtNewIdSource, ubiConfig.getUBIProperty(Property.VTNEW_IDS));

        String iabAgentRex = params.get("lookup/iabAgentRex","./src/main/resources/lookup/iabAgentRex");
        executionEnvironment.registerCachedFile(iabAgentRex, ubiConfig.getUBIProperty(Property.IAB_AGENT));

        String appid = params.get("lookup/appid","./src/main/resources/lookup/appid");
        executionEnvironment.registerCachedFile(appid, ubiConfig.getUBIProperty(Property.APP_ID));


        String testUserIds = params.get("lookup/testUserIds","./src/main/resources/lookup/testUserIds");
        executionEnvironment.registerCachedFile(testUserIds, ubiConfig.getUBIProperty(Property.TEST_USER_IDS));

        String largeSessionGuid = params.get("lookup/largeSessionGuid","./src/main/resources/lookup/largeSessionGuid");
        executionEnvironment.registerCachedFile(largeSessionGuid, ubiConfig.getUBIProperty(Property.LARGE_SESSION_GUID));

        String pageFmly = params.get("lookup/pageFmly","./src/main/resources/lookup/pageFmly");
        executionEnvironment.registerCachedFile(pageFmly, ubiConfig.getUBIProperty(Property.PAGE_FMLY));

        String mpx = params.get("lookup/mpx","./src/main/resources/lookup/mpx");
        executionEnvironment.registerCachedFile(mpx, ubiConfig.getUBIProperty(Property.MPX_ROTATION));
    }
}
