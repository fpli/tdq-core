package com.ebay.sojourner.ubd.common.sql;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public class RuleManager {

  private static final RuleManager INSTANCE = new RuleManager();
  public static boolean RULE_PULL_ENABLED = true;
  public static boolean RULE_PUSH_ENABLED = false;
  private RuleFetcher ruleFetcher;
  private RuleNotificationListener ruleNotificationListener;

  private List<SqlEventRule> sqlEventRuleList = new CopyOnWriteArrayList<>();

  private RuleManager() {
    ruleFetcher = new RuleFetcher(this);

    if (RULE_PULL_ENABLED) {
      ruleFetcher.fetchRulesPeriodically();
    }

    if (RULE_PUSH_ENABLED) {
      ruleNotificationListener = new RuleNotificationListener(this);
      new Thread(
          () -> {
            ruleNotificationListener.listen();
          }, "Sojourner RuleNotificationListener Thread")
          .start();
    }
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

  protected void fetchRules() {
    ruleFetcher.fetchRules();
  }

  protected void updateRules(List<RuleDefinition> ruleDefinitions) {
    // TODO: We should first compare pulled rules and exiting rules.
    //  Also we should respect rule categories e.g. event, session, attribute.

    if (CollectionUtils.isNotEmpty(ruleDefinitions)) {
      List<SqlEventRule> sqlNewEventRuleList = ruleDefinitions.stream()
          .map(rule -> SqlEventRule.of(rule.getContent(), rule.getBizId(), rule.getVersion()))
          .collect(Collectors.toList());

      sqlEventRuleList = sqlNewEventRuleList;
    }

    log.info("Rules deployed: " + this.sqlEventRuleList.size());
  }
}
