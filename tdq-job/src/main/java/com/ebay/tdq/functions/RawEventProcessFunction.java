package com.ebay.tdq.functions;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.planner.LkpManager;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.utils.LocalCache;
import com.ebay.tdq.utils.TdqConfigManager;
import com.ebay.tdq.utils.TdqContext;
import com.ebay.tdq.utils.TdqMetricGroup;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author juntzhang
 */
@Slf4j
public class RawEventProcessFunction extends ProcessFunction<TdqEvent, InternalMetric> implements CheckpointedFunction {

  private final TdqEnv tdqEnv;
  private final TdqContext tdqCxt;
  private transient long errorMsgCurrentTimeMillis;
  private transient TdqMetricGroup metricGroup;
  private transient ListState<InternalMetric> cacheState;
  private transient LocalCache localCache;

  public RawEventProcessFunction(TdqContext tdqCxt) {
    this.tdqCxt = tdqCxt;
    this.tdqEnv = tdqCxt.getTdqEnv();
    if (tdqEnv.getLocalCombineFlushTimeout() > 60000) {
      throw new RuntimeException("flink.app.advance.local-combine.flush-timeout must less than 60s!");
    }
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    TdqConfigManager.getInstance(tdqEnv).start();
    LkpManager.getInstance(tdqEnv).start();
  }

  @Override
  public void close() throws Exception {
    TdqConfigManager.getInstance(tdqEnv).stop();
    LkpManager.getInstance(tdqEnv).stop();
  }

  @Override
  public void processElement(TdqEvent event, Context ctx, Collector<InternalMetric> collector) throws Exception {
    long s1 = System.nanoTime();
    processElement0(event, TdqConfigManager.getInstance(tdqEnv).getPhysicalPlans(), ctx, collector);
    metricGroup.markElement(s1);
  }

  @Override
  public void snapshotState(FunctionSnapshotContext context) throws Exception {
    cacheState.clear();
    for (InternalMetric m : localCache.values()) {
      cacheState.add(m);
    }
    metricGroup.inc("snapshotState");
  }

  @Override
  public void initializeState(FunctionInitializationContext context) throws Exception {
    final TypeInformation<InternalMetric> ti = TypeInformation.of(new TypeHint<InternalMetric>() {
    });
    cacheState = context.getOperatorStateStore().getListState(new ListStateDescriptor<>("cacheState", ti));
    initialize();
    if (context.isRestored()) {
      metricGroup.inc("restored");
      cacheState.get().forEach(v -> localCache.put(v.getMetricIdWithEventTime(), v));
    }
  }

  private void initialize() {
    errorMsgCurrentTimeMillis = 0L;
    metricGroup = new TdqMetricGroup(getRuntimeContext().getMetricGroup());
    localCache = new LocalCache(tdqEnv, metricGroup);
    metricGroup.gauge(localCache);
    TdqConfigManager.getInstance(tdqEnv);
  }

  private void processElement0(TdqEvent event,
      List<PhysicalPlan> physicalPlans, Context ctx, Collector<InternalMetric> collector) throws Exception {
    if (physicalPlans == null || physicalPlans.size() == 0) {
      throw new Exception("physical plans is empty!");
    }
    if (tdqEnv.isNotProcessElement(event.getEventTimeMs())) {
      metricGroup.inc("isNotProcessElement");
      return;
    }
    for (PhysicalPlan plan : physicalPlans) {
      metricGroup.inc(plan.metricName());
      if (plan.metricName().equals("exception")) {
        throw new RuntimeException("this is debugging exception, checking checkpoint recovery.");
      }
      long s = System.nanoTime();
      try {
        InternalMetric metric = plan.process(event);
        sampleData(ctx, event, metric, plan);
        localCache.flush(plan, metric, collector);
        metricGroup.updateEventHistogram(s);
      } catch (Exception e) {
        errorMsg(ctx, event, e, plan);
      }
    }
  }

  private void sampleData(Context ctx, TdqEvent event, InternalMetric metric, PhysicalPlan plan) {
    if (plan.sampling()) {
      metricGroup.inc("sampleEvent_" + plan.metricName());
      ctx.output(tdqCxt.getSampleOutputTag(), new TdqSampleData(
          event, metric, plan.metricName(), plan.cxt().get().toString())
      );
    }
  }

  private void errorMsg(Context ctx, TdqEvent event, Exception e, PhysicalPlan plan) {
    metricGroup.inc("errorEvent");
    metricGroup.inc("errorEvent_" + plan.metricName());
    if ((System.currentTimeMillis() - errorMsgCurrentTimeMillis) > 5000) {
      log.warn("Drop event={},plan={},exception={}", event, plan, e);
      ctx.output(tdqCxt.getExceptionOutputTag(), new TdqErrorMsg(event, e, plan.metricName()));
      errorMsgCurrentTimeMillis = System.currentTimeMillis();
    }
  }

}
