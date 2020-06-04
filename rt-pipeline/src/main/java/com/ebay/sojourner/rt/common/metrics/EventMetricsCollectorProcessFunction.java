package com.ebay.sojourner.rt.common.metrics;

import com.ebay.sojourner.business.ubd.rule.RuleManager;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class EventMetricsCollectorProcessFunction extends ProcessFunction<UbiEvent, UbiEvent> {

  private Set<Long> eventDynamicRuleCounterNameSet = new CopyOnWriteArraySet<>();
  private List<String> eventStaticRuleCounterNameList;
  private Map<String, Counter> eventRuleCounterNameMap = new ConcurrentHashMap<>();
  private Counter eventTotalCounter;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    // total event count
    eventTotalCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("ubiEvent count");

    // static rule
    eventStaticRuleCounterNameList = Arrays.asList("rule801", "rule1");

    for (String ruleName : eventStaticRuleCounterNameList) {
      Counter staticRuleCounter =
          getRuntimeContext()
              .getMetricGroup()
              .addGroup("sojourner-ubd")
              .counter(ruleName);
      eventRuleCounterNameMap.put(ruleName, staticRuleCounter);
    }
  }

  @Override
  public void processElement(UbiEvent ubiEvent, Context ctx, Collector<UbiEvent> out) {

    eventTotalCounter.inc();
    Set<Long> dynamicRuleIdSet = RuleManager
        .getInstance().getRuleDefinitions()
        .stream()
        .map(RuleDefinition::getBizId)
        .collect(Collectors.toSet());

    if (CollectionUtils.isNotEmpty(dynamicRuleIdSet)) {
      Collection intersection = CollectionUtils
          .intersection(dynamicRuleIdSet, eventDynamicRuleCounterNameSet);
      if (CollectionUtils.isNotEmpty(intersection)) {
        for (Object ruleId : intersection) {
          Counter dynamicRuleCounter = getRuntimeContext()
              .getMetricGroup()
              .addGroup("sojourner-ubd")
              .counter("rule" + ruleId);
          eventRuleCounterNameMap.put("rule" + ruleId, dynamicRuleCounter);
          eventDynamicRuleCounterNameSet.add((Long) ruleId);
        }
      }
    }
    ruleHitCount(ubiEvent.getBotFlags());
    out.collect(null);
  }

  private void ruleHitCount(Set<Integer> botFlags) {

    if (!botFlags.isEmpty()) {
      for (int botRule : botFlags) {
        Counter counter = eventRuleCounterNameMap.get("rule" + botRule);
        if (botRule != 0 && counter != null) {
          counter.inc();
        }
      }
    }
  }
}
