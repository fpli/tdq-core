package com.ebay.sojourner.ubd.common.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class LkpManager {

  public static final String LKP_FILED_DELIMITER = ",";
  public static final String LKP_RECORD_DELIMITER = "\177";
  public static final String LKP_EMPTY_FIELD = "";
  public static final String TEXT_RECORD_DELIMITER = "\n";
  public static final String TEXT_FIELD_DELIMITER = "\t";
  public static final int PAIR_LENGTH = 2;
  private static volatile LkpManager lkpManager;
  public volatile HdfsLoader hdfsLoader;
  private Set<Integer> pageIdSet = new CopyOnWriteArraySet<>();
  // private static Set<String> pageIdSet4Bot12 = new HashSet<String>();
  private Map<Integer, Integer> findingFlagMap = new ConcurrentHashMap<>();
  private Map<Integer, Integer[]> vtNewIdsMap = new ConcurrentHashMap<Integer, Integer[]>();
  private Set<String> appIdWithBotFlags = new CopyOnWriteArraySet<String>();
  private List<String> iabAgentRegs = new CopyOnWriteArrayList<>();
  private Set<String> testUserIds = new CopyOnWriteArraySet<String>();
  private Set<String> largeSessionGuidSet = new CopyOnWriteArraySet<String>();
  private Map<Integer, String[]> pageFmlyMap = new ConcurrentHashMap<Integer, String[]>();
  private Map<String, String> mpxMap = new ConcurrentHashMap<String, String>();
  private Map<String, Boolean> selectedIps = new ConcurrentHashMap<String, Boolean>();
  private Set<String> selectedAgents = new CopyOnWriteArraySet<String>();
  private Map<String, String> result = new ConcurrentHashMap<String, String>();
  private volatile LkpFetcher lkpFetcher;

  public LkpManager() {

    lkpFetcher = new LkpFetcher(this);
    lkpFetcher.startDailyRefresh();
    hdfsLoader = new HdfsLoader();
    loadResources(true);
    hdfsLoader.closeFS();
  }

  public static LkpManager getInstance() {
    if (lkpManager == null) {
      synchronized (LkpManager.class) {
        if (lkpManager == null) {
          lkpManager = new LkpManager();
        }
      }
    }
    return lkpManager;
  }

  private void loadResources(boolean isInit) {
    loadIframePageIds(isInit);
    loadSelectedIps(isInit);
    loadSelectedAgents(isInit);
    loadLargeSessionGuid(isInit);
    loadIabAgent(isInit);
    loadFindingFlag(isInit);
    loadVtNewIds(isInit);
    loadAppIds(isInit);
    loadPageFmlys(isInit);
    loadMpxRotetion(isInit);
  }

  public void loadIframePageIds(boolean isInit) {
    Set<Integer> pageIdSetMid = new CopyOnWriteArraySet<>();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String iframePageIds = UBIConfig.getString(Property.IFRAME_PAGE_IDS);
    String pageIds = isTestEnabled ? iframePageIds : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), iframePageIds);
    if (StringUtils.isNotBlank(pageIds)) {
      for (String pageId : pageIds.split(LKP_RECORD_DELIMITER)) {
        try {
          pageIdSetMid.add(Integer.valueOf(pageId));
        } catch (NumberFormatException e) {
          log.warn("Parsing PageId failed, format incorrect...");
        }

      }
    } else {
      log.warn("Empty content for lookup table of iframe page ids");
    }
    pageIdSet = pageIdSetMid;

  }

  public void loadSelectedIps(boolean isInit) {
    Map<String, Boolean> selectedIpsMid = new ConcurrentHashMap<>();
    parseTextFile(Property.SELECTED_IPS, selectedIpsMid);
    selectedIps = selectedIpsMid;
  }

  public void loadSelectedAgents(boolean isInit) {
    Set<String> selectedAgentsMid = new CopyOnWriteArraySet<>();
    parseTextFile(Property.SELECTED_AGENTS, selectedAgentsMid);
    selectedAgents = selectedAgentsMid;
  }

  private void parseTextFile(String filePathProperty, Set<String> sets) {
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String file = UBIConfig.getString(filePathProperty);
    String fileContent = isTestEnabled ? file : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), file);
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

  private void parseTextFile(String filePathProperty, Map<String, Boolean> maps) {
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String file = UBIConfig.getString(filePathProperty);
    String fileContent = isTestEnabled ? file : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), file);
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
    System.out.println("map size:" + maps.size());
  }

  public void loadLargeSessionGuid(boolean isInit) {
    Set<String> largeSessionGuidSetMid = new CopyOnWriteArraySet<String>();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String largeSessionGuidValue = UBIConfig.getString(Property.LARGE_SESSION_GUID);
    String largeSessionGuids =
        isTestEnabled ? largeSessionGuidValue : hdfsLoader
            .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH),
                largeSessionGuidValue);
    if (StringUtils.isNotBlank(largeSessionGuids)) {
      for (String guid : largeSessionGuids.split(LKP_FILED_DELIMITER)) {
        if (StringUtils.isNotBlank(guid)) {
          largeSessionGuidSetMid.add(guid.trim());
        }
      }
    } else {
      log.warn("Empty content for lookup table of large session guid");
    }
    largeSessionGuidSet = largeSessionGuidSetMid;
  }

  public void loadIabAgent(boolean isInit) {
    List<String> iabAgentRegsMid = new CopyOnWriteArrayList<>();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String iabAgentReg = UBIConfig.getString(Property.IAB_AGENT);
    String iabAgentRegValue =
        isTestEnabled ? iabAgentReg : hdfsLoader
            .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), iabAgentReg);
    if (StringUtils.isNotBlank(iabAgentRegValue)) {
      for (String iabAgent : iabAgentRegValue.split(LKP_RECORD_DELIMITER)) {
        iabAgentRegsMid.add(iabAgent.toLowerCase());
      }
    } else {
      log.warn("Empty content for lookup table of iab agent info");
    }
    iabAgentRegs = iabAgentRegsMid;

  }

  public void loadFindingFlag(boolean isInit) {
    Map<Integer, Integer> findingFlagMapMid = new ConcurrentHashMap<>();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String findingFlag = UBIConfig.getString(Property.FINDING_FLAGS);
    String findingFlags = isTestEnabled ? findingFlag : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), findingFlag);
    if (StringUtils.isNotBlank(findingFlags)) {
      for (String pageFlag : findingFlags.split(LKP_RECORD_DELIMITER)) {
        String[] values = pageFlag.split(LKP_FILED_DELIMITER);
        // Keep the null judgment also for session metrics first finding flag
        if (values[0] != null && values[1] != null) {
          try {
            findingFlagMapMid.put(
                Integer.valueOf(values[0].trim()), Integer.valueOf(values[1].trim()));
          } catch (NumberFormatException e) {
            log.error(
                "Ignore the incorrect format for findflags: " + values[0] + " - " + values[1]);
          }
        }
      }
    } else {
      log.warn("Empty content for lookup table of finding flag");
    }
    findingFlagMap = findingFlagMapMid;

  }

  public void loadTestUserIds() {

  }

  public void loadVtNewIds(boolean isInit) {
    Map<Integer, Integer[]> vtNewIdsMapMid = new ConcurrentHashMap<Integer, Integer[]>();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String vtNewIds = UBIConfig.getString(Property.VTNEW_IDS);
    String vtNewIdsValue = isTestEnabled ? vtNewIds : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), vtNewIds);
    if (StringUtils.isNotBlank(vtNewIdsValue)) {
      for (String vtNewId : vtNewIdsValue.split(LKP_RECORD_DELIMITER)) {
        Integer[] pageInfo = new Integer[2];
        String[] ids = vtNewId.split(LKP_FILED_DELIMITER, pageInfo.length + 1);
        Integer newPageId = StringUtils.isEmpty(ids[0]) ? null : Integer.valueOf(ids[0].trim());
        pageInfo[0] = StringUtils.isEmpty(ids[1]) ? null : Integer.valueOf(ids[1].trim());
        pageInfo[1] = StringUtils.isEmpty(ids[2]) ? null : Integer.valueOf(ids[2].trim());
        vtNewIdsMapMid.put(newPageId, pageInfo);
      }
    } else {
      log.warn("Empty content for lookup table of vtNewIds");
    }
    vtNewIdsMap = vtNewIdsMapMid;

  }

  public void loadAppIds(boolean isInit) {
    Set<String> appIdWithBotFlagsMid = new CopyOnWriteArraySet();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String appIds = UBIConfig.getString(Property.APP_ID);
    String appIdAndFlags = isTestEnabled ? appIds : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), appIds);
    if (StringUtils.isNotBlank(appIdAndFlags)) {
      String[] appIdFlagPair = appIdAndFlags.split(LKP_RECORD_DELIMITER);
      for (String appIdFlag : appIdFlagPair) {
        if (StringUtils.isNotBlank(appIdFlag)) {
          appIdWithBotFlagsMid.add(appIdFlag.trim());
        }
      }
    } else {
      log.warn("Empty content for lookup table of app Ids");
    }
    appIdWithBotFlags = appIdWithBotFlagsMid;

  }

  public void loadPageFmlys(boolean isInit) {
    Map<Integer, String[]> pageFmlyMapMid = new ConcurrentHashMap<Integer, String[]>();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String pageFmlys = UBIConfig.getString(Property.PAGE_FMLY);
    String pageFmlysValue = isTestEnabled ? pageFmlys : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), pageFmlys);
    if (StringUtils.isNotBlank(pageFmlysValue)) {
      for (String pageFmlyPair : pageFmlysValue.split(LKP_RECORD_DELIMITER)) {
        String[] pageFmlyNames = new String[2];
        if (StringUtils.isNotBlank(pageFmlyPair)) {
          String[] values = pageFmlyPair.split(LKP_FILED_DELIMITER, pageFmlyNames.length + 1);
          Integer pageId = StringUtils.isEmpty(values[0]) ? null : Integer.valueOf(values[0]);
          pageFmlyNames[0] = StringUtils.isEmpty(values[1]) ? null : values[1];
          pageFmlyNames[1] = StringUtils.isEmpty(values[2]) ? null : values[2];
          pageFmlyMapMid.put(pageId, pageFmlyNames);
        }
      }
    } else {
      log.warn("Empty content for lookup table of page fmlys");
    }
    pageFmlyMap = pageFmlyMapMid;

  }

  public void loadLocally() throws Exception {
    result.put(
        Property.IFRAME_PAGE_IDS, FileLoader.loadContent(null, Resources.IFRAME_PAGE_SOURCE));
    result.put(Property.FINDING_FLAGS, FileLoader.loadContent(null, Resources.FINDING_FLAG_SOURCE));
    result.put(Property.VTNEW_IDS, FileLoader.loadContent(null, Resources.VT_NEWID_SOURCE));
    result.put(Property.IAB_AGENT, FileLoader.loadContent(null, Resources.IAB_AGENT_SOURCE));
    result.put(Property.APP_ID, FileLoader.loadContent(null, Resources.APP_ID_SOURCE));
    result.put(Property.TEST_USER_IDS, FileLoader.loadContent(null, Resources.TEST_USER_SOURCE));
    result.put(
        Property.LARGE_SESSION_GUID, FileLoader.loadContent(null, Resources.LARGE_SESSION_SOURCE));
    result.put(Property.PAGE_FMLY, FileLoader.loadContent(null, Resources.PAGE_FMLY_NAME));
    result.put(Property.MPX_ROTATION, FileLoader.loadContent(null, Resources.MPX_ROTATION_SOURCE));
    result.put(Property.SELECTED_IPS, FileLoader.loadContent(null, Resources.SELECTED_IPS));
    result.put(Property.SELECTED_AGENTS, FileLoader.loadContent(null, Resources.SELECTED_AGENTS));
  }

  public void loadMpxRotetion(boolean isInit) {
    Map<String, String> mpxMapMid = new ConcurrentHashMap<String, String>();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String mpxRotation = UBIConfig.getString(Property.MPX_ROTATION);
    String mpxRotations = isTestEnabled ? mpxRotation : hdfsLoader
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), mpxRotation);

    if (StringUtils.isNotBlank(mpxRotations)) {
      for (String mpx : mpxRotations.split(LKP_RECORD_DELIMITER)) {
        String[] values = mpx.split(LKP_FILED_DELIMITER);
        // Keep the null judgment also for session metrics first finding flag
        if (values[0] != null && values[1] != null) {
          try {
            //                            mpxMap.put(Long.parseLong(values[0].trim()),
            // String.valueOf(values[1].trim()));
            mpxMapMid.put(values[0].trim(), values[1].trim());
          } catch (NumberFormatException e) {
            log.error("Ignore the incorrect format for mpx: " + values[0] + " - " + values[1]);
          }
        }
      }
    } else {
      log.warn("Empty content for lookup table of mpx rotation.");
    }
    mpxMap = mpxMapMid;
  }

  public Set<Integer> getIframePageIdSet() {
    return pageIdSet;
  }

  //    public static Set<String> getIframepageIdSet4Bot12() {
  //        return pageIdSet4Bot12;
  //    }
  public Map<Integer, Integer> getFindingFlagMap() {
    return findingFlagMap;
  }

  public Map<Integer, Integer[]> getVtNewIdsMap() {
    return vtNewIdsMap;
  }

  public List<String> getIabAgentRegs() {
    return iabAgentRegs;
  }

  public Set<String> getAppIds() {
    return appIdWithBotFlags;
  }

  public Set<String> getTestUserIds() {
    return testUserIds;
  }

  public Map<Integer, String[]> getPageFmlyMaps() {
    return pageFmlyMap;
  }

  public Map<String, Boolean> getSelectedIps() {
    return selectedIps;
  }

  public Set<String> getSelectedAgents() {
    return selectedAgents;
  }

  public Map<String, String> getResult() {
    return result;
  }

  public void clearAppId() {
    appIdWithBotFlags.clear();
  }

  public void cleanTestUserIds() {
    testUserIds.clear();
  }

  public void clearIabAgent() {
    iabAgentRegs.clear();
  }

  public void clearPageFmlyName() {
    pageFmlyMap.clear();
  }

  public void clearSelectedIps() {
    selectedIps.clear();
  }

  public Set<String> getLargeSessionGuid() {
    return largeSessionGuidSet;
  }

  public Map<String, String> getMpxMap() {
    return mpxMap;
  }

  public void clearMpxMap() {
    mpxMap.clear();
  }
}
