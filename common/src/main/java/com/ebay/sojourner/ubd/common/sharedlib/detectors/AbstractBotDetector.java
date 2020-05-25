package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sql.RuleManager;
import com.ebay.sojourner.ubd.common.sql.SqlEventRule;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public abstract class AbstractBotDetector<T> implements BotDetector<T> {

  protected RuleManager ruleManager = RuleManager.getInstance();

  public void init() {
    ruleManager.initRules();
  }

  @Override
  public Set<Integer> getBotFlagList(T t) {
    Set<Integer> botRuleList = new LinkedHashSet<>();
    Set<SqlEventRule> botRules = ruleManager.getSqlEventRuleSet();
    if (CollectionUtils.isNotEmpty(botRules)) {
      for (SqlEventRule rule : botRules) {
        rule.init();
        if (t instanceof UbiEvent) {
          UbiEvent ubiEvent = (UbiEvent) t;
          int botRule = rule.getBotFlag(ubiEvent);
          if (botRule != 0) {
            botRuleList.add(botRule);
          }
        }
      }
    }
    return botRuleList;
  }

  public void close() {
    ruleManager.close();
  }

}
