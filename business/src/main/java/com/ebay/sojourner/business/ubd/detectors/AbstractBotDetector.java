package com.ebay.sojourner.business.ubd.detectors;

import com.ebay.sojourner.business.ubd.rule.RuleChangeEventListener;
import com.ebay.sojourner.business.ubd.rule.RuleManager;
import com.ebay.sojourner.common.model.rule.RuleChangeEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import com.ebay.sojourner.dsl.sql.SqlEventRule;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public abstract class AbstractBotDetector<T>
    implements BotDetector<T>, RuleChangeEventListener<RuleChangeEvent> {

  protected Set<SqlEventRule> sqlRules = Sets.newCopyOnWriteArraySet();
  protected Set<RuleDefinition> ruleDefinitions;

  public AbstractBotDetector(RuleManager ruleManager) {
    this.ruleDefinitions = ruleManager.getRuleDefinitions();
    ruleManager.addListener(this);
    this.initBotRules();
  }

  //FIXME(Jason): this init method should be removed
  @Override
  public void initBotRules() {
    if (CollectionUtils.isNotEmpty(this.ruleDefinitions)) {
      this.sqlRules = ruleDefinitions
          .stream()
          .map(rule -> SqlEventRule
              .of(rule.getContent(), rule.getBizId(), rule.getVersion(), rule.getCategory()))
          .collect(Collectors.toSet());
    }
  }

}
