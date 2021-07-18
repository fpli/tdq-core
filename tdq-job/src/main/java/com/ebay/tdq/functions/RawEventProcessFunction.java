package com.ebay.tdq.functions;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.rules.TdqErrorMsg;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.rules.TdqSampleData;
import com.ebay.tdq.utils.LocalCache;
import com.ebay.tdq.utils.PhysicalPlanFactory;
import com.ebay.tdq.utils.TdqEnv;
import com.ebay.tdq.utils.TdqMetricGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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

/**
 * @author juntzhang
 */
@Slf4j
public class RawEventProcessFunction
    extends BroadcastProcessFunction<RawEvent, PhysicalPlans, TdqMetric> implements CheckpointedFunction {
  private final MapStateDescriptor<String, PhysicalPlans> stateDescriptor;
  private final TdqEnv tdqEnv;
  private final String cfgPlan = "CFG_PLAN";
  private transient long debugCurrentTimeMillis;
  private transient long errorMsgCurrentTimeMillis;
  private transient TdqMetricGroup metricGroup;
  private transient ListState<TdqMetric> cacheState;
  private transient PhysicalPlans physicalPlans;
  private transient LocalCache localCache;

  public RawEventProcessFunction(MapStateDescriptor<String, PhysicalPlans> descriptor, TdqEnv tdqEnv) {
    this.stateDescriptor = descriptor;
    this.tdqEnv          = tdqEnv;
    if (tdqEnv.getLocalCombineFlushTimeout() > 60000) {
      throw new RuntimeException("flink.app.advance.local-combine.flush-timeout must less than 60s!");
    }
  }

  @Override
  public void processElement(RawEvent event, ReadOnlyContext ctx, Collector<TdqMetric> collector) throws Exception {
    long s1 = System.nanoTime();
    ReadOnlyBroadcastState<String, PhysicalPlans> broadcastState = ctx.getBroadcastState(stateDescriptor);
    PhysicalPlans plans = broadcastState.get(cfgPlan);
    if (plans != null && ArrayUtils.isNotEmpty(plans.plans())) {
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
      cacheState.get().forEach(v -> localCache.put(v.getTagIdWithEventTime(), v));
    }
  }

  private void initialize() {
    errorMsgCurrentTimeMillis = 0L;
    debugCurrentTimeMillis    = 0L;
    metricGroup               = new TdqMetricGroup(getRuntimeContext().getMetricGroup());
    physicalPlans             = getPhysicalPlans();
    localCache                = new LocalCache(tdqEnv, metricGroup);
    metricGroup.gauge(localCache);
  }


  protected PhysicalPlans getPhysicalPlans() {
    return PhysicalPlanFactory.getPhysicalPlans(PhysicalPlanFactory.getTdqConfigs(this.tdqEnv.getJdbcConfig()));
  }

  private void processElement0(RawEvent event,
      PhysicalPlans physicalPlans, ReadOnlyContext ctx, Collector<TdqMetric> collector) throws Exception {
    if (physicalPlans == null || physicalPlans.plans().length == 0) {
      throw new Exception("physical plans is empty!");
    }
    for (PhysicalPlan plan : physicalPlans.plans()) {
      metricGroup.inc(plan.metricKey());
      if (plan.metricKey().equals("exception")) {
        throw new RuntimeException("this is debugging exception, checking checkpoint recovery.");
      }
      long s = System.nanoTime();
      try {
        TdqMetric metric = plan.process(event);
        debug(ctx, event, metric, plan);
        sampleData(ctx, event, metric, plan);
        localCache.flush(plan, metric, collector);
        metricGroup.updateEventHistogram(s);
      } catch (Exception e) {
        errorMsg(ctx, event, e, plan);
      }
    }
  }


  private void debug(ReadOnlyContext ctx, RawEvent event, TdqMetric metric, PhysicalPlan plan) {
    if ((metric != null && metric.getEventTime() - ctx.currentWatermark() != 0)) {
      metricGroup.inc("debugEvent_" + plan.metricKey());
      if ((System.currentTimeMillis() - debugCurrentTimeMillis) > 5000) {
        String param = "watermark=" + ctx.currentWatermark();
        if (ctx.currentWatermark() > 0) {
          param += ("\nwatermarkFmt=" + DateFormatUtils.format(ctx.currentWatermark(), "yyyy-MM-dd HH:mm:ss"));
        }
        ctx.output(tdqEnv.getDebugOutputTag(), new TdqSampleData(event, metric, plan.metricKey(), param));
      }
      debugCurrentTimeMillis = System.currentTimeMillis();
    }
  }

  private void sampleData(ReadOnlyContext ctx, RawEvent event, TdqMetric metric, PhysicalPlan plan) {
    if (plan.sampling()) {
      metricGroup.inc("sampleEvent_" + plan.metricKey());
      ctx.output(tdqEnv.getSampleOutputTag(), new TdqSampleData(
          event, metric, plan.metricKey(), plan.cxt().get().toString())
      );
    }
  }

  private void errorMsg(ReadOnlyContext ctx, RawEvent event, Exception e, PhysicalPlan plan) {
    metricGroup.inc("errorEvent_" + plan.metricKey());
    if ((System.currentTimeMillis() - errorMsgCurrentTimeMillis) > 5000) {
      log.warn("Drop event={},plan={},exception={}", event, plan, e);
      ctx.output(tdqEnv.getExceptionOutputTag(), new TdqErrorMsg(event, e, plan.metricKey()));
      errorMsgCurrentTimeMillis = System.currentTimeMillis();
    }
  }
}
