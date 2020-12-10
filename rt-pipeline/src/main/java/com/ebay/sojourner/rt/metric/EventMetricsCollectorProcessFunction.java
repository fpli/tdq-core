package com.ebay.sojourner.rt.metric;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.dsl.domain.RuleManager;
import com.ebay.sojourner.dsl.domain.rule.RuleDefinition;
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

  private Set<Long> dynamicRuleIdOldSet = new CopyOnWriteArraySet<>();
  private List<String> eventStaticRuleList;
  private Map<String, Counter> eventRuleCounterMap = new ConcurrentHashMap<>();
  private Counter eventTotalCounter;
  private static final String ruleCounterPreffix = "rule";
  private static final String ubiEventCounterName = "ubiEvent_count";

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    // total event count
    eventTotalCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter(ubiEventCounterName);

    // static rule
    eventStaticRuleList = Arrays
        .asList("rule801", "rule1", "rule802", "rule803", "rule804", "rule805", "rule806",
            "rule807", "rule810", "rule811", "rule812", "rule813", "rule856", "rule5", "rule6",
            "rule7", "rule8", "rule9", "rule10", "rule11", "rule12", "rule15", "rule203", "rule204",
            "rule205", "rule206", "rule207", "rule208", "rule212", "rule215", "rule220",
            "rule221", "rule223", "rule224");

    for (String ruleName : eventStaticRuleList) {
      Counter staticRuleCounter =
          getRuntimeContext()
              .getMetricGroup()
              .addGroup(Constants.SOJ_METRICS_GROUP)
              .counter(ruleName);
      eventRuleCounterMap.put(ruleName, staticRuleCounter);
    }
  }

  @Override
  public void processElement(UbiEvent ubiEvent, Context ctx, Collector<UbiEvent> out) {

    eventTotalCounter.inc();
    Set<Long> dynamicRuleIdNewSet = RuleManager
        .getInstance().getEventRuleDefinitions()// FIXME: redesign this
        .stream()
        .map(RuleDefinition::getBizId)
        .collect(Collectors.toSet());

    if (CollectionUtils.isNotEmpty(dynamicRuleIdNewSet)) {
      Collection intersection = CollectionUtils
          .intersection(dynamicRuleIdNewSet, dynamicRuleIdOldSet);
      if (CollectionUtils.isNotEmpty(intersection)) {
        for (Object ruleId : intersection) {
          Counter dynamicRuleCounter = getRuntimeContext()
              .getMetricGroup()
              .addGroup(Constants.SOJ_METRICS_GROUP)
              .counter(ruleCounterPreffix + ruleId);
          eventRuleCounterMap.put(ruleCounterPreffix + ruleId, dynamicRuleCounter);
          dynamicRuleIdOldSet.add((Long) ruleId);
        }
      }
    }
    ruleHitCount(ubiEvent.getBotFlags());
    out.collect(null);
  }

  private void ruleHitCount(Set<Integer> botFlags) {

    if (!botFlags.isEmpty()) {
      for (int botRule : botFlags) {
        Counter counter = eventRuleCounterMap.get(ruleCounterPreffix + botRule);
        if (botRule != 0 && counter != null) {
          counter.inc();
        }
      }
    }
  }
}
