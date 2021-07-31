package com.ebay.tdq.planner;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author juntzhang
 */
@Slf4j
public class LkpRefreshTimeTask extends TimerTask {

  private final String id;
  private final Refreshable refreshable;

  public LkpRefreshTimeTask(Refreshable refreshable, TimeUnit timeUnit) {
    this.refreshable = refreshable;
    this.id = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor
        = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
      @Override
      public Thread newThread(@NotNull Runnable r) {
        Thread t = new Thread(r, "tdq-thread-" + id);
        t.setDaemon(true);
        return t;
      }
    });
    scheduledThreadPoolExecutor.scheduleAtFixedRate(this, 0, 1, timeUnit);
  }

  public String getId() {
    return id;
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