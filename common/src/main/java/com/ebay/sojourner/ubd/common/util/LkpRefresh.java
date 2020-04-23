package com.ebay.sojourner.ubd.common.util;

import static com.ebay.sojourner.ubd.common.util.UBIConfig.getUBIProperty;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LkpRefresh extends TimerTask {

  private static final int UPDATE_COUNTS = 8;
  private static Calendar calendar = Calendar.getInstance();
  private static Timer timer = new Timer();
  private volatile LkpManager lkpManager;
  private Map<String, Long> lkpfileDate = new ConcurrentHashMap<>();

  public LkpRefresh(LkpManager lkpManager) {
    this.lkpManager = lkpManager;
  }

  public void startDailyRefresh() {
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE), 5, 0, 0);
    Date date = calendar.getTime();
    timer.scheduleAtFixedRate(this, date, TimeUnit.DAYS.toMillis(1));
  }

  @Override
  public void run() {
    String lkpPath = getUBIProperty(Property.LKP_PATH);
    int currentRoundCount = 0;
    while (currentRoundCount < UPDATE_COUNTS) {
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.IFRAME_PAGE_IDS), lkpfileDate)) {
        lkpManager.loadIframePageIds();
      }
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.FINDING_FLAGS), lkpfileDate)) {
        lkpManager.loadFindingFlag();
      }
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.VTNEW_IDS), lkpfileDate)) {
        lkpManager.loadVtNewIds();
      }
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.IAB_AGENT), lkpfileDate)) {
        lkpManager.loadIabAgent();
      }
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.APP_ID), lkpfileDate)) {
        lkpManager.loadAppIds();
      }
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.PAGE_FMLY), lkpfileDate)) {
        lkpManager.loadPageFmlys();
      }
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.MPX_ROTATION), lkpfileDate)) {
        lkpManager.loadMpxRotetion();
      }
      if (lkpManager.isUpdate(lkpPath, getUBIProperty(Property.SELECTED_IPS), lkpfileDate)) {
        lkpManager.loadSelectedIps();
      }
      currentRoundCount++;
    }
    lkpManager.closeFS();
  }
}
