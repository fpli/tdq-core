package com.ebay.sojourner.business.ubd.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sql.RuleChangeEvent;
import com.ebay.sojourner.ubd.common.sql.RuleDefinition;
import com.ebay.sojourner.ubd.common.sql.SqlEventRule;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public class EventBotDetector extends AbstractBotDetector<UbiEvent> {

  @Override
  public Set<Integer> getBotFlagList(UbiEvent ubiEvent) {
    Set<Integer> botRuleList = Sets.newHashSet();
    if (CollectionUtils.isNotEmpty(sqlRules)) {
      for (SqlEventRule rule : sqlRules) {
        rule.init();
        int botRule = rule.getBotFlag(ubiEvent);
        if (botRule != 0) {
          botRuleList.add(botRule);
        }
      }
    }
    return botRuleList;
  }

  @Override
  public void onChange(RuleChangeEvent ruleChangeEvent) {
    Set<RuleDefinition> ruleDefinitions = ruleChangeEvent.getRules();
    if (CollectionUtils.isNotEmpty(ruleDefinitions)) {
      this.sqlRules = ruleDefinitions
          .stream()
          .map(rule -> SqlEventRule
              .of(rule.getContent(), rule.getBizId(), rule.getVersion(), rule.getCategory()))
          .collect(Collectors.toSet());
    }
    log.info("Deployed rules count: {}", this.sqlRules.size());
  }
}
