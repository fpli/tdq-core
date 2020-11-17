package com.ebay.sojourner.common.util;

import static com.ebay.sojourner.common.util.UBIConfig.getString;
import static com.ebay.sojourner.common.util.UBIConfig.getUBIProperty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
  private static final String LKP_RECORD_DELIMITER = "\177";
  private static final String TEXT_RECORD_DELIMITER = "\n";
  private static final String TEXT_FIELD_DELIMITER = "\t";
  private static final String LKP_PATH = getUBIProperty(Property.LKP_PATH);
  private static volatile LkpManager lkpManager;
  private Set<Integer> pageIdSet = new CopyOnWriteArraySet<>();
  private Map<Integer, Integer> findingFlagMap = new ConcurrentHashMap<>();
  private Map<Integer, Integer[]> vtNewIdsMap = new ConcurrentHashMap<>();
  private Set<String> appIdWithBotFlags = new CopyOnWriteArraySet<>();
  private List<String> iabAgentRegs = new CopyOnWriteArrayList<>();
  private Set<String> largeSessionGuidSet = new CopyOnWriteArraySet<>();
  private Map<Integer, String[]> pageFmlyMap = new ConcurrentHashMap<>();
  private Map<Long, String> mpxMap = new ConcurrentHashMap<>();
  private Map<String, Boolean> selectedIps = new ConcurrentHashMap<>();
  private Set<String> selectedAgents = new CopyOnWriteArraySet<>();
  private Map<String, Long> lkpFileLastUpdDt = new ConcurrentHashMap<>();
  private volatile FileSystem fileSystem = null;
  private volatile boolean loadLkpFromHDFS = false;
  private volatile boolean firstRun = true;
  private volatile LkpRefreshTimeTask lkpRefreshTimeTask;

  private LkpManager() {
    lkpRefreshTimeTask = new LkpRefreshTimeTask(this);
    refreshLkpFiles();
    firstRun = false;
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

  public void refreshLkpFiles() {
    refreshIframePageIds();
    refreshSelectedIps();
    refreshSelectedAgents();
    refreshLargeSessionGuid();
    refreshIabAgent();
    refreshFindingFlag();
    refreshVtNewIds();
    refreshAppIds();
    refreshPageFmlys();
    refreshMpxRotetion();
  }

  private void refreshIframePageIds() {
    String property = Property.IFRAME_PAGE_IDS;
    if (isUpdate(property)) {
      Set<Integer> pageIdSetMid = new CopyOnWriteArraySet<>();

      String pageIds = getLkpFileContent(property);
      for (String pageId : pageIds.split(LKP_RECORD_DELIMITER)) {
        try {
          pageIdSetMid.add(Integer.valueOf(pageId));
        } catch (NumberFormatException e) {
          log.warn("Parsing PageId failed, format incorrect...");
        }
      }
      pageIdSet = pageIdSetMid;
    }
  }

  private void refreshSelectedIps() {
    String property = Property.SELECTED_IPS;
    if (isUpdate(property)) {
      Map<String, Boolean> selectedIpsMid = new ConcurrentHashMap<>();

      String fileContent = getLkpFileContent(property);
      for (String record : fileContent.split(TEXT_RECORD_DELIMITER)) {
        if (StringUtils.isNotBlank(record)) {
          String[] recordPair = record.split(TEXT_FIELD_DELIMITER);
          if (recordPair.length == 2) {
            String recordKey = recordPair[0];
            String recordValue = recordPair[1];
            if (StringUtils.isNotBlank(recordKey) && StringUtils.isNotBlank(recordValue)) {
              selectedIpsMid.put(recordKey.trim(), Boolean.valueOf(recordValue.trim()));
            }
          }
        }
      }
      selectedIps = selectedIpsMid;
    }
  }

  private void refreshSelectedAgents() {
    String property = Property.SELECTED_AGENTS;
    if (isUpdate(property)) {
      Set<String> selectedAgentsMid = new CopyOnWriteArraySet<>();
      String fileContent = getLkpFileContent(property);
      for (String record : fileContent.split(TEXT_RECORD_DELIMITER)) {
        if (StringUtils.isNotBlank(record)) {
          String[] recordPair = record.split(TEXT_FIELD_DELIMITER);
          String recordKey = recordPair[0];
          if (StringUtils.isNotBlank(recordKey)) {
            selectedAgentsMid.add(recordKey.trim());
          }
        }
      }
      selectedAgents = selectedAgentsMid;
    }
  }

  private void refreshLargeSessionGuid() {
    String property = Property.LARGE_SESSION_GUID;
    if (isUpdate(property)) {
      Set<String> largeSessionGuidSetMid = new CopyOnWriteArraySet<>();
      String largeSessionGuids = getLkpFileContent(property);
      for (String guid : largeSessionGuids.split(LKP_FILED_DELIMITER)) {
        if (StringUtils.isNotBlank(guid)) {
          largeSessionGuidSetMid.add(guid.trim());
        }
      }
      largeSessionGuidSet = largeSessionGuidSetMid;
    }
  }

  private void refreshIabAgent() {
    String property = Property.IAB_AGENT;
    if (isUpdate(property)) {
      List<String> iabAgentRegsMid = new CopyOnWriteArrayList<>();
      String iabAgentRegValue = getLkpFileContent(property);
      for (String iabAgent : iabAgentRegValue.split(LKP_RECORD_DELIMITER)) {
        iabAgentRegsMid.add(iabAgent.toLowerCase());
      }
      iabAgentRegs = iabAgentRegsMid;
    }
  }

  private void refreshFindingFlag() {
    String property = Property.FINDING_FLAGS;
    if (isUpdate(property)) {
      Map<Integer, Integer> findingFlagMapMid = new ConcurrentHashMap<>();
      String findingFlags = getLkpFileContent(property);
      for (String pageFlag : findingFlags.split(LKP_RECORD_DELIMITER)) {
        String[] values = pageFlag.split(LKP_FILED_DELIMITER);
        // Keep the null judgment also for session metrics first finding flag
        if (values[0] != null && values[1] != null) {
          try {
            findingFlagMapMid.put(Integer.valueOf(values[0].trim()),
                Integer.valueOf(values[1].trim()));
          } catch (NumberFormatException e) {
            log.error(
                "Ignore the incorrect format for findflags: " + values[0] + " - " + values[1]);
          }
        }
      }
      findingFlagMap = findingFlagMapMid;
    }
  }

  private void refreshVtNewIds() {
    String property = Property.VTNEW_IDS;
    if (isUpdate(property)) {
      Map<Integer, Integer[]> vtNewIdsMapMid = new ConcurrentHashMap<>();
      String vtNewIdsValue = getLkpFileContent(property);
      for (String vtNewId : vtNewIdsValue.split(LKP_RECORD_DELIMITER)) {
        Integer[] pageInfo = new Integer[2];
        String[] ids = vtNewId.split(LKP_FILED_DELIMITER, pageInfo.length + 1);
        Integer newPageId = StringUtils.isEmpty(ids[0]) ? null : Integer.valueOf(ids[0].trim());
        pageInfo[0] = StringUtils.isEmpty(ids[1]) ? null : Integer.valueOf(ids[1].trim());
        pageInfo[1] = StringUtils.isEmpty(ids[2]) ? null : Integer.valueOf(ids[2].trim());
        vtNewIdsMapMid.put(newPageId, pageInfo);
      }
      vtNewIdsMap = vtNewIdsMapMid;
    }
  }

  private void refreshAppIds() {
    String property = Property.APP_ID;
    if (isUpdate(property)) {
      Set<String> appIdWithBotFlagsMid = new CopyOnWriteArraySet<>();
      String appIdAndFlags = getLkpFileContent(property);
      String[] appIdFlagPair = appIdAndFlags.split(LKP_RECORD_DELIMITER);
      for (String appIdFlag : appIdFlagPair) {
        if (StringUtils.isNotBlank(appIdFlag)) {
          appIdWithBotFlagsMid.add(appIdFlag.trim());
        }
      }
      appIdWithBotFlags = appIdWithBotFlagsMid;
    }
  }

  private void refreshPageFmlys() {
    String property = Property.PAGE_FMLY;
    if (isUpdate(property)) {
      Map<Integer, String[]> pageFmlyMapMid = new ConcurrentHashMap<>();
      String pageFmlysValue = getLkpFileContent(property);
      for (String pageFmlyPair : pageFmlysValue.split(LKP_RECORD_DELIMITER)) {
        String[] pageFmlyNames = new String[2];
        if (StringUtils.isNotBlank(pageFmlyPair)) {
          String[] values = pageFmlyPair.split(LKP_FILED_DELIMITER, pageFmlyNames.length + 1);
          Integer pageId = StringUtils.isEmpty(values[0]) ? null : Integer.valueOf(values[0]);
          if (values == null || values.length != 3) {
            log.error("refreshPageFmlys error ========:" + pageFmlyPair);
          }
          pageFmlyNames[0] = StringUtils.isEmpty(values[1]) ? null : values[1];
          pageFmlyNames[1] = StringUtils.isEmpty(values[2]) ? null : values[2];
          pageFmlyMapMid.put(pageId, pageFmlyNames);
        }
      }
      pageFmlyMap = pageFmlyMapMid;
    }
  }

  private void refreshMpxRotetion() {
    String property = Property.MPX_ROTATION;
    if (isUpdate(property)) {
      Map<Long, String> mpxMapMid = new ConcurrentHashMap<>();
      String mpxRotations = getLkpFileContent(property);
      for (String mpx : mpxRotations.split(LKP_RECORD_DELIMITER)) {
        String[] values = mpx.split(LKP_FILED_DELIMITER);
        if (values == null || values.length != 2) {
          log.error("refreshMpxRotetion error ========:" + mpx);
        }
        if (values[0] != null && values[1] != null) {
          try {
            mpxMapMid.put(Long.parseLong(values[0].trim()), values[1].trim());
          } catch (NumberFormatException e) {
            log.error("Ignore the incorrect format for mpx: " + values[0] + " - " + values[1]);
          }
        }
      }
      mpxMap = mpxMapMid;
    }
  }

  private synchronized String getLkpFileContent(String lkpType) {
    String filename = getString(lkpType);
    Path filePath = new Path(LKP_PATH + filename);
    StringBuffer resultBuilder = new StringBuffer();
    try (InputStream in = getInputStream(filePath, filename)) {
      byte[] bytes = new byte[4096];
      int readBytes = 0;
      while ((readBytes = in.read(bytes)) != -1) {
        resultBuilder.append(new String(Arrays.copyOfRange(bytes, 0, readBytes),
            StandardCharsets.UTF_8));
        bytes = new byte[4096];
      }
    } catch (IOException e) {
      log.error("Open HDFS file {} issue:{}", filePath.getName(), ExceptionUtils.getStackTrace(e));
    } finally {
      closeFS();
    }
    return resultBuilder.toString().trim();
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
      instream = LkpManager.class.getResourceAsStream(resource);
      if (instream == null) {
        throw new FileNotFoundException("Can't locate resource based on classPath: " + resource);
      }
    } else {
      throw new RuntimeException("Try to load empty resource.");
    }
    return instream;
  }

  public boolean isUpdate(String lkpType) {
    if (firstRun) {
      return true;
    }
    if (!loadLkpFromHDFS) {
      return false;
    }
    String fileName = getString(lkpType);
    Path path = new Path(LKP_PATH, fileName);
    try {
      initFs();
      if (fileSystem.exists(path)) {
        FileStatus[] fileStatus = fileSystem.listStatus(path, new FileNameFilter(fileName));
        long lastModifiedTime = fileStatus[0].getModificationTime();
        long preLastModifiedTime =
            lkpFileLastUpdDt.get(fileName) == null ? 0 : lkpFileLastUpdDt.get(fileName);
        if (lastModifiedTime > preLastModifiedTime) {
          lkpFileLastUpdDt.put(fileName, lastModifiedTime);
        }

        log.error("lkp refresh time after :" + Calendar.getInstance().getTime());
        log.error("lkp refresh filename after:" + fileName);
        for (Entry entry : lkpFileLastUpdDt.entrySet()) {
          log.error("lkp key after:======" + entry.getKey());
          log.error("lkp value after:======" + entry.getValue());
        }
        return lastModifiedTime > preLastModifiedTime;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      closeFS();
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
      configuration.setBoolean("fs.hdfs.impl.disable.cache", true);
      fileSystem = FileSystem.newInstance(configuration);
      loadLkpFromHDFS = true;
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

  public Map<Long, String> getMpxMap() {
    return mpxMap;
  }

  public void clearAppId() {
    appIdWithBotFlags.clear();
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
