package com.ebay.sojourner.business.ubd.detectors;

import com.ebay.sojourner.business.ubd.rule.RuleChangeEventListener;
import com.ebay.sojourner.business.ubd.rule.RuleManager;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.model.rule.RuleCategory;
import com.ebay.sojourner.common.model.rule.RuleChangeEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import com.ebay.sojourner.dsl.sql.SQLSessionRule;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewSessionBotDetector implements
    BotDetector<UbiSession>, RuleChangeEventListener<RuleChangeEvent> {

  private List<SQLSessionRule> sqlSessionRules = Lists.newArrayList();
  private final Set<Integer> botFlags = Sets.newHashSet();

  public NewSessionBotDetector() {
    RuleManager ruleManager = RuleManager.getInstance();
    ruleManager.addListener(this);
    this.initBotRules();
  }

  @Override
  public Set<Integer> getBotFlagList(UbiSession ubiSession)
      throws IOException, InterruptedException {
    botFlags.clear();
    for (SQLSessionRule rule : sqlSessionRules) {
      int flag = rule.execute(ubiSession);
      if (flag > 0) {
        botFlags.add(flag);
      }
    }

    return botFlags;
  }

  @Override
  public void initBotRules() {
    RuleManager ruleManager = RuleManager.getInstance();
    Set<RuleDefinition> rules = ruleManager.getSessionRuleDefinitions();

    sqlSessionRules = rules.stream()
        .map(SQLSessionRule::new)
        .collect(Collectors.toList());
  }

  @Override
  public void onChange(RuleChangeEvent ruleChangeEvent) {
    log.info("Session sql rule changed, change event: {}", ruleChangeEvent);
    sqlSessionRules = ruleChangeEvent.getRules()
        .stream()
        .map(SQLSessionRule::new)
        .collect(Collectors.toList());
  }

  @Override
  public RuleCategory category() {
    return RuleCategory.SESSION;
  }
}
