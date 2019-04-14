package com.ebay.sojourner.ubd.util;


import com.ebay.sojourner.ubd.constant.Property;
import com.ebay.sojourner.ubd.constant.Resources;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.log4j.Logger;

import java.util.*;

public class LkpFetcher {
    public static final Logger log = Logger.getLogger(LkpFetcher.class);
    public static final String LKP_FILED_DELIMITER = ",";
    public static final String LKP_RECORD_DELIMITER = "\177";
    public static final String LKP_EMPTY_FIELD = "";
    public static final String TEXT_RECORD_DELIMITER = "\n";
    public static final String TEXT_FIELD_DELIMITER = "\t";
    public static final int PAIR_LENGTH = 2;

    private static Set<String> pageIdSet = new HashSet<String>();
   // private static Set<String> pageIdSet4Bot12 = new HashSet<String>();
    private static Map<Integer, Integer> findingFlagMap = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer[]> vtNewIdsMap = new HashMap<Integer, Integer[]>();
    private static Set<String> appIdWithBotFlags = new HashSet<String>();
    private static List<String> iabAgentRegs = new ArrayList<String>();
    private static Set<String> testUserIds = new HashSet<String>();
    private static Set<String> largeSessionGuidSet = new HashSet<String>();
    private static Map<Integer, String[]> pageFmlyMap = new HashMap<Integer, String[]>();
    private static Map<Long, String> mpxMap = new HashMap<Long, String>();
    private static Map<String, Boolean> selectedIps = new HashMap<String, Boolean>();
    private static Set<String> selectedAgents = new HashSet<String>();

    private Map<String, String> result;

    public LkpFetcher() {
        result = new HashMap<String, String>();
    }

