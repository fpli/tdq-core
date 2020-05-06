package com.ebay.sojourner.ubd.common.sql;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public class RuleManager {

  private static final RuleManager INSTANCE = new RuleManager();
  private RuleFetcher ruleFetcher;
  private RuleNotificationListener ruleNotificationListener;

  private Set<SqlEventRule> sqlEventRuleSet = new CopyOnWriteArraySet<>();
  private Set<Long> eventRuleIdSet = new CopyOnWriteArraySet<>();

  private RuleManager() {

    ruleFetcher = new RuleFetcher(this);

    // ruleFetcher.fetchRules();

    ruleNotificationListener = new RuleNotificationListener(ruleFetcher);
    new Thread(
        () -> {
          ruleNotificationListener.listen();
        }, "Sojourner RuleNotificationListener Thread")
        .start();

    // ruleFetcher.fetchRulesPeriodically();
  }

  public static RuleManager getInstance() {
    return INSTANCE;
  }

  public Set<SqlEventRule> sqlEventRules() {
    return this.sqlEventRuleSet;
  }

  public Set<Long> ruleIdSet() {
    return this.eventRuleIdSet;
  }

  public void close() {
    ruleNotificationListener.close();
  }

  public void updateRules(List<RuleDefinition> ruleDefinitions) {

    if (CollectionUtils.isNotEmpty(ruleDefinitions)) {

      sqlEventRuleSet = ruleDefinitions
          .stream()
          .filter(rule -> rule.getIsActive())
          .map(rule -> SqlEventRule
              .of(rule.getContent(), rule.getBizId(), rule.getVersion(), rule.getCategory()))
          .collect(Collectors.toSet());
    }

    log.info("Rules deployed: " + this.sqlEventRuleSet.size());
  }

  public void updateRulesById(RuleDefinition ruleDefinition, Long ruleId) {

    eventRuleIdSet = sqlEventRuleSet
        .stream()
        .map(SqlEventRule::getRuleId)
        .collect(Collectors.toSet());

    if (ruleDefinition.getIsActive()) {

      SqlEventRule sqlEventRule = SqlEventRule
          .of(ruleDefinition.getContent(), ruleDefinition.getBizId(), ruleDefinition.getVersion(),
              ruleDefinition.getCategory());
      if (!eventRuleIdSet.contains(ruleId)) {
        sqlEventRuleSet.add(sqlEventRule);
      } else {
        sqlEventRuleSet.removeIf(rule -> rule.getRuleId() == ruleId);
        sqlEventRuleSet.add(sqlEventRule);
      }
    } else {
      if (eventRuleIdSet.contains(ruleId)) {
        sqlEventRuleSet.removeIf(rule -> rule.getRuleId() == ruleId);
        eventRuleIdSet.remove(ruleId);
      }
    }

    eventRuleIdSet = sqlEventRuleSet
        .stream()
        .map(SqlEventRule::getRuleId)
        .collect(Collectors.toSet());
  }

  public static void main(String[] args) throws Exception {
    RuleManager.getInstance();
    Thread.sleep(10 * 60 * 1000);
  }

}
