package com.ebay.tdq.functions;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.planner.LkpManager;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqErrorMsg;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.rules.TdqSampleData;
import com.ebay.tdq.utils.LocalCache;
import com.ebay.tdq.utils.TdqEnv;
import com.ebay.tdq.utils.TdqMetricGroup;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
@Slf4j
public class RawEventProcessFunction extends ProcessFunction<RawEvent, TdqMetric> implements CheckpointedFunction {

  private final TdqEnv tdqEnv;
  private transient long errorMsgCurrentTimeMillis;
  private transient TdqMetricGroup metricGroup;
  private transient ListState<TdqMetric> cacheState;
  private transient LocalCache localCache;

  public RawEventProcessFunction(TdqEnv tdqEnv) {
    this.tdqEnv = tdqEnv;
    if (tdqEnv.getLocalCombineFlushTimeout() > 60000) {
      throw new RuntimeException("flink.app.advance.local-combine.flush-timeout must less than 60s!");
    }
  }

  @Override
  public void processElement(RawEvent event, Context ctx, Collector<TdqMetric> collector) throws Exception {
    long s1 = System.nanoTime();
    processElement0(event, LkpManager.getInstance(tdqEnv.getJdbcEnv()).getPhysicalPlans(), ctx, collector);
    metricGroup.markElement(s1);
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
    metricGroup = new TdqMetricGroup(getRuntimeContext().getMetricGroup());
    localCache = new LocalCache(tdqEnv, metricGroup);
    metricGroup.gauge(localCache);
  }

  private void processElement0(RawEvent event,
      List<PhysicalPlan> physicalPlans, Context ctx, Collector<TdqMetric> collector) throws Exception {
    if (physicalPlans == null || physicalPlans.size() == 0) {
      throw new Exception("physical plans is empty!");
    }
    if (tdqEnv.getKafkaSourceEnv().isNotProcessElement(event.getUnixEventTimestamp())) {
      metricGroup.inc("isNotProcessElement");
      return;
    }
    for (PhysicalPlan plan : physicalPlans) {
      metricGroup.inc(plan.metricKey());
      if (plan.metricKey().equals("exception")) {
        throw new RuntimeException("this is debugging exception, checking checkpoint recovery.");
      }
      long s = System.nanoTime();
      try {
        TdqMetric metric = plan.process(event);
        sampleData(ctx, event, metric, plan);
        localCache.flush(plan, metric, collector);
        metricGroup.updateEventHistogram(s);
      } catch (Exception e) {
        errorMsg(ctx, event, e, plan);
      }
    }
  }

  private void sampleData(Context ctx, RawEvent event, TdqMetric metric, PhysicalPlan plan) {
    if (plan.sampling()) {
      metricGroup.inc("sampleEvent_" + plan.metricKey());
      ctx.output(tdqEnv.getSampleOutputTag(), new TdqSampleData(
          event, metric, plan.metricKey(), plan.cxt().get().toString())
      );
    }
  }

  private void errorMsg(Context ctx, RawEvent event, Exception e, PhysicalPlan plan) {
    metricGroup.inc("errorEvent");
    metricGroup.inc("errorEvent_" + plan.metricKey());
    if ((System.currentTimeMillis() - errorMsgCurrentTimeMillis) > 5000) {
      log.warn("Drop event={},plan={},exception={}", event, plan, e);
      ctx.output(tdqEnv.getExceptionOutputTag(), new TdqErrorMsg(event, e, plan.metricKey()));
      errorMsgCurrentTimeMillis = System.currentTimeMillis();
    }
  }

}
