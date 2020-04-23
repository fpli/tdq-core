package com.ebay.sojourner.ubd.common.util;

import static com.ebay.sojourner.ubd.common.util.UBIConfig.getString;
import static com.ebay.sojourner.ubd.common.util.UBIConfig.getUBIProperty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

@Slf4j
public class LkpManager {
  public static final String LKP_FILED_DELIMITER = ",";
  public static final String LKP_RECORD_DELIMITER = "\177";
  public static final String TEXT_RECORD_DELIMITER = "\n";
  public static final String TEXT_FIELD_DELIMITER = "\t";
  public static final int PAIR_LENGTH = 2;

  private Set<Integer> pageIdSet = new CopyOnWriteArraySet<>();
  private Map<Integer, Integer> findingFlagMap = new ConcurrentHashMap<>();
  private Map<Integer, Integer[]> vtNewIdsMap = new ConcurrentHashMap<>();
  private Set<String> appIdWithBotFlags = new CopyOnWriteArraySet<>();
  private List<String> iabAgentRegs = new CopyOnWriteArrayList<>();
  private Set<String> largeSessionGuidSet = new CopyOnWriteArraySet<>();
  private Map<Integer, String[]> pageFmlyMap = new ConcurrentHashMap<>();
  private Map<String, String> mpxMap = new ConcurrentHashMap<>();
  private Map<String, Boolean> selectedIps = new ConcurrentHashMap<>();
  private Set<String> selectedAgents = new CopyOnWriteArraySet<>();
  private volatile FileSystem fileSystem = null;
  private boolean loadLkpFromHDFS = true;

  private static volatile LkpManager lkpManager;
  private volatile LkpRefresh lkpRefresh;

  private LkpManager() {
    lkpRefresh = new LkpRefresh(this);
    lkpRefresh.startDailyRefresh();
    loadResources();
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
    loadVtNewIds();
    loadAppIds();
    loadPageFmlys();
    loadMpxRotetion();
  }

