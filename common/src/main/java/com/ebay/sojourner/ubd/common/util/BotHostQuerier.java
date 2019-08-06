package com.ebay.sojourner.ubd.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author weifang.
 */
public class BotHostQuerier {
    private final ExecutorService executor;

    public BotHostQuerier(int threadPoolSize) {
        executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void startQuery(final String ip, final Callback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean ret = BotHostMatcher.INSTANCE.isBotIp(ip);
                callback.postProcess(ip, ret);
            }
        };

        executor.submit(runnable);
    }

    public interface Callback {
        void postProcess(String ip, boolean isBotIp);
    }
}
