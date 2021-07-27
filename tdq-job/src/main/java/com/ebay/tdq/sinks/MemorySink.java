package com.ebay.tdq.sinks;

import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.rules.PhysicalPlan;
import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @author juntzhang
 */
@Slf4j
@VisibleForTesting
public class MemorySink extends RichSinkFunction<TdqMetric> {

  private static final Map<String, List<TdqMetric>> localCache = new HashMap<>();
  private final String name;
  private final PhysicalPlan plan;

  public MemorySink(String jobName, PhysicalPlan plan) {
    this.name = jobName;
    this.plan = plan;
    if (localCache.get(jobName) != null) {
      localCache.get(jobName).clear();
    }
  }

  public boolean check0(List<TdqMetric> expectedList, List<TdqMetric> actualList) {
    assert actualList.size() == expectedList.size();
    Map<String, TdqMetric> m = new HashMap<>();
    expectedList.forEach(v -> {
      String time = DateFormatUtils.format(v.getEventTime(), "yyyy-MM-dd HH:mm:ss");
      m.put(v.getTagId() + " " + time, v);
    });
    boolean success = true;

    for (TdqMetric actual : actualList) {
      String time = DateFormatUtils.format(actual.getEventTime(), "yyyy-MM-dd HH:mm:ss");
      TdqMetric expected = m.get(actual.getTagId() + " " + time);
      if (expected == null) {
        log.error("can not find {} in list1", actual);
        success = false;
        break;
      }
      plan.evaluate(actual);
      success = Math.abs(expected.getValue() - actual.getValue()) < 0.00001;
    }
    return success;
  }

  @Override
  public void invoke(TdqMetric metric, Context context) {
    localCache.compute(name, (k, v) -> {
      if (v == null) {
        v = new ArrayList<>();
      }
      v.add(metric);
      return v;
    });
  }

  public boolean check(List<TdqMetric> expectedList) {
    log.info("memory=>");
    localCache.get(name).forEach(System.out::println);
    return check0(expectedList, localCache.get(name));
  }
}
