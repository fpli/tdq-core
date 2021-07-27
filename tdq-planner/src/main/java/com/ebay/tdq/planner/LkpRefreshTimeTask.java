package com.ebay.tdq.planner;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @author juntzhang
 */
@Slf4j
public class LkpRefreshTimeTask extends TimerTask {

  private final Refreshable refreshable;

  public LkpRefreshTimeTask(Refreshable refreshable, TimeUnit timeUnit) {
    this.refreshable = refreshable;
    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor
        = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
      @Override
      public Thread newThread(@NotNull Runnable r) {
        Thread t = new Thread(r, "tdq-lkp-refresh-thread");
        t.setDaemon(true);
        return t;
      }
    });
    scheduledThreadPoolExecutor.scheduleAtFixedRate(this, 0, 1, timeUnit);
  }

  @Override
  public void run() {
    try {
      refreshable.refresh();
    } catch (Exception e) {
      log.warn(System.currentTimeMillis() + "refresh lkp file failed");
    }
  }
}