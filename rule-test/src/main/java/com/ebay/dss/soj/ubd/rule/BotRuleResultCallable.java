package com.ebay.dss.soj.ubd.rule;

import com.ebay.dss.soj.jdbc.HiveJDBClient;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

public class BotRuleResultCallable implements Callable<BotRuleResult> {

  private String script;

  public BotRuleResultCallable(String script) {
    this.script = script;
  }

  @Override
  public BotRuleResult call() throws Exception {
    HiveJDBClient client = new HiveJDBClient();
    ResultSet resultSet = client.exeSQL(script);
    return BotRuleResult.of(resultSet);
  }
}
