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
  private static final int UPDATE_COUNTS = 10;
  private static Calendar calendar;
  private Timer timer;
  private LkpManager lkpManager;
  private Map<String, Long> lkpfileDate = new ConcurrentHashMap<String, Long>();
  private volatile HdfsLoader hdfsLoader;
  public LkpFetcher(LkpManager lkpManager) {
    init();
    this.lkpManager = lkpManager;
    this.hdfsLoader=lkpManager.hdfsLoader;
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
    while (currentRoundCount < UPDATE_COUNTS) {
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.IFRAME_PAGE_IDS), lkpfileDate)) {
        lkpManager.loadIframePageIds();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.FINDING_FLAGS), lkpfileDate)) {
        lkpManager.loadFindingFlag();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.VTNEW_IDS), lkpfileDate)) {
        lkpManager.loadVtNewIds();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.IAB_AGENT), lkpfileDate)) {
        lkpManager.getIabAgentRegs();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.APP_ID), lkpfileDate)) {
        lkpManager.loadAppIds();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.LARGE_SESSION_GUID), lkpfileDate)) {
        lkpManager.loadLargeSessionGuid();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.PAGE_FMLY), lkpfileDate)) {
        lkpManager.loadPageFmlys();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.MPX_ROTATION), lkpfileDate)) {
        lkpManager.loadMpxRotetion();
        currentRoundCount++;
      }
      if (hdfsLoader
          .isUpdate(lkpPath, UBIConfig.getUBIProperty(Property.SELECTED_IPS), lkpfileDate)) {
        lkpManager.loadSelectedIps();
        currentRoundCount++;
      }

    }
    System.out.println("daily refresh completed");
  }
}
