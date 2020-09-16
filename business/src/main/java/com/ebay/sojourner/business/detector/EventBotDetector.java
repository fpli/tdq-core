package com.ebay.sojourner.business.detector;

import com.ebay.sojourner.business.rule.RuleChangeEventListener;
import com.ebay.sojourner.business.rule.RuleManager;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.rule.RuleCategory;
import com.ebay.sojourner.common.model.rule.RuleChangeEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import com.ebay.sojourner.dsl.sql.SQLEventRule;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBotDetector implements
    BotDetector<UbiEvent>, RuleChangeEventListener<RuleChangeEvent> {

  private List<SQLEventRule> sqlRules = Lists.newArrayList();
  private final Set<Integer> botFlags = Sets.newHashSet();

  public EventBotDetector() {
    RuleManager ruleManager = RuleManager.getInstance();
    ruleManager.addListener(this);
    this.initBotRules();
  }

  @Override
  public Set<Integer> getBotFlagList(UbiEvent ubiEvent) {
    botFlags.clear();
    for (SQLEventRule rule : sqlRules) {
      int flag = rule.execute(ubiEvent);
      if (flag > 0) {
        botFlags.add(flag);
      }
    }

    return botFlags;
  }

  @Override
  public void initBotRules() {
    RuleManager ruleManager = RuleManager.getInstance();
    Set<RuleDefinition> rules = ruleManager.getEventRuleDefinitions();

    sqlRules = rules.stream()
        .map(SQLEventRule::new)
        .collect(Collectors.toList());
  }

  @Override
  public void onChange(RuleChangeEvent ruleChangeEvent) {
    log.info("Event sql rule changed, change event: {}", ruleChangeEvent);
    sqlRules = ruleChangeEvent.getRules()
        .stream()
        .map(SQLEventRule::new)
        .collect(Collectors.toList());
  }

  @Override
  public RuleCategory category() {
    return RuleCategory.EVENT;
  }
}
