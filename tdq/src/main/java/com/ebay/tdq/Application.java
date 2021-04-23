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
import java.time.Duration;
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

import static com.ebay.tdq.utils.TdqConstant.OUTPUT_TAG_MAP;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_COLLECTOR_BY_WINDOW;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_NORMALIZER;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_PRE_COLLECTOR;
import static com.ebay.tdq.utils.TdqConstant.WINDOW_METRIC_COLLECTOR_BY_WINDOW;
import static com.ebay.tdq.utils.TdqConstant.WINDOW_METRIC_PRE_COLLECTOR;

public class Application {
  public void start(String[] args) throws Exception {
    // step0: prepare environment
    final StreamExecutionEnvironment env = FlinkEnvFactory.create(args, false);

    // step1: build data source
    DataStream<RawEvent> rawEventDataStream = BehaviorPathfinderSource.build(env);

    // step2: normalize event to metric
    SingleOutputStreamOperator<TdqMetric> normalizeOperator = normalizeEvent(env,
        rawEventDataStream);

    // step3: aggregate metric by key and window
    SingleOutputStreamOperator<TdqMetric> outputTagsOperator = reduceMetric(normalizeOperator);

    // step4: output metric by window
    outputMetricByWindow(outputTagsOperator);

    env.execute("Tdq Job [topic=behavior.pathfinder]");
  }

  // output metric by window
  protected void outputMetricByWindow(
      SingleOutputStreamOperator<TdqMetric> outputTagsOperator) {
    OUTPUT_TAG_MAP.forEach((seconds, tag) -> {
      String key = Duration.ofSeconds(seconds).toString();

      // TODO ONLY FOR DEBUG
      outputTagsOperator.getSideOutput(tag)
          .print("PRE OUT " + key)
          .uid("pre-" + key)
          .name("Pre " + key)
          .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR);

      outputTagsOperator
          .getSideOutput(tag)
          .keyBy(TdqMetric::getUid)
          .window(TumblingEventTimeWindows.of(Time.seconds(seconds)))
          .aggregate(new TdqAggregateFunction())
          .slotSharingGroup("metric-final-collector")
          .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR)
          .name("Metrics Final Collector Window[" + key + "]")
          .uid("tdq-final-metrics-collector-window-" + key)
          .print("FINAL OUT " + key)
          .uid("final-" + key)
          .name("Final " + key)
          .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR);
    });
  }

  // aggregate metric by key and window
  protected SingleOutputStreamOperator<TdqMetric> reduceMetric(
      SingleOutputStreamOperator<TdqMetric> normalizeOperator) {
    SingleOutputStreamOperator<TdqMetric> collectorOperator = normalizeOperator
        .keyBy("uid", "partition")
        .window(TumblingEventTimeWindows.of(Time.seconds(WINDOW_METRIC_PRE_COLLECTOR)))
        .aggregate(new TdqAggregateFunction())
        .setParallelism(PARALLELISM_METRIC_PRE_COLLECTOR)
        .slotSharingGroup("metric-pre-collector")
        .name("Metric Pre Collector")
        .uid("metric-pre-collector");

    return collectorOperator
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
  }

  // normalize event to metric
  protected SingleOutputStreamOperator<TdqMetric> normalizeEvent(
      StreamExecutionEnvironment env,
      DataStream<RawEvent> rawEventDataStream) {
    DataStream<PhysicalPlan> mappingSourceStream = TdqConfigSource.build(env);

    MapStateDescriptor<String, PhysicalPlan> stateDescriptor = new MapStateDescriptor<>(
        "tdqConfigMappingBroadcastState",
        BasicTypeInfo.STRING_TYPE_INFO,
        TypeInformation.of(new TypeHint<PhysicalPlan>() {
        }));

    BroadcastStream<PhysicalPlan> broadcastStream =
        mappingSourceStream.broadcast(stateDescriptor);

    return rawEventDataStream
        .connect(broadcastStream)
        .process(new TdqRawEventProcessFunction(
            stateDescriptor, PARALLELISM_METRIC_NORMALIZER)
        )
        .name("Connector Operator")
        .uid("connector-operator")
        .slotSharingGroup("metric-normalizer")
        .setParallelism(PARALLELISM_METRIC_NORMALIZER)
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(
                    (SerializableTimestampAssigner<TdqMetric>)
                        (event, timestamp) -> event.getEventTime())
                .withIdleness(Duration.ofSeconds(1))
        )
        .name("Connector Watermark Operator")
        .uid("connector-watermark-operator")
        .slotSharingGroup("metric-normalizer")
        .setParallelism(PARALLELISM_METRIC_NORMALIZER);
  }
}
