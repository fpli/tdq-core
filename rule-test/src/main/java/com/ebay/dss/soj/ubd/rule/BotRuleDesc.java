package com.ebay.dss.soj.ubd.rule;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
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
    BotRuleParser botRuleParser = new BotRuleParser(sqlContent);
    BotRuleDesc botRuleDesc = new BotRuleDesc();
    botRuleDesc.setBoltFlag(botRuleParser.getBotFlag());
    botRuleDesc.setTableName(botRuleParser.getSourceTable());
    botRuleDesc.setDuration(Duration.ofMinutes(60));
    return botRuleDesc;
  }


  static String genCheckScript(BotRuleDesc ruleDesc) {
    StringBuilder sqlBuilder = new StringBuilder();
    String currentTimestamp = currentTimestamp("yyyy/MM/dd HH:mm:ss.SSS");
    String tableName = ruleDesc.getTableName();
    int originBotFlag = ruleDesc.getBoltFlag();
    sqlBuilder.append(genCheckScript(tableName, originBotFlag, currentTimestamp)).append("\n")
        .append("union all").append("\n")
        .append(genCheckScript(tableName, originBotFlag + 1000, currentTimestamp));
    return sqlBuilder.toString();
  }

  private static String genCheckScript(String tableName, int botFlag, String currentTimestamp) {
    StringBuilder sqlBuilder = new StringBuilder("select count(*) from ").append(tableName)
        .append("\n")
        .append("where array_contains(");
    if (tableName.contains("EVENT")) {
      sqlBuilder.append("botFlags").append(", ").append(botFlag).append(")").append("\n")
          .append("and sojlib.ts_mils(eventTimestamp) > '");
    } else if (tableName.contains("SESSION")) {
      sqlBuilder.append("botFlagList").append(", ").append(botFlag).append(")").append("\n")
          .append("and sojlib.ts_mils(startTimestamp) > '");
    }
    sqlBuilder.append(currentTimestamp).append("'");
    return sqlBuilder.toString();
  }

  private static String currentTimestamp(String formatPattern) {
    return new SimpleDateFormat(formatPattern).format(new Date());
  }
}
