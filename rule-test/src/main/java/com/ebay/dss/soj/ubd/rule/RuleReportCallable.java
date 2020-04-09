package com.ebay.dss.soj.ubd.rule;

import com.ebay.dss.soj.jdbc.HiveJDBClient;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

public class RuleReportCallable implements Callable<RuleReportResult> {

  private BotRuleDesc botRuleDesc;
  private long triggerTime;
  private HiveJDBClient hiveJDBClient = new HiveJDBClient();

  public RuleReportCallable(BotRuleDesc botRuleDesc, long triggerTime) {
    this.botRuleDesc = botRuleDesc;
    this.triggerTime = triggerTime;
  }

  @Override
  public RuleReportResult call() {
    String sql = BotRuleDesc.generateReportScript(botRuleDesc);
    ResultSet resultSet = hiveJDBClient.exeSQL(sql);
    RuleReportResult ruleReportResult = RuleReportResult.toRuleReportResult(resultSet);
    return ruleReportResult;
  }
}
