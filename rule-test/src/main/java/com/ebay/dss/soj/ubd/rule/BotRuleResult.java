package com.ebay.dss.soj.ubd.rule;

import java.sql.ResultSet;
import lombok.Data;

@Data
public class BotRuleResult {

  private BotRuleDesc botRuleDesc;
  private String result;

  static BotRuleResult of(ResultSet resultSet) {
    return new BotRuleResult();
  }
}
