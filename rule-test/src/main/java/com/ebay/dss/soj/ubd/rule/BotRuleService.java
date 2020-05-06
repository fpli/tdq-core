package com.ebay.dss.soj.ubd.rule;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j;

@Log4j
public class BotRuleService {

  private static volatile ListeningScheduledExecutorService executorService;
  private static int DEFAULT_PARALLEL_NUM = 5;

  public static void trigger(String sql, Class<? extends BotRuleResultCallback> callbackClass) {
    if (executorService == null) {
      synchronized (BotRuleService.class) {
        if (executorService == null) {
          executorService = MoreExecutors.listeningDecorator(
              Executors.newScheduledThreadPool(DEFAULT_PARALLEL_NUM,
                  r -> new Thread(r, UUID.randomUUID().toString())));
        }
      }
    }
    submit(sql, callbackClass);
  }

  private static void submit(String sql,
      Class<? extends BotRuleResultCallback> botRuleResultCallbackClass) {
    BotRuleResultCallback botRuleResultCallback = null;
    try {
      botRuleResultCallback = botRuleResultCallbackClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      log.error("Error occurred when construct class. ", e);
    }
    BotRuleDesc botRuleDesc = BotRuleDesc.of(sql);

    String checkScript = BotRuleDesc.genCheckScript(botRuleDesc);
    //According to the duration trigger another check after hot deploy
    ListenableScheduledFuture<BotRuleResult> afterFuture = executorService
        .schedule(new BotRuleResultCallable(checkScript), botRuleDesc.getDuration().getSeconds(),
            TimeUnit.SECONDS);
    Futures.addCallback(afterFuture, new BotRuleCallbackProxy(botRuleResultCallback),
        executorService);
  }

  public static void shutdown(int waitTimeSeconds) {
    if (executorService != null) {
      synchronized (BotRuleService.class) {
        if (executorService != null) {
          MoreExecutors
              .shutdownAndAwaitTermination(executorService, waitTimeSeconds, TimeUnit.SECONDS);
        }
      }
    }
  }
}
