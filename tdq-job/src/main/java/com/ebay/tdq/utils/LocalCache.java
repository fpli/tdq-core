package com.ebay.tdq.utils;

import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
public class LocalCache {
  private final Map<String, TdqMetric> cache;
  private final int localCombineQueueSize;
  private final int localCombineFlushTimeout;
  private final TdqMetricGroup metricGroup;
  private long cacheCurrentTimeMillis;

  public LocalCache(int localCombineQueueSize, int localCombineFlushTimeout,
      TdqMetricGroup metricGroup) {
    this.localCombineQueueSize    = localCombineQueueSize;
    this.cacheCurrentTimeMillis   = System.currentTimeMillis();
    this.localCombineFlushTimeout = localCombineFlushTimeout;
    this.metricGroup              = metricGroup;
    this.cache                    = new HashMap<>(localCombineQueueSize + 16);
  }

  protected boolean needFlush(TdqMetric curr) {
    return cache.size() >= localCombineQueueSize
        || (System.currentTimeMillis() - cacheCurrentTimeMillis) > localCombineFlushTimeout;
  }

  public Collection<TdqMetric> values() {
    return cache.values();
  }

  public void put(String k, TdqMetric v) {
    cache.put(k, v);
  }

  public int size() {
    return cache.size();
  }

  public void flush(PhysicalPlan plan, TdqMetric curr, Collector<TdqMetric> collector) {
    if (curr == null) return;
    metricGroup.inc(curr.getMetricKey() + "_" + DateUtils.getMinBuckets(curr.getEventTime(), 5));
    TdqMetric last = cache.get(curr.getCacheId());
    if (last != null) {
      curr = plan.merge(last, curr);
    }
    cache.put(curr.getCacheId(), curr);

    if (needFlush(curr)) {
      metricGroup.inc("flush");
      if (cache.size() >= localCombineQueueSize) {
        metricGroup.inc("sizeFlush");
      } else {
        metricGroup.inc("flushTimeout");
      }
      for (TdqMetric m : cache.values()) {
        if (m.getExprMap() != null && m.getExprMap().get("p1") != null) {
          metricGroup.inc(
              m.getMetricKey() + "_" + DateUtils.getMinBuckets(m.getEventTime(), 5) + "_2",
              (long) (double) m.getExprMap().get("p1")
          );
        }
        collector.collect(m);
      }
      metricGroup.markEvent();
      cache.clear();
      cacheCurrentTimeMillis = System.currentTimeMillis();
    }
  }
}
