package com.ebay.sojourner.common.util;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LkpRefreshTimeTask extends TimerTask {

    private volatile LkpManager lkpManager;

    public LkpRefreshTimeTask(LkpManager lkpManager, TimeUnit timeUnit) {
        this.lkpManager = lkpManager;
        //Set start at 00:05:00 every day
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //        calendar.set(Calendar.MINUTE, 5);
        //        calendar.set(Calendar.SECOND, 0);
        //        Timer timer = new Timer(true);
        //        timer.scheduleAtFixedRate(this, calendar.getTime(), timeUnit.toMillis(1));
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(this,0,1,timeUnit);
    }

    @Override
    public void run() {
        lkpManager.refreshLkpFiles();
        //    lkpManager.closeFS();
    }
}



