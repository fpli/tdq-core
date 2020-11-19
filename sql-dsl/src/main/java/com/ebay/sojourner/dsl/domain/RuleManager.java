package com.ebay.sojourner.dsl.domain;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.dsl.domain.rule.RuleCategory;
import com.ebay.sojourner.dsl.domain.rule.RuleChangeEvent;
import com.ebay.sojourner.dsl.domain.rule.RuleChangeEventType;
import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public class RuleManager {

  private static final RuleManager INSTANCE = new RuleManager();
  private RuleFetcher ruleFetcher;
  private ExecutorService zkExecutor;
  private ScheduledExecutorService schedulingExecutor;
  private final List<RuleChangeEventListener<RuleChangeEvent>> listeners = Lists.newLinkedList();
  public boolean isInitDone = false;

  @Getter
  private Set<RuleDefinition> eventRuleDefinitions = Sets.newHashSet();
  @Getter
  private Set<RuleDefinition> sessionRuleDefinitions = Sets.newHashSet();
  @Getter
  private Set<RuleDefinition> attributeRuleDefinitions = Sets.newHashSet();

  private RuleManager() {

    final boolean HOT_DEPLOY = EnvironmentUtils.getBoolean("flink.app.hot-deploy");

    // default hot deploy is disabled
    if (HOT_DEPLOY) {
      log.info("Rule hot deployment enabled.");
      ruleFetcher = new RuleFetcher();
      zkExecutor = Executors.newSingleThreadExecutor();
      schedulingExecutor = Executors.newSingleThreadScheduledExecutor();

      // 1. init rules
      initRules();
      // 2. init scheduling
      initScheduler(schedulingExecutor, 60L, 60L);
    }
  }

  public static RuleManager getInstance() {
    return INSTANCE;
  }

  public void addListener(RuleChangeEventListener<RuleChangeEvent> listener) {
    this.listeners.add(listener);
  }

  public void close() {
    if (zkExecutor != null && !zkExecutor.isShutdown()) {
      zkExecutor.shutdown();
    }
    if (schedulingExecutor != null && !schedulingExecutor.isShutdown()) {
      schedulingExecutor.shutdown();
    }
  }

  private void initRules() {
    try {
      log.info("Init all rules");
      List<RuleDefinition> ruleDefinitionList = ruleFetcher.fetchAllRules();
      loadRuleDef(ruleDefinitionList);
      log.info("Init rules done.");
      isInitDone = true;
    } catch (Throwable e) {
      log.error("Cannot init rules", e);
      throw new RuntimeException("Cannot init rules");
    }
  }

  private void loadRuleDef(List<RuleDefinition> ruleDefinitionList) {
    if (CollectionUtils.isNotEmpty(ruleDefinitionList)) {
      for (RuleDefinition ruleDefinition : ruleDefinitionList) {
        if (ruleDefinition.getCategory().equalsIgnoreCase("event")) {
          eventRuleDefinitions.add(ruleDefinition);
        } else if (ruleDefinition.getCategory().equalsIgnoreCase("session")) {
          sessionRuleDefinitions.add(ruleDefinition);
        } else if (ruleDefinition.getCategory().equalsIgnoreCase("attribute")) {
          attributeRuleDefinitions.add(ruleDefinition);
        }
      }
    } else {
      log.info("No rules returned.");
    }
  }

  private void initScheduler(ScheduledExecutorService schedulingExecutor,
                             Long initDelayInSeconds, Long delayInSeconds) {
    schedulingExecutor.scheduleWithFixedDelay(() -> {
      log.info("Scheduled to update all rules");
      Set<RuleDefinition> rules = null;

      try {
        rules = Sets.newHashSet(ruleFetcher.fetchAllRules());
        if (CollectionUtils.isNotEmpty(rules)) {
          Set<RuleDefinition> newEventRules = rules.stream()
              .filter(r -> r.getCategory().equalsIgnoreCase("event"))
              .collect(Collectors.toSet());
          if (ruleHasChanges(eventRuleDefinitions, newEventRules)) {
            this.eventRuleDefinitions = newEventRules;
            notifyListeners(listeners.stream()
                                     .filter(r -> r.category().equals(RuleCategory.EVENT))
                                     .collect(Collectors.toList()),
                            eventRuleDefinitions);
          }
          Set<RuleDefinition> newSessionRules = rules.stream()
              .filter(r -> r.getCategory().equalsIgnoreCase("session"))
              .collect(Collectors.toSet());
          if (ruleHasChanges(sessionRuleDefinitions, newSessionRules)) {
            this.sessionRuleDefinitions = newSessionRules;
            notifyListeners(listeners.stream()
                                     .filter(r -> r.category().equals(RuleCategory.SESSION))
                                     .collect(Collectors.toList()),
                            sessionRuleDefinitions);
          }
          Set<RuleDefinition> newAttributeRules = rules.stream()
              .filter(r -> r.getCategory().equalsIgnoreCase("attribute"))
              .collect(Collectors.toSet());
          if (ruleHasChanges(attributeRuleDefinitions, newAttributeRules)) {
            this.attributeRuleDefinitions = newAttributeRules;
            notifyListeners(listeners.stream()
                                     .filter(r -> r.category().equals(RuleCategory.ATTRIBUTE))
                                     .collect(Collectors.toList()),
                            attributeRuleDefinitions);
          }
        } else {
          log.info("No rules returned.");
        }
      } catch (Exception e) {
        log.error("Cannot update rules", e);
      }
    }, initDelayInSeconds, delayInSeconds, TimeUnit.SECONDS);
  }

  private void notifyListeners(List<RuleChangeEventListener<RuleChangeEvent>> listeners,
                               Set<RuleDefinition> ruleDefinitions) {
    log.info("Rule Changed, notifying listeners");
    RuleChangeEvent ruleChangeEvent = new RuleChangeEvent();
    ruleChangeEvent.setType(RuleChangeEventType.UPDATE);
    ruleChangeEvent.setRules(ruleDefinitions);
    ruleChangeEvent.setLocalDateTime(LocalDateTime.now());

    for (RuleChangeEventListener<RuleChangeEvent> listener : listeners) {
      listener.onChange(ruleChangeEvent);
    }
  }

  private boolean ruleHasChanges(Set<RuleDefinition> originalRules, Set<RuleDefinition> newRules) {
    if (originalRules.size() != newRules.size()) {
      return true;
    }

    for (RuleDefinition rule : newRules) {
      if (!originalRules.contains(rule)) {
        return true;
      }
    }
    return false;
  }

}