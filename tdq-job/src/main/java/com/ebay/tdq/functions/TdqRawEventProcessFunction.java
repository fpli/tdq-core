package com.ebay.tdq.functions;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.flink.connector.kafka.SojSerializableTimestampAssigner;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.JdbcConfig;
import com.ebay.tdq.utils.LocalCache;
import com.ebay.tdq.utils.PhysicalPlanFactory;
import com.ebay.tdq.utils.TdqMetricGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_FLUSH_TIMEOUT;
import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_QUEUE_SIZE;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqRawEventProcessFunction
    extends BroadcastProcessFunction<RawEvent, PhysicalPlans, TdqMetric> implements CheckpointedFunction {
  protected final JdbcConfig jdbcConfig;
  private final MapStateDescriptor<String, PhysicalPlans> stateDescriptor;
  private final int localCombineFlushTimeout;
  private final int localCombineQueueSize;
  private final String cfgPlan = "CFG_PLAN";

  private transient long logCurrentTimeMillis;
  private transient TdqMetricGroup metricGroup;
  private transient ListState<TdqMetric> cacheState;
  private transient PhysicalPlans physicalPlans;
  private transient LocalCache localCache;

  public TdqRawEventProcessFunction(MapStateDescriptor<String, PhysicalPlans> descriptor) {
    this.stateDescriptor          = descriptor;
    this.localCombineFlushTimeout = LOCAL_COMBINE_FLUSH_TIMEOUT;
    this.localCombineQueueSize    = LOCAL_COMBINE_QUEUE_SIZE;
    if (LOCAL_COMBINE_FLUSH_TIMEOUT > 59000) {
      throw new RuntimeException("flink.app.advance.local-combine.flush-timeout must less than 59s!");
    }
    this.jdbcConfig = new JdbcConfig();
  }

  @Override
  public void processElement(RawEvent event, ReadOnlyContext ctx, Collector<TdqMetric> collector) throws Exception {
    long s1 = System.nanoTime();
    ReadOnlyBroadcastState<String, PhysicalPlans> broadcastState = ctx.getBroadcastState(stateDescriptor);
    PhysicalPlans plans = broadcastState.get(cfgPlan);
    if (plans != null) {
      metricGroup.inc("broadcastConfig");
      metricGroup.inc("broadcastConfig_" + plans.plans().length);
      if (plans.plans().length > 0) {
        processElement0(event, plans, ctx, collector);
      }
    } else {
      metricGroup.inc("mysqlConfig");
      processElement0(event, physicalPlans, ctx, collector);
    }
    metricGroup.markElement(s1);
  }

  @Override
  public void processBroadcastElement(PhysicalPlans plan,
      Context ctx, Collector<TdqMetric> collector) throws Exception {
    BroadcastState<String, PhysicalPlans> broadcastState = ctx.getBroadcastState(stateDescriptor);
    broadcastState.put(cfgPlan, plan);
    metricGroup.inc("processBroadcastElement");
  }

  @Override
  public void snapshotState(FunctionSnapshotContext context) throws Exception {
    cacheState.clear();
    for (TdqMetric m : localCache.values()) {
      cacheState.add(m);
    }
    metricGroup.inc("snapshotState");
  }

  @Override
  public void initializeState(FunctionInitializationContext context) throws Exception {
    final TypeInformation<TdqMetric> ti = TypeInformation.of(new TypeHint<TdqMetric>() {
    });
    cacheState = context.getOperatorStateStore().getListState(new ListStateDescriptor<>("cacheState", ti));
    initialize();
    if (context.isRestored()) {
      metricGroup.inc("restored");
      cacheState.get().forEach(v -> localCache.put(v.getTagIdWithET(), v));
    }
  }

  private void initialize() {
    logCurrentTimeMillis = 0L;
    metricGroup          = new TdqMetricGroup(getRuntimeContext().getMetricGroup());
    physicalPlans        = getPhysicalPlans();
    localCache           = new LocalCache(localCombineQueueSize, localCombineFlushTimeout, metricGroup);
    metricGroup.gauge(localCache);
  }


  protected PhysicalPlans getPhysicalPlans() {
    return PhysicalPlanFactory.getPhysicalPlans(PhysicalPlanFactory.getTdqConfigs(this.jdbcConfig));
  }

  private void processElement0(RawEvent event,
      PhysicalPlans physicalPlans, ReadOnlyContext ctx, Collector<TdqMetric> collector) throws Exception {
    if (physicalPlans == null || physicalPlans.plans().length == 0) {
      throw new Exception("physical plans is empty!");
    }
    for (PhysicalPlan plan : physicalPlans.plans()) {
      metricGroup.inc(plan.metricKey());
      long s = System.nanoTime();
      TdqMetric metric = process(event, plan);
      localCache.flush(plan, metric, collector);
      metricGroup.updateEventHistogram(s);
    }
  }

  private TdqMetric process(RawEvent event, PhysicalPlan plan) {
    try {
      return plan.process(event, getEventTime(event));
    } catch (Exception e) {
      metricGroup.inc("errorEvent");
      if ((System.currentTimeMillis() - logCurrentTimeMillis) > 30000) {
        log.warn(e.getMessage(), e);
        log.warn("Drop event={},plan={}", event, plan);
        logCurrentTimeMillis = System.currentTimeMillis();
      }
      return null;
    }
  }

  private long getEventTime(RawEvent event) {
    return SojSerializableTimestampAssigner.getEventTime(event);
  }
}
