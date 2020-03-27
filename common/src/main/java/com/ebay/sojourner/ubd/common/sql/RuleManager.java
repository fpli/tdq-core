package com.ebay.sojourner.ubd.common.sql;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

public class RuleManager {

  protected static final Logger LOGGER = Logger.getLogger(RuleManager.class);
  public static boolean RULE_PULL_ENABLED = true;
  public static boolean RULE_PUSH_ENABLED = false;
  private static final RuleManager INSTANCE = new RuleManager();

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

  public List<SqlEventRule> sqlEventRules() {
    return this.sqlEventRuleList;
  }

  protected void fetchRules() {
    ruleFetcher.fetchRules();
  }

  protected void updateRules(List<RuleDefinition> ruleDefinitions) {
    // TODO: We should first compare pulled rules and exiting rules.
    //  Also we should respect rule categories e.g. event, session, attribute.
    for (RuleDefinition ruleDef : ruleDefinitions) {
      try {
        sqlEventRuleList.add(SqlEventRule.of(ruleDef.getContent()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.out.println("Rules deployed: " + sqlEventRuleList.size());
  }

  public static void main(String[] args) throws Exception {
    RuleManager.getInstance();
    Thread.sleep(10 * 60 * 1000);
  }
}
