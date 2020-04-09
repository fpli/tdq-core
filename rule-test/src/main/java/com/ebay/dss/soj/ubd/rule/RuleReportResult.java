package com.ebay.dss.soj.ubd.rule;

import java.sql.ResultSet;
import lombok.Data;

@Data
public class RuleReportResult {

  private BotRuleDesc botRuleDesc;
  private String result;

  static RuleReportResult toRuleReportResult(ResultSet resultSet) {
    return new RuleReportResult();
  }
}
