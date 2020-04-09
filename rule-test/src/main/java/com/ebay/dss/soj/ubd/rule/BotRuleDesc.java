package com.ebay.dss.soj.ubd.rule;

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

  static BotRuleDesc toRuleDesc(String sqlContent) {
    //parse sqlContent to BoltRuleDesc using calcite
    BotRuleDesc botRuleDesc = new BotRuleDesc();

    return botRuleDesc;
  }

  static String generateReportScript(BotRuleDesc ruleDesc) {
    StringBuilder sqlScript = new StringBuilder();
    sqlScript
        .append("select count(*) from ").append(ruleDesc.getTableName())
        .append(" where boltFlag=").append(ruleDesc.getBoltFlag())
        .append(" and ");
    return sqlScript.toString();
  }
}