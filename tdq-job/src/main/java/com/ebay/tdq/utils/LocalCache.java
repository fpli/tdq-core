package com.ebay.tdq.utils;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.rules.PhysicalPlan;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
public class LocalCache {

  private final TdqEnv tdqEnv;
  private final Map<String, InternalMetric> cache;
  private final TdqMetricGroup metricGroup;
  private final Random random = new Random();
  private long cacheCurrentTimeMillis;

  public LocalCache(TdqEnv tdqEnv, TdqMetricGroup metricGroup) {
    this.tdqEnv = tdqEnv;
    this.cacheCurrentTimeMillis = System.currentTimeMillis();
    this.metricGroup = metricGroup;
    this.cache = new HashMap<>(tdqEnv.getLocalCombineQueueSize() + 16);
  }

  protected boolean needFlush(InternalMetric curr) {
    return cache.size() >= tdqEnv.getLocalCombineQueueSize()
        || (System.currentTimeMillis() - cacheCurrentTimeMillis) > tdqEnv
        .getLocalCombineFlushTimeout();
  }

  public Collection<InternalMetric> values() {
    return cache.values();
  }

  public void put(String k, InternalMetric v) {
    cache.put(k, v);
  }

  public int size() {
    return cache.size();
  }

  public void flush(PhysicalPlan plan, InternalMetric curr, Collector<InternalMetric> collector) {
    if (curr == null) {
      return;
    }
    metricGroup.inc(curr.getMetricName() + "_" + DateUtils.getMinBuckets(
        curr.getEventTime(), 5, tdqEnv.getTimeZone()));
    InternalMetric last = cache.get(curr.getMetricIdWithEventTime());
    if (last != null) {
      curr = plan.merge(last, curr);
    }
    cache.put(curr.getMetricIdWithEventTime(), curr);

    if (needFlush(curr)) {
      metricGroup.inc("flush");
      if (cache.size() >= tdqEnv.getLocalCombineQueueSize()) {
        metricGroup.inc("sizeFlush");
      } else {
        metricGroup.inc("flushTimeout");
      }
      for (InternalMetric m : cache.values()) {
        m.setPartition(Math.abs(random.nextInt()) % tdqEnv.getOutputPartitions());
        metricGroup.inc("localCachePartition" + m.getPartition());
        metricGroup.inc("collect");
        collector.collect(m);
      }
      metricGroup.markEvent();
      cache.clear();
      cacheCurrentTimeMillis = System.currentTimeMillis();
    }
  }
}
