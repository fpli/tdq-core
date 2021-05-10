package com.ebay.tdq;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.functions.TdqAggregateFunction;
import com.ebay.tdq.functions.TdqMetricProcessWindowTagFunction;
import com.ebay.tdq.functions.TdqRawEventProcessFunction;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.sinks.ProntoSink;
import com.ebay.tdq.sources.BehaviorPathfinderSource;
import com.ebay.tdq.sources.TdqConfigSource;
import com.ebay.tdq.utils.FlinkEnvFactory;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import static com.ebay.tdq.utils.TdqConstant.OUTPUT_TAG_MAP;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_COLLECTOR_BY_WINDOW;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_INDEX_PATTERN;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_LATENCY_INDEX_PATTERN;
import static com.ebay.tdq.utils.TdqConstant.SINK_TYPES;
import static com.ebay.tdq.utils.TdqConstant.WINDOW_METRIC_COLLECTOR_BY_WINDOW;

public class ProfilingJob {
  OutputTag<TdqMetric> lateDataTag = new OutputTag<TdqMetric>("late") {
  };

  public void start(String[] args) throws Exception {
    // step0: prepare environment
    final StreamExecutionEnvironment env = FlinkEnvFactory.create(args, false);

    // step1: build data source
    List<DataStream<RawEvent>> rawEventDataStream = BehaviorPathfinderSource.build(env);

    // step2: normalize event to metric
    DataStream<TdqMetric> normalizeOperator = normalizeEvent(env, rawEventDataStream);

    // step3: aggregate metric by key and window
    Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags = reduceMetric(normalizeOperator);

    // step4: output metric by window
    outputMetricByWindow(outputTags);

    env.execute("Tdq Job [topic=behavior.pathfinder]");
  }

  // output metric by window
  protected void outputMetricByWindow(Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags) {
    outputTags.forEach((key, ds) -> {
      output(ds, "final_" + key, false);
    });
  }

  private void output(DataStream<TdqMetric> ds, String uid, boolean isLateData) {
    if (SINK_TYPES.contains("console")) {
      ds.print(uid.toUpperCase())
          .uid(uid)
          .name(uid)
          .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR);
    }
    if (SINK_TYPES.contains("pronto")) {
      if (isLateData) {
        ProntoSink.output(ds, uid, PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR, PRONTO_LATENCY_INDEX_PATTERN);
      } else {
        ProntoSink.output(ds, uid, PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR, PRONTO_INDEX_PATTERN);
      }
    }
  }

  // aggregate metric by key and window
  protected Map<String, SingleOutputStreamOperator<TdqMetric>> reduceMetric(DataStream<TdqMetric> normalizeOperator) {
    SingleOutputStreamOperator<TdqMetric> unifyDataStream = normalizeOperator
        .keyBy(TdqMetric::getUid)
        .window(TumblingEventTimeWindows.of(Time.seconds(WINDOW_METRIC_COLLECTOR_BY_WINDOW)))
        //.allowedLateness(Time.minutes(OUT_OF_ORDERLESS_IN_MIN))
        .sideOutputLateData(lateDataTag)
        .aggregate(new TdqAggregateFunction(), new TdqMetricProcessWindowTagFunction(OUTPUT_TAG_MAP))
        .setParallelism(PARALLELISM_METRIC_COLLECTOR_BY_WINDOW)
        .slotSharingGroup("metric-collector-by-window")
        .name("Metric Split by Window Collector")
        .uid("metric_split_by_window_collector");

    output(unifyDataStream.getSideOutput(lateDataTag), "collector_by_window_late", true);

    Map<String, SingleOutputStreamOperator<TdqMetric>> ans = Maps.newHashMap();
    OUTPUT_TAG_MAP.forEach((seconds, tag) -> {
      String key = Duration.ofSeconds(seconds).toString();
      ans.put(
          key,
          unifyDataStream
              .getSideOutput(tag)
              .keyBy(TdqMetric::getUid)
              .window(TumblingEventTimeWindows.of(Time.seconds(seconds)))
              .aggregate(new TdqAggregateFunction())
              .slotSharingGroup("metric-final-collector")
              .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR)
              .name("Metrics Final Collector Window[" + key + "]")
              .uid("tdq-final-metrics-collector-window-" + key)
      );
    });
    return ans;
  }

  protected DataStream<PhysicalPlan> getConfigDS(StreamExecutionEnvironment env) {
    return TdqConfigSource.build(env);
  }

  // normalize event to metric
  protected DataStream<TdqMetric> normalizeEvent(
      StreamExecutionEnvironment env,
      List<DataStream<RawEvent>> rawEventDataStream) {

    MapStateDescriptor<String, PhysicalPlan> stateDescriptor = new MapStateDescriptor<>(
        "tdqConfigMappingBroadcastState",
        BasicTypeInfo.STRING_TYPE_INFO,
        TypeInformation.of(new TypeHint<PhysicalPlan>() {
        }));

    BroadcastStream<PhysicalPlan> broadcastStream =
        getConfigDS(env).broadcast(stateDescriptor);

    DataStream<TdqMetric> ans = normalizeEvent(
        rawEventDataStream.get(0),
        stateDescriptor,
        broadcastStream,
        0);


    for (int i = 1; i < rawEventDataStream.size(); i++) {
      ans = ans.union(normalizeEvent(
          rawEventDataStream.get(i),
          stateDescriptor,
          broadcastStream,
          i));
    }
    return ans;
  }

  private DataStream<TdqMetric> normalizeEvent(
      DataStream<RawEvent> rawEventDataStream,
      MapStateDescriptor<String, PhysicalPlan> stateDescriptor,
      BroadcastStream<PhysicalPlan> broadcastStream,
      int idx) {
    String slotSharingGroup = rawEventDataStream.getTransformation().getSlotSharingGroup();
    int parallelism = rawEventDataStream.getTransformation().getParallelism();

    return rawEventDataStream.connect(broadcastStream)
        .process(new TdqRawEventProcessFunction(stateDescriptor))
        .name("Connector" + idx + " Operator")
        .uid("connector" + idx + "-operator")
        .slotSharingGroup(slotSharingGroup)
        .setParallelism(parallelism)
        ;
  }
}
