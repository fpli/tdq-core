package com.ebay.sojourner.ubd.rt.common.metrics;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.detectors.EventBotDetector;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class EventRulesCounterMetricsCollector extends RichSinkFunction<UbiEvent> {

  private List<Long> eventDynamicRuleCounterNameList = new CopyOnWriteArrayList<>();
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
  public void invoke(UbiEvent ubiEvent, Context context) throws Exception {
    eventTotalCounter.inc();
    List<Long> dynamicRuleIdList = EventBotDetector.dynamicRuleIdList();
    Collection intersection = CollectionUtils
        .intersection(dynamicRuleIdList, eventDynamicRuleCounterNameList);
    if (CollectionUtils.isNotEmpty(intersection)) {
      for (Object ruleId : intersection) {
        Counter dynamicRuleCounter = getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("rule" + ruleId);
        eventRuleCounterNameMap.put("rule" + ruleId,dynamicRuleCounter);
        eventDynamicRuleCounterNameList.add((Long) ruleId);
      }
    }
    ruleHitCount(ubiEvent.getBotFlags());
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
