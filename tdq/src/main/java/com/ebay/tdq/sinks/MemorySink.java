package com.ebay.tdq.sinks;

import com.ebay.tdq.rules.TdqMetric;
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
public class MemorySink extends RichSinkFunction<TdqMetric> {
  private static final Map<String, List<TdqMetric>> collect = new HashMap<>();
  private final String name;

  public MemorySink(String name) {
    this.name = name;
    if (collect.get(name) != null) {
      collect.get(name).clear();
    }
  }

  @Override
  public void invoke(TdqMetric metric, Context context) {
    collect.compute(name, (k, v) -> {
      if (v == null) {
        v = new ArrayList<>();
      }
      v.add(metric);
      return v;
    });
  }

  public boolean check(List<TdqMetric> expectedList) {
    assert expectedList.size() == collect.get(name).size();
    Map<String, TdqMetric> m = new HashMap<>();
    collect.get(name).forEach(v -> {
      String time = DateFormatUtils.format(v.getEventTime(), "yyyy-MM-dd HH:mm:ss");
      m.put(v.getUid() + " " + time, v);
    });
    boolean success = true;
    for (TdqMetric expected : expectedList) {
      String time = DateFormatUtils.format(expected.getEventTime(), "yyyy-MM-dd HH:mm:ss");
      TdqMetric actual = m.get(expected.getUid() + " " + time);
      if (actual == null) {
        log.error("can not find, expected=>{}", expected);
        success = false;
        continue;
      }
      if (actual.getValue() - expected.getValue() > 0.0001d) {
        log.error("actual=>{}", actual);
        log.error("expected=>{}", expected);
        success = false;
      }
    }
    return success;
  }
}