    public static void loadIframePageIds(Configuration conf, RuntimeContext runtimeContext) {
        if (pageIdSet.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String iframePageIds = conf.getString(Property.IFRAME_PAGE_IDS,null);
            String pageIds = isTestEnabled ? iframePageIds :  FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(iframePageIds));
            if (StringUtils.isNotBlank(pageIds)) {
                for (String pageId : pageIds.split(LKP_RECORD_DELIMITER)) {
                    pageIdSet.add(pageId);
                }
            } else {
                log.warn("Empty content for lookup table of iframe page ids");
            }
        }
    }

    public static void loadSelectedIps(Configuration conf) {
        parseTextFile(Property.SELECTED_IPS, selectedIps, conf);
    }

    public static void loadSelectedAgents(Configuration conf) {
        parseTextFile(Property.SELECTED_AGENTS, selectedAgents, conf);
    }

    private static void parseTextFile(String filePathProperty, Set<String> sets, Configuration conf) {
        if (sets.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String file = conf.getString(filePathProperty,null);
            String fileContent = isTestEnabled ? file : FileLoader.loadContent(file, null);
            if (StringUtils.isNotBlank(fileContent)) {
                for (String record : fileContent.split(TEXT_RECORD_DELIMITER)) {
                    if (StringUtils.isNotBlank(record)) {
                        String[] recordPair = record.split(TEXT_FIELD_DELIMITER);
                        String recordKey = recordPair[0];
                        if (StringUtils.isNotBlank(recordKey)) {
                            sets.add(recordKey.trim());
                        }
                    }
                }
            } else {
                log.warn("Empty content for lookup table of sets: " + filePathProperty);
            }
        }
    }
    
    private static void parseTextFile(String filePathProperty, Map<String, Boolean> maps, Configuration conf) {
        if (maps.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String file = conf.getString(filePathProperty,null);
            String fileContent = isTestEnabled ? file : FileLoader.loadContent(file, null);
            if (StringUtils.isNotBlank(fileContent)) {
                for (String record : fileContent.split(TEXT_RECORD_DELIMITER)) {
                    if (StringUtils.isNotBlank(record)) {
                        String[] recordPair = record.split(TEXT_FIELD_DELIMITER);
                        if (recordPair.length == PAIR_LENGTH) {
                            String recordKey = recordPair[0];
                            String recordValue = recordPair[1];
                            if (StringUtils.isNotBlank(recordKey) && StringUtils.isNotBlank(recordValue)) {
                                maps.put(recordKey.trim(), Boolean.valueOf(recordValue.trim()));
                            }
                        }
                    }
                }
            } else {
                log.warn("Empty content for lookup table of sets: " + filePathProperty);
            }
        }
    }

    public static void loadLargeSessionGuid(Configuration conf, RuntimeContext runtimeContext) {
        if (largeSessionGuidSet.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String largeSessionGuidValue = conf.getString(Property.LARGE_SESSION_GUID,null);
            String largeSessionGuids = isTestEnabled ? largeSessionGuidValue :  FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(largeSessionGuidValue));
            if (StringUtils.isNotBlank(largeSessionGuids)) {
                for (String guid : largeSessionGuids.split(LKP_FILED_DELIMITER)) {
                    if (StringUtils.isNotBlank(guid)) {
                        largeSessionGuidSet.add(guid.trim());
                    }
                }
            } else {
                log.warn("Empty content for lookup table of large session guid");
            }
        }
    }

    public static void loadIabAgent(Configuration conf , RuntimeContext runtimeContext) {
        if (iabAgentRegs.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String iabAgentReg = conf.getString(Property.IAB_AGENT,null);
            String iabAgentRegValue = isTestEnabled ? iabAgentReg : FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(iabAgentReg));
            if (StringUtils.isNotBlank(iabAgentRegValue)) {
                for (String iabAgent : iabAgentRegValue.split(LKP_RECORD_DELIMITER)) {
                    iabAgentRegs.add(iabAgent.toLowerCase());
                }
            } else {
                log.warn("Empty content for lookup table of iab agent info");
            }
        }
    }

    public static void loadFindingFlag(Configuration conf, RuntimeContext runtimeContext) {
        if (findingFlagMap.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String findingFlag = conf.getString(Property.FINDING_FLAGS,null);
            String findingFlags = isTestEnabled ? findingFlag : FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(findingFlag));
            if (StringUtils.isNotBlank(findingFlags)) {
                for (String pageFlag : findingFlags.split(LKP_RECORD_DELIMITER)) {
                    String[] values = pageFlag.split(LKP_FILED_DELIMITER);
                    // Keep the null judgment also for session metrics first finding flag
                    if (values[0] != null && values[1] != null) {
                        try {
                            findingFlagMap.put(Integer.valueOf(values[0].trim()), Integer.valueOf(values[1].trim()));
                        } catch (NumberFormatException e) {
                            log.error("Ignore the incorrect format for findflags: " + values[0] + " - " + values[1]);
                        }
                    }
                }
            } else {
                log.warn("Empty content for lookup table of finding flag");
            }
        }
    }

    public static void loadTestUserIds(Configuration conf, RuntimeContext runtimeContext) {
        if (testUserIds.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String testUserIdsValue = conf.getString(Property.TEST_USER_IDS,null);
            String userIdsToFilter = isTestEnabled ? testUserIdsValue : FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(testUserIdsValue));
            if (StringUtils.isNotBlank(userIdsToFilter)) {
                for (String userId : userIdsToFilter.split(LKP_RECORD_DELIMITER)) {
                    if (StringUtils.isNotBlank(userId)) {
                        testUserIds.add(userId.trim());
                    }
                }
            } else {
                log.error("Empty content for lookup table of test user ids");
            }
        }
    }

    public static void loadVtNewIds(Configuration conf, RuntimeContext runtimeContext) {
        if (vtNewIdsMap.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String vtNewIds = conf.getString(Property.VTNEW_IDS,null);
            String vtNewIdsValue = isTestEnabled ? vtNewIds :  FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(vtNewIds));
            if (StringUtils.isNotBlank(vtNewIdsValue)) {
                for (String vtNewId : vtNewIdsValue.split(LKP_RECORD_DELIMITER)) {
                    Integer[] pageInfo = new Integer[2];
                    String[] ids = vtNewId.split(LKP_FILED_DELIMITER, pageInfo.length + 1);
                    Integer newPageId = StringUtils.isEmpty(ids[0]) ? null : Integer.valueOf(ids[0].trim());
                    pageInfo[0] = StringUtils.isEmpty(ids[1]) ? null : Integer.valueOf(ids[1].trim());
                    pageInfo[1] = StringUtils.isEmpty(ids[2]) ? null : Integer.valueOf(ids[2].trim());
                    vtNewIdsMap.put(newPageId, pageInfo);
                }
            } else {
                log.warn("Empty content for lookup table of vtNewIds");
            }
        }
    }

    public static void loadAppIds(Configuration conf, RuntimeContext runtimeContext) {
        if (appIdWithBotFlags.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String appIds = conf.getString(Property.APP_ID,null);
            String appIdAndFlags = isTestEnabled ? appIds :  FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(appIds));
            if (StringUtils.isNotBlank(appIdAndFlags)) {
                String[] appIdFlagPair = appIdAndFlags.split(LKP_RECORD_DELIMITER);
                for (String appIdFlag : appIdFlagPair) {
                    if (StringUtils.isNotBlank(appIdFlag)) {
                        appIdWithBotFlags.add(appIdFlag.trim());
                    }
                }
            } else {
                log.warn("Empty content for lookup table of app Ids");
            }
        }
    }

    public static void loadPageFmlys(Configuration conf, RuntimeContext runtimeContext) {
        if (pageFmlyMap.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String pageFmlys = conf.getString(Property.PAGE_FMLY,null);
            String pageFmlysValue = isTestEnabled ? pageFmlys : FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(pageFmlys));
            if (StringUtils.isNotBlank(pageFmlysValue)) {
                for (String pageFmlyPair : pageFmlysValue.split(LKP_RECORD_DELIMITER)) {
                    String[] pageFmlyNames = new String[2];
                    if (StringUtils.isNotBlank(pageFmlyPair)) {
                        String[] values = pageFmlyPair.split(LKP_FILED_DELIMITER, pageFmlyNames.length + 1);
                        Integer pageId = StringUtils.isEmpty(values[0]) ? null : Integer.valueOf(values[0]);
                        pageFmlyNames[0] = StringUtils.isEmpty(values[1]) ? null : values[1];
                        pageFmlyNames[1] = StringUtils.isEmpty(values[2]) ? null : values[2];
                        pageFmlyMap.put(pageId, pageFmlyNames);
                    }
                }
            } else {
                log.warn("Empty content for lookup table of page fmlys");
            }
        }
    }

    public void loadLocally() throws Exception {
        result.put(Property.IFRAME_PAGE_IDS, FileLoader.loadContent(null, Resources.IFRAME_PAGE_SOURCE));
        result.put(Property.FINDING_FLAGS, FileLoader.loadContent(null, Resources.FINDING_FLAG_SOURCE));
        result.put(Property.VTNEW_IDS, FileLoader.loadContent(null, Resources.VT_NEWID_SOURCE));
        result.put(Property.IAB_AGENT, FileLoader.loadContent(null, Resources.IAB_AGENT_SOURCE));
        result.put(Property.APP_ID, FileLoader.loadContent(null, Resources.APP_ID_SOURCE));
        result.put(Property.TEST_USER_IDS, FileLoader.loadContent(null, Resources.TEST_USER_SOURCE));
        result.put(Property.LARGE_SESSION_GUID, FileLoader.loadContent(null, Resources.LARGE_SESSION_SOURCE));
        result.put(Property.PAGE_FMLY, FileLoader.loadContent(null, Resources.PAGE_FMLY_NAME));
        result.put(Property.MPX_ROTATION, FileLoader.loadContent(null, Resources.MPX_ROTATION_SOURCE));
        result.put(Property.SELECTED_IPS, FileLoader.loadContent(null, Resources.SELECTED_IPS));
        result.put(Property.SELECTED_AGENTS, FileLoader.loadContent(null, Resources.SELECTED_AGENTS));
    }

    public static void loadMpxRotetion(Configuration conf, RuntimeContext runtimeContext) {
        if (mpxMap.isEmpty()) {
            boolean isTestEnabled = conf.getBoolean(Property.IS_TEST_ENABLE, false);
            String mpxRotation = conf.getString(Property.MPX_ROTATION,null);
            String mpxRotations = isTestEnabled ? mpxRotation : FileLoader.loadContent(runtimeContext.getDistributedCache().getFile(mpxRotation));

            if (StringUtils.isNotBlank(mpxRotations)) {
                for (String mpx : mpxRotations.split(LKP_RECORD_DELIMITER)) {
                    String[] values = mpx.split(LKP_FILED_DELIMITER);
                    // Keep the null judgment also for session metrics first finding flag
                    if (values[0] != null && values[1] != null) {
                        try {
                            mpxMap.put(Long.parseLong(values[0].trim()), String.valueOf(values[1].trim()));
                        } catch (NumberFormatException e) {
                            log.error("Ignore the incorrect format for mpx: " + values[0] + " - " + values[1]);
                        }
                    }
                }
            } else {
                log.warn("Empty content for lookup table of mpx rotation.");
            }
        }
    }

    public static Set<String> getIframePageIdSet() {
        return pageIdSet;
    }

