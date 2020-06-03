package com.ebay.sojourner.common.util;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LkpRefreshTimeTask extends TimerTask {

  private volatile LkpManager lkpManager;

  public LkpRefreshTimeTask(LkpManager lkpManager) {
    this.lkpManager = lkpManager;
    //Set start at 5:00 every day
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 5);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Timer timer = new Timer(true);
    timer.scheduleAtFixedRate(this, calendar.getTime(), TimeUnit.DAYS.toMillis(1));
  }

  @Override
  public void run() {
    lkpManager.refreshLkpFiles();
    lkpManager.closeFS();
  }
}
