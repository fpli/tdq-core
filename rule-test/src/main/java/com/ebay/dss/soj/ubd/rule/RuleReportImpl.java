package com.ebay.dss.soj.ubd.rule;

import com.ebay.dss.soj.jdbc.HiveJDBClient;
import java.sql.ResultSet;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.log4j.Log4j;

@Log4j
public class RuleReportImpl implements RuleReport {

  private ExecutorService executorService = Executors.newFixedThreadPool(5);
  private Map<BotRuleDesc, RuleReportResult> resultMap = new ConcurrentHashMap();

  @Override
  public void trigger(String sqlContent) {
    long triggerTime = System.nanoTime();
    executorService.submit(new RuleReportRunnable(sqlContent, triggerTime));
  }

  @Override
  public RuleReportResult getResult(String sqlContent) {
    BotRuleDesc botRuleDesc = BotRuleDesc.toRuleDesc(sqlContent);
    if (log.isDebugEnabled()) {
      resultMap.forEach((key, value) -> log.debug(key + ":" + value));
    }
    return resultMap.get(botRuleDesc);
  }

  class RuleReportRunnable implements Runnable {

    private String sqlContent;
    private long triggerTime;
    private HiveJDBClient hiveJDBClient = new HiveJDBClient();

    RuleReportRunnable(String sqlContent, long triggerTime) {
      this.sqlContent = sqlContent;
      this.triggerTime = triggerTime;
    }

    @Override
    public void run() {
      BotRuleDesc botRuleDesc = BotRuleDesc.toRuleDesc(sqlContent);
      String sql = BotRuleDesc.generateReportScript(botRuleDesc);
      ResultSet resultSet = hiveJDBClient.exeSQL(sql);
      RuleReportResult ruleReportResult = RuleReportResult.toRuleReportResult(resultSet);
      resultMap.put(botRuleDesc, ruleReportResult);
    }
  }

}
