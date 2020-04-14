package com.ebay.dss.soj.ubd.rule;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BotRuleDesc {

  private int boltFlag;
  private String tableName;
  private Duration duration;

  static BotRuleDesc of(String sqlContent) {
    //parse sqlContent to BoltRuleDesc using calcite
    BotRuleDesc botRuleDesc = new BotRuleDesc();
    botRuleDesc.setBoltFlag(1);
    botRuleDesc.setTableName("ubi_event");
    botRuleDesc.setDuration(Duration.ofMinutes(10));
    return botRuleDesc;
  }

  static String genBeforeCheckScript(BotRuleDesc ruleDesc) {
    StringBuilder sqlScript = new StringBuilder();
    sqlScript.append("select * from default.ubi_session_dq_overall");
    return sqlScript.toString();
  }

  static String genAfterCheckScript(BotRuleDesc ruleDesc) {
    StringBuilder sqlScript = new StringBuilder();
    sqlScript
        .append("select * from default.ubi_bot_dq_overall");
    return sqlScript.toString();
  }
}
