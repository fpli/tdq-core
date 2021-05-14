package com.ebay.tdq.functions;

import com.codahale.metrics.SlidingWindowReservoir;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.RheosHeader;
import com.ebay.sojourner.flink.connector.kafka.TimestampFieldExtractor;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.JdbcConfig;
import com.ebay.tdq.utils.JsonUtils;
import com.ebay.tdq.utils.PhysicalPlanFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.Histogram;
import org.apache.flink.metrics.Meter;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_FLUSH_TIMEOUT;
import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_QUEUE_SIZE;

/**
 * todo add Checkpoint Function, add check cache size for dims
 * https://stackoverflow.com/questions/47825565/apache-flink-how-can-i-compute-windows-with-local-pre-aggregation
 * when started broadcast cause event dropped,
 *
 * @author juntzhang
 */
@Slf4j
public class TdqRawEventProcessFunction
    extends BroadcastProcessFunction<RawEvent, PhysicalPlans, TdqMetric> {
  protected final JdbcConfig jdbcConfig;
  private final MapStateDescriptor<Integer, PhysicalPlans> stateDescriptor;
  private final int localCombineFlushTimeout;
  private final int localCombineQueueSize;
  protected long cacheCurrentTimeMillis = System.currentTimeMillis();
  protected long logCurrentTimeMillis = 0L;
  private Counter tdqErrorEventsCount;
  private Counter flush;
  private Counter flushSize;
  private Counter flushTimeout;
  private Meter tdqProcessEventsMeter;
  private Meter tdqProcessElementMeter;
  private Meter tdqCollectMeter;
  private Meter tdqCollectEventMeter;
  private Histogram tdqProcessMetricHistogram;
  private Histogram tdqProcessElementHistogram;
  private transient Map<String, TdqMetric> cache;
  private PhysicalPlans physicalPlans;

  public TdqRawEventProcessFunction(MapStateDescriptor<Integer, PhysicalPlans> descriptor) {
    this.stateDescriptor          = descriptor;
    this.localCombineFlushTimeout = LOCAL_COMBINE_FLUSH_TIMEOUT;
    this.localCombineQueueSize    = LOCAL_COMBINE_QUEUE_SIZE;
    if (LOCAL_COMBINE_FLUSH_TIMEOUT > 59000) {
      throw new RuntimeException("flink.app.advance.local-combine.flush-timeout must less than 59s!");
    }
    this.jdbcConfig = new JdbcConfig();
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    tdqProcessEventsMeter      = getRuntimeContext().getMetricGroup().meter("tdqProcessEventsMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqCollectMeter            = getRuntimeContext().getMetricGroup().meter("tdqCollectMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqCollectEventMeter       = getRuntimeContext().getMetricGroup().meter("tdqCollectEventMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    tdqProcessElementMeter     = getRuntimeContext().getMetricGroup().meter("tdqProcessElementMeter",
        new DropwizardMeterWrapper(new com.codahale.metrics.Meter()));
    flush                      = getRuntimeContext().getMetricGroup().counter("tdqFlush");
    flushSize                  = getRuntimeContext().getMetricGroup().counter("tdqFlushSize");
    flushTimeout               = getRuntimeContext().getMetricGroup().counter("tdqFlushTimeout");
    tdqErrorEventsCount        = getRuntimeContext().getMetricGroup().counter("tdqErrorEventsCount");
    tdqProcessMetricHistogram  = getRuntimeContext().getMetricGroup().histogram("tdqProcessMetricHistogram",
        new DropwizardHistogramWrapper(
            new com.codahale.metrics.Histogram(new SlidingWindowReservoir(10))));
    tdqProcessElementHistogram = getRuntimeContext().getMetricGroup().histogram("tdqProcessElementHistogram",
        new DropwizardHistogramWrapper(
            new com.codahale.metrics.Histogram(new SlidingWindowReservoir(10))));
    final Map<String, TdqMetric> _cache = new HashMap<>(localCombineQueueSize + 16);
    this.cache = _cache;
    getRuntimeContext().getMetricGroup().gauge("cacheSize", (Gauge<Integer>) () -> _cache.size());
    this.physicalPlans = getPhysicalPlans();
  }


  protected PhysicalPlans getPhysicalPlans() {
    return PhysicalPlanFactory.getPhysicalPlans(PhysicalPlanFactory.getTdqConfigs(this.jdbcConfig));
  }


  protected boolean needFlush() {
    return cache.size() >= localCombineQueueSize
        || (System.currentTimeMillis() - cacheCurrentTimeMillis) > localCombineFlushTimeout;
  }

  private void collect(PhysicalPlan plan, TdqMetric curr, ReadOnlyContext ctx, Collector<TdqMetric> collector) {
    TdqMetric last = cache.get(curr.getCacheId());
    if (last != null) {
      curr = plan.merge(last, curr);
    }
    cache.put(curr.getCacheId(), curr);

    if (needFlush()) {
      flush.inc();
      if (cache.size() >= localCombineQueueSize) {
        flushSize.inc();
      } else {
        flushTimeout.inc();
      }
      for (TdqMetric m : cache.values()) {
        collector.collect(m);
      }
      tdqCollectEventMeter.markEvent(cache.size());
      tdqCollectMeter.markEvent();
      cache.clear();
      cacheCurrentTimeMillis = System.currentTimeMillis();
    }
  }

  @Override
  public void processElement(RawEvent event, ReadOnlyContext ctx, Collector<TdqMetric> collector) throws Exception {
    long s1 = System.nanoTime();
    ReadOnlyBroadcastState<Integer, PhysicalPlans> broadcastState = ctx.getBroadcastState(stateDescriptor);
    Iterator<Map.Entry<Integer, PhysicalPlans>> iter = broadcastState.immutableEntries().iterator();
    if (iter.hasNext()) {
      Map.Entry<Integer, PhysicalPlans> entry = iter.next();
      if (entry.getKey() > 0) {
        this.physicalPlans = entry.getValue();
      }
    }

    if (physicalPlans == null || physicalPlans.plans().length == 0) {
      throw new Exception("physical plans is empty!");
    }

    for (PhysicalPlan plan : physicalPlans.plans()) {
      long s = System.nanoTime();
      TdqMetric metric = process(event, plan);
      if (metric != null) {
        collect(plan, metric, ctx, collector);
        tdqProcessMetricHistogram.update(System.nanoTime() - s);
      }
      tdqProcessEventsMeter.markEvent();
    }

    tdqProcessElementMeter.markEvent();
    tdqProcessElementHistogram.update(System.nanoTime() - s1);
  }

  private TdqMetric process(RawEvent event, PhysicalPlan plan) {
    try {
      return plan.process(event, TimestampFieldExtractor.getField(event));
    } catch (Exception e) {
      tdqErrorEventsCount.inc();
      if ((System.currentTimeMillis() - logCurrentTimeMillis) > 30000) {
        log.warn(e.getMessage(), e);
        log.warn("Drop event={},plan={}", event, plan);
        logCurrentTimeMillis = System.currentTimeMillis();
      }
      return null;
    }
  }

  private String getRawEventStr(RawEvent event) throws Exception {
    RheosHeader header = event.getRheosHeader();
    event.setRheosHeader(null);
    String str = JsonUtils.toJSONString(event);
    event.setRheosHeader(header);
    return str;
  }


  @Override
  public void processBroadcastElement(PhysicalPlans plan,
      Context ctx, Collector<TdqMetric> collector) throws Exception {
    BroadcastState<Integer, PhysicalPlans> broadcastState = ctx.getBroadcastState(stateDescriptor);
    broadcastState.put(plan.plans().length, plan);
  }
}
