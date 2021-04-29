package com.ebay.tdq;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.functions.TdqAggregateFunction;
import com.ebay.tdq.functions.TdqMetricProcessWindowTagFunction;
import com.ebay.tdq.functions.TdqRawEventProcessFunction;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.sources.BehaviorPathfinderSource;
import com.ebay.tdq.sources.TdqConfigSource;
import com.ebay.tdq.utils.FlinkEnvFactory;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
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

import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_QUEUE_SIZE;
import static com.ebay.tdq.utils.TdqConstant.OUTPUT_TAG_MAP;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_COLLECTOR_BY_WINDOW;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR;
import static com.ebay.tdq.utils.TdqConstant.WINDOW_METRIC_COLLECTOR_BY_WINDOW;

public class Application {
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
      ds.print("FINAL OUT " + key)
          .uid("final-" + key)
          .name("Final " + key)
          .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR);
    });
  }

  // aggregate metric by key and window
  protected Map<String, SingleOutputStreamOperator<TdqMetric>> reduceMetric(
      DataStream<TdqMetric> normalizeOperator) {
    SingleOutputStreamOperator<TdqMetric> unifyDataStream = normalizeOperator
        .keyBy(TdqMetric::getUid)
        .window(TumblingEventTimeWindows.of(
            Time.seconds(WINDOW_METRIC_COLLECTOR_BY_WINDOW)
        ))
        .aggregate(new TdqAggregateFunction(),
            new TdqMetricProcessWindowTagFunction(OUTPUT_TAG_MAP))
        .setParallelism(PARALLELISM_METRIC_COLLECTOR_BY_WINDOW)
        .slotSharingGroup("metric-collector-by-window")
        .name("Metric Split by Window Collector")
        .uid("metric-split-by-window-collector");

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
        .process(new TdqRawEventProcessFunction(stateDescriptor, LOCAL_COMBINE_QUEUE_SIZE))
        .name("Connector" + idx + " Operator")
        .uid("connector" + idx + "-operator")
        .slotSharingGroup(slotSharingGroup)
        .setParallelism(parallelism)
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(
                    (SerializableTimestampAssigner<TdqMetric>) (event, timestamp) -> event.getEventTime())
                .withIdleness(Duration.ofSeconds(1))
        )
        .name("Connector" + idx + " Watermark Operator")
        .uid("connector" + idx + "-watermark-operator")
        .slotSharingGroup(slotSharingGroup)
        .setParallelism(parallelism)
        ;
  }
}
