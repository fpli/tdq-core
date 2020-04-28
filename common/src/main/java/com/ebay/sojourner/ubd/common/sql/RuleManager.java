package com.ebay.sojourner.ubd.common.sql;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public class RuleManager {

  private static final RuleManager INSTANCE = new RuleManager();
  private RuleFetcher ruleFetcher;
  private RuleNotificationListener ruleNotificationListener;

  private List<SqlEventRule> sqlEventRuleList = new CopyOnWriteArrayList<>();

  private RuleManager() {
    ruleFetcher = new RuleFetcher(this);

    ruleFetcher.fetchRules();

    ruleNotificationListener = new RuleNotificationListener(ruleFetcher);
    new Thread(
        () -> {
          ruleNotificationListener.listen();
        }, "Sojourner RuleNotificationListener Thread")
        .start();

    ruleFetcher.fetchRulesPeriodically();

  }

  public static RuleManager getInstance() {
    return INSTANCE;
  }

  public static void main(String[] args) throws Exception {
    RuleManager.getInstance();
    Thread.sleep(10 * 60 * 1000);
  }

  public List<SqlEventRule> sqlEventRules() {
    return this.sqlEventRuleList;
  }

  public void updateRules(List<RuleDefinition> ruleDefinitions) {

    if (CollectionUtils.isNotEmpty(ruleDefinitions)) {
      List<SqlEventRule> sqlNewEventRuleList = ruleDefinitions.stream()
          .filter(rule -> rule.getStatus().equals("4"))
          .map(rule -> SqlEventRule
              .of(rule.getContent(), rule.getBizId(), rule.getVersion(), rule.getCategory()))
          .collect(Collectors.toList());

      sqlEventRuleList = sqlNewEventRuleList;
    }

    log.info("Rules deployed: " + this.sqlEventRuleList.size());
  }

  public void updateRulesById(RuleDefinition ruleDefinition, Long ruleId) {

    List<Long> ruleIdList = sqlEventRuleList.stream().map(rule -> rule.getRuleId())
        .collect(Collectors.toList());
    SqlEventRule sqlEventRule = SqlEventRule
        .of(ruleDefinition.getContent(), ruleDefinition.getBizId(), ruleDefinition.getVersion(),
            ruleDefinition.getCategory());
    if (!ruleIdList.contains(ruleId)) {
      sqlEventRuleList.add(sqlEventRule);
    } else if (!ruleDefinition.getStatus().equals("4")) {
      sqlEventRuleList.removeIf(rule -> rule.getRuleId() == ruleId);
    } else {
      sqlEventRuleList.removeIf(rule -> rule.getRuleId() == ruleId);
      sqlEventRuleList.add(sqlEventRule);
    }
  }
}