  public void loadIframePageIds() {
    Set<Integer> pageIdSetMid = new CopyOnWriteArraySet<>();
    String iframePageIds = getString(Property.IFRAME_PAGE_IDS);
    String pageIds = getLkpFileContent(getUBIProperty(Property.LKP_PATH), iframePageIds);
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

  public void loadSelectedIps() {
    Map<String, Boolean> selectedIpsMid = new ConcurrentHashMap<>();
    parseTextFile(Property.SELECTED_IPS, selectedIpsMid);
    selectedIps = selectedIpsMid;
  }

  public void loadSelectedAgents() {
    Set<String> selectedAgentsMid = new CopyOnWriteArraySet<>();
    parseTextFile(Property.SELECTED_AGENTS, selectedAgentsMid);
    selectedAgents = selectedAgentsMid;
  }

  private void parseTextFile(String filePathProperty, Set<String> sets) {
    String file = getString(filePathProperty);
    String fileContent = getLkpFileContent(getUBIProperty(Property.LKP_PATH), file);
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
    String file = getString(filePathProperty);
    String fileContent = getLkpFileContent(getUBIProperty(Property.LKP_PATH), file);
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
    Set<String> largeSessionGuidSetMid = new CopyOnWriteArraySet<>();
    String largeSessionGuidValue = getString(Property.LARGE_SESSION_GUID);
    String largeSessionGuids = getLkpFileContent(getUBIProperty(Property.LKP_PATH),
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

  public void loadIabAgent() {
    List<String> iabAgentRegsMid = new CopyOnWriteArrayList<>();
    String iabAgentReg = getString(Property.IAB_AGENT);
    String iabAgentRegValue = getLkpFileContent(getUBIProperty(Property.LKP_PATH), iabAgentReg);
    if (StringUtils.isNotBlank(iabAgentRegValue)) {
      for (String iabAgent : iabAgentRegValue.split(LKP_RECORD_DELIMITER)) {
        iabAgentRegsMid.add(iabAgent.toLowerCase());
      }
    } else {
      log.warn("Empty content for lookup table of iab agent info");
    }
    iabAgentRegs = iabAgentRegsMid;

  }

  public void loadFindingFlag() {
    Map<Integer, Integer> findingFlagMapMid = new ConcurrentHashMap<>();
    String findingFlag = getString(Property.FINDING_FLAGS);
    String findingFlags = getLkpFileContent(getUBIProperty(Property.LKP_PATH), findingFlag);
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

  public void loadVtNewIds() {
    Map<Integer, Integer[]> vtNewIdsMapMid = new ConcurrentHashMap<>();
    String vtNewIds = getString(Property.VTNEW_IDS);
    String vtNewIdsValue = getLkpFileContent(getUBIProperty(Property.LKP_PATH), vtNewIds);
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

  public void loadAppIds() {
    Set<String> appIdWithBotFlagsMid = new CopyOnWriteArraySet<>();
    String appIds = getString(Property.APP_ID);
    String appIdAndFlags = getLkpFileContent(getUBIProperty(Property.LKP_PATH), appIds);
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

  public void loadPageFmlys() {
    Map<Integer, String[]> pageFmlyMapMid = new ConcurrentHashMap<>();
    String pageFmlys = getString(Property.PAGE_FMLY);
    String pageFmlysValue = getLkpFileContent(getUBIProperty(Property.LKP_PATH), pageFmlys);
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

  public void loadMpxRotetion() {
    Map<String, String> mpxMapMid = new ConcurrentHashMap<>();
    String mpxRotation = getString(Property.MPX_ROTATION);
    String mpxRotations = getLkpFileContent(getUBIProperty(Property.LKP_PATH), mpxRotation);
    if (StringUtils.isNotBlank(mpxRotations)) {
      for (String mpx : mpxRotations.split(LKP_RECORD_DELIMITER)) {
        String[] values = mpx.split(LKP_FILED_DELIMITER);
        // Keep the null judgment also for session metrics first finding flag
        if (values[0] != null && values[1] != null) {
          try {
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

  public String getLkpFileContent(String parentPath, String filename) {
    String content = null;
    Path filePath = new Path(parentPath + filename);
    try (InputStream in = getInputStream(filePath, filename)) {
      StringBuffer resultBuilder = new StringBuffer();
      byte[] bytes = new byte[4096];
      int readBytes = 0;
      while ((readBytes = in.read(bytes)) != -1) {
        resultBuilder.append(new String(Arrays.copyOfRange(bytes, 0, readBytes),
            StandardCharsets.UTF_8));
        bytes = new byte[4096];
      }
      content = resultBuilder.toString().trim();
    } catch (IOException e) {
      log.error("Open HDFS file {} issue:{}", filePath.getName(), ExceptionUtils.getStackTrace(e));
    }
    return content;
  }

  private InputStream getInputStream(Path path, String resource) {
    InputStream instream = null;
    try {
      initFs();
      instream = fileSystem.open(path);
    } catch (Exception e) {
      log.warn("Load file failed from [{}], will try to load from classpath: {}", path, resource);
      loadLkpFromHDFS = false;
      try {
        instream = getStreamFromClasspath(resource);
      } catch (FileNotFoundException ex) {
        log.error("Cannot find file {} from HDFS and classpath.", resource);
      }
    }
    return instream;
  }

  private InputStream getStreamFromClasspath(String resource) throws FileNotFoundException {
    InputStream instream;
    if (StringUtils.isNotBlank(resource)) {
      instream = HdfsLoader.class.getResourceAsStream(resource);
      if (instream == null) {
        throw new FileNotFoundException("Can't locate resource based on classPath: " + resource);
      }
    } else {
      throw new RuntimeException("Try to load empty resource.");
    }
    return instream;
  }

  public boolean isUpdate(String path, String fileName, Map<String, Long> lkpFileDate) {
    if (!loadLkpFromHDFS) {
      return false;
    }
    Path path1 = new Path(path, fileName);
    try {
      if (fileSystem.exists(path1)) {
        FileStatus[] fileStatus = fileSystem.listStatus(path1, new FileNameFilter(fileName));
        long lastModifiedTime = fileStatus[0].getModificationTime();
        long preLastModifiedTime =
            lkpFileDate.get(fileName) == null ? 0 : lkpFileDate.get(fileName);
        if (lastModifiedTime > preLastModifiedTime) {
          lkpFileDate.put(fileName, lastModifiedTime);
        }
        return lastModifiedTime > preLastModifiedTime;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void closeFS() {
    if (fileSystem != null) {
      try {
        fileSystem.close();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        fileSystem = null;
      }
    }
  }

  private void initFs() throws IOException, IllegalArgumentException {
    if (fileSystem == null) {
      Configuration configuration = new Configuration();
      fileSystem = FileSystem.newInstance(configuration);
    }
  }

  public Set<Integer> getIframePageIdSet() {
    return pageIdSet;
  }

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

  public Map<Integer, String[]> getPageFmlyMaps() {
    return pageFmlyMap;
  }

  public Map<String, Boolean> getSelectedIps() {
    return selectedIps;
  }

  public Set<String> getSelectedAgents() {
    return selectedAgents;
  }

  public Set<String> getLargeSessionGuid() {
    return largeSessionGuidSet;
  }

  public Map<String, String> getMpxMap() {
    return mpxMap;
  }

  private class FileNameFilter implements PathFilter {

    private String fileName;

    private FileNameFilter(String fileName) {
      this.fileName = fileName;
    }
    @Override
    public boolean accept(Path path) {
      if (fileName.contains(path.getName())) {
        return true;
      }
      return false;
    }
  }
}