//    public static Set<String> getIframepageIdSet4Bot12() {
//        return pageIdSet4Bot12;
//    }
    public static Map<Integer, Integer> getFindingFlagMap() {
        return findingFlagMap;
    }

    public static Map<Integer, Integer[]> getVtNewIdsMap() {
        return vtNewIdsMap;
    }

    public static List<String> getIabAgentRegs() {
        return iabAgentRegs;
    }

    public static Set<String> getAppIds() {
        return appIdWithBotFlags;
    }

    public static Set<String> getTestUserIds() {
        return testUserIds;
    }

    public static Map<Integer, String[]> getPageFmlyMaps() {
        return pageFmlyMap;
    }

    public static Map<String, Boolean> getSelectedIps() {
        return selectedIps;
    }

    public static Set<String> getSelectedAgents() {
        return selectedAgents;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public static void clearAppId() {
        appIdWithBotFlags.clear();
    }

    public static void cleanTestUserIds() {
        testUserIds.clear();
    }

    public static void clearIabAgent() {
        iabAgentRegs.clear();
    }

    public static void clearPageFmlyName() {
        pageFmlyMap.clear();
    }

    public static void clearSelectedIps() {
        selectedIps.clear();
    }

    public static Set<String> getLargeSessionGuid() {
        return largeSessionGuidSet;
    }

    public static Map<Long, String> getMpxMap() {
        return mpxMap;
    }

    public static void clearMpxMap() {
        mpxMap.clear();
    }
}
