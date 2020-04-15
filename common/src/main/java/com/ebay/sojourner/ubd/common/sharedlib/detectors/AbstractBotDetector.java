package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sql.RuleManager;
import com.ebay.sojourner.ubd.common.sql.SqlEventRule;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBotDetector<T> implements BotDetector<T> {

  @Override
  public void initDynamicRules(RuleManager ruleManager, Set<Rule> botRules,
      List<Long> dynamicRuleIdList, String category) {
    List<SqlEventRule> dynamicEventRules = ruleManager.sqlEventRules();
    log.info("before hot deploy bot rules:" + botRules.size());
    if (botRules.isEmpty() && !dynamicEventRules.isEmpty()) {
      // add
      botRules.addAll(dynamicEventRules);
      for (SqlEventRule sqlEventRule : dynamicEventRules) {
        if (sqlEventRule.getCategory().equals(category)) {
          dynamicRuleIdList.add(sqlEventRule.getRuleId());
          sqlEventRule.init();
        }
      }
    } else if (!botRules.isEmpty() && !dynamicEventRules.isEmpty()) {
      // update
      for (Rule rule : botRules) {
        if (rule instanceof SqlEventRule) {
          SqlEventRule dynamicRule = (SqlEventRule) rule;
          if (dynamicRule.getVersion() != 0 && dynamicRule.getRuleId() != 0) {
            for (SqlEventRule sqlEventRule : dynamicEventRules) {
              if (sqlEventRule.getRuleId() == dynamicRule.getRuleId()
                  && sqlEventRule.getVersion() > dynamicRule.getVersion()
                  && sqlEventRule.getCategory().equals(category)) {
                botRules.remove(dynamicRule);
                botRules.add(sqlEventRule);
                sqlEventRule.init();
              }
            }
          }
        }
      }

      // add
      for (SqlEventRule sqlEventRule : dynamicEventRules) {
        if (!dynamicRuleIdList.contains(sqlEventRule.getRuleId())) {
          if (sqlEventRule.getCategory().equals(category)) {
            dynamicRuleIdList.add(sqlEventRule.getRuleId());
            botRules.add(sqlEventRule);
            sqlEventRule.init();
          }
        }
      }
    }
    log.info("after hot deploy bot rules:" + botRules.size());
  }
}
