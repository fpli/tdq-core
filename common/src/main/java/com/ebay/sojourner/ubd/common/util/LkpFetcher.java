package com.ebay.sojourner.ubd.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LkpFetcher extends TimerTask {

  private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
  private static final int UPDATE_COUNTS = 8;
  private static final int ROUND_THRESHOLD=20;
  private static Calendar calendar;
  private Timer timer;
  private volatile LkpManager lkpManager;
  private Map<String, Long> lkpfileDate = new ConcurrentHashMap<String, Long>();
  private volatile HdfsLoader hdfsLoader;

  public LkpFetcher(LkpManager lkpManager) {
    init();
    this.lkpManager = lkpManager;
    this.hdfsLoader = lkpManager.hdfsLoader;
  }

  private void init() {
    if (timer == null) {
      timer = new Timer();
    }
    if (calendar == null) {
      calendar = Calendar.getInstance();
    }
  }

  public void startDailyRefresh() {
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE), 5, 0, 0);
    Date date = calendar.getTime();
        timer.scheduleAtFixedRate(this, date, ONE_DAY_MILLIS);
  }


  @Override
  public void run() {
    String lkpPath = UBIConfig.getUBIProperty(Property.LKP_PATH);
    int currentRoundCount = 0;
    int totalRoundCount=0;
    while (currentRoundCount < UPDATE_COUNTS||totalRoundCount<ROUND_THRESHOLD) {
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.IFRAME_PAGE_IDS), lkpfileDate)) {
        lkpManager.loadIframePageIds(false);
        currentRoundCount++;
        System.out.println("IFRAME_PAGE_IDS:" + currentRoundCount);
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.FINDING_FLAGS), lkpfileDate)) {
        lkpManager.loadFindingFlag(false);
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.VTNEW_IDS), lkpfileDate)) {
        lkpManager.loadVtNewIds(false);
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.IAB_AGENT), lkpfileDate)) {
        lkpManager.loadIabAgent(false);
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.APP_ID), lkpfileDate)) {
        lkpManager.loadAppIds(false);
        currentRoundCount++;
      }
      //      if (hdfsLoader
      //          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.LARGE_SESSION_GUID),
      //          lkpfileDate)) {
      //        lkpManager.loadLargeSessionGuid(false);
      //        currentRoundCount++;
      //      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.PAGE_FMLY), lkpfileDate)) {
        lkpManager.loadPageFmlys(false);
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.MPX_ROTATION), lkpfileDate)) {
        lkpManager.loadMpxRotetion(false);
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.SELECTED_IPS), lkpfileDate)) {
        lkpManager.loadSelectedIps(false);
        currentRoundCount++;
      }
      try {
        Thread.sleep(100000);
      } catch (InterruptedException e) {
        e.printStackTrace();
        log.error("Lkpfetcher interruptted");
      }
    }
    hdfsLoader.closeFS();
    System.out.println("daily refresh completed");
  }
}
