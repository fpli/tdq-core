package com.ebay.dss.soj.ubd.rule;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j;

@Log4j
public class RuleReportImpl implements RuleReport {

  private ExecutorService executorService = Executors.newFixedThreadPool(5);
  private Map<BotRuleDesc, RuleReportResult> resultMap = new ConcurrentHashMap();

  @Override
  public void trigger(String sqlContent) {
    long triggerTime = System.nanoTime();
    BotRuleDesc botRuleDesc = BotRuleDesc.toRuleDesc(sqlContent);
    Future<RuleReportResult> future = executorService.submit(
        new RuleReportCallable(botRuleDesc, triggerTime));
    if (future.isDone()) {
      try {
        resultMap.put(botRuleDesc, future.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public RuleReportResult getResult(String sqlContent) {
    BotRuleDesc botRuleDesc = BotRuleDesc.toRuleDesc(sqlContent);
    if (log.isDebugEnabled()) {
      resultMap.forEach((key, value) -> log.debug(key + ":" + value));
    }
    return resultMap.get(botRuleDesc);
  }
}