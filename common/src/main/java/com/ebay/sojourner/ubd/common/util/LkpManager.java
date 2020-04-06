package com.ebay.sojourner.ubd.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

  private static Set<Integer> pageIdSet = new HashSet<Integer>();
  // private static Set<String> pageIdSet4Bot12 = new HashSet<String>();
  private static Map<Integer, Integer> findingFlagMap = new HashMap<Integer, Integer>();
  private static Map<Integer, Integer[]> vtNewIdsMap = new HashMap<Integer, Integer[]>();
  private static Set<String> appIdWithBotFlags = new HashSet<String>();
  private static List<String> iabAgentRegs = new ArrayList<String>();
  private static Set<String> testUserIds = new HashSet<String>();
  private static Set<String> largeSessionGuidSet = new HashSet<String>();
  private static Map<Integer, String[]> pageFmlyMap = new HashMap<Integer, String[]>();
  private static Map<String, String> mpxMap = new HashMap<String, String>();
  private static Map<String, Boolean> selectedIps = new HashMap<String, Boolean>();
  private static Set<String> selectedAgents = new HashSet<String>();
  private static volatile LkpManager lkpManager;
  private Map<String, String> result = new HashMap<String, String>();
  private volatile LkpFetcher lkpFetcher;

  private LkpManager() {
    lkpFetcher = new LkpFetcher(this);
    lkpFetcher.startDailyRefresh();
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

  private void loadResources() {
    loadIframePageIds();
    loadSelectedIps();
    loadSelectedAgents();
    loadLargeSessionGuid();
    loadIabAgent();
    loadFindingFlag();
    loadTestUserIds();
    loadVtNewIds();
    loadAppIds();
    loadPageFmlys();
    loadMpxRotetion();
  }

  public void loadIframePageIds() {
    pageIdSet.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String iframePageIds = UBIConfig.getString(Property.IFRAME_PAGE_IDS);
    String pageIds = isTestEnabled ? iframePageIds : HdfsLoader.getInstance()
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), iframePageIds);
    if (StringUtils.isNotBlank(pageIds)) {
      for (String pageId : pageIds.split(LKP_RECORD_DELIMITER)) {
        try {
          pageIdSet.add(Integer.valueOf(pageId));
        } catch (NumberFormatException e) {
          log.warn("Parsing PageId failed, format incorrect...");
        }

      }
    } else {
      log.warn("Empty content for lookup table of iframe page ids");
    }

  }

  public void loadSelectedIps() {
    parseTextFile(Property.SELECTED_IPS, selectedIps);
  }

  public void loadSelectedAgents() {
    parseTextFile(Property.SELECTED_AGENTS, selectedAgents);
  }

  private void parseTextFile(String filePathProperty, Set<String> sets) {
    sets.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String file = UBIConfig.getString(filePathProperty);
    String fileContent = isTestEnabled ? file : HdfsLoader.getInstance()
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
    maps.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String file = UBIConfig.getString(filePathProperty);
    String fileContent = isTestEnabled ? file : HdfsLoader.getInstance()
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

  }

  public void loadLargeSessionGuid() {
    largeSessionGuidSet.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String largeSessionGuidValue = UBIConfig.getString(Property.LARGE_SESSION_GUID);
    String largeSessionGuids =
        isTestEnabled ? largeSessionGuidValue : HdfsLoader.getInstance()
            .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH),
                largeSessionGuidValue);
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

  public void loadIabAgent() {
    iabAgentRegs.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String iabAgentReg = UBIConfig.getString(Property.IAB_AGENT);
    String iabAgentRegValue =
        isTestEnabled ? iabAgentReg : HdfsLoader.getInstance()
            .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), iabAgentReg);
    if (StringUtils.isNotBlank(iabAgentRegValue)) {
      for (String iabAgent : iabAgentRegValue.split(LKP_RECORD_DELIMITER)) {
        iabAgentRegs.add(iabAgent.toLowerCase());
      }
    } else {
      log.warn("Empty content for lookup table of iab agent info");
    }

  }

  public void loadFindingFlag() {
    findingFlagMap.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String findingFlag = UBIConfig.getString(Property.FINDING_FLAGS);
    String findingFlags = isTestEnabled ? findingFlag : HdfsLoader.getInstance()
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), findingFlag);
    if (StringUtils.isNotBlank(findingFlags)) {
      for (String pageFlag : findingFlags.split(LKP_RECORD_DELIMITER)) {
        String[] values = pageFlag.split(LKP_FILED_DELIMITER);
        // Keep the null judgment also for session metrics first finding flag
        if (values[0] != null && values[1] != null) {
          try {
            findingFlagMap.put(
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

  }

  public void loadTestUserIds() {

  }

  public void loadVtNewIds() {
    vtNewIdsMap.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String vtNewIds = UBIConfig.getString(Property.VTNEW_IDS);
    String vtNewIdsValue = isTestEnabled ? vtNewIds : HdfsLoader.getInstance()
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), vtNewIds);
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

  public void loadAppIds() {
    appIdWithBotFlags.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String appIds = UBIConfig.getString(Property.APP_ID);
    String appIdAndFlags = isTestEnabled ? appIds : HdfsLoader.getInstance()
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), appIds);
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

  public void loadPageFmlys() {
    pageFmlyMap.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String pageFmlys = UBIConfig.getString(Property.PAGE_FMLY);
    String pageFmlysValue = isTestEnabled ? pageFmlys : HdfsLoader.getInstance()
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), pageFmlys);
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

  public void loadMpxRotetion() {
    mpxMap.clear();
    boolean isTestEnabled = UBIConfig.getBooleanOrDefault(Property.IS_TEST_ENABLE, false);
    String mpxRotation = UBIConfig.getString(Property.MPX_ROTATION);
    String mpxRotations = isTestEnabled ? mpxRotation : HdfsLoader.getInstance()
        .getLkpFileContent(UBIConfig.getUBIProperty(Property.LKP_PATH), mpxRotation);

    if (StringUtils.isNotBlank(mpxRotations)) {
      for (String mpx : mpxRotations.split(LKP_RECORD_DELIMITER)) {
        String[] values = mpx.split(LKP_FILED_DELIMITER);
        // Keep the null judgment also for session metrics first finding flag
        if (values[0] != null && values[1] != null) {
          try {
            //                            mpxMap.put(Long.parseLong(values[0].trim()),
            // String.valueOf(values[1].trim()));
            mpxMap.put(values[0].trim(), values[1].trim());
          } catch (NumberFormatException e) {
            log.error("Ignore the incorrect format for mpx: " + values[0] + " - " + values[1]);
          }
        }
      }
    } else {
      log.warn("Empty content for lookup table of mpx rotation.");
    }
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
