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

  private final RuleManager ruleManager = RuleManager.getInstance();

  protected Set<SqlEventRule> sqlRules = Sets.newCopyOnWriteArraySet();

  public AbstractBotDetector() {
    this.initBotRules();
  }

  //FIXME(Jason): this init method should be removed
  @Override
  public void initBotRules() {
    Set<RuleDefinition> ruleDefinitions = ruleManager.getRuleDefinitions();
    if (CollectionUtils.isNotEmpty(ruleDefinitions)) {
      this.sqlRules = ruleDefinitions
          .stream()
          .map(rule -> SqlEventRule
              .of(rule.getContent(), rule.getBizId(), rule.getVersion(), rule.getCategory()))
          .collect(Collectors.toSet());
    }
  }

  public void close() {
    ruleManager.close();
  }

}
