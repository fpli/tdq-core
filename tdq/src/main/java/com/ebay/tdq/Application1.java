package com.ebay.tdq;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.functions.TdqAggregateFunction;
import com.ebay.tdq.functions.TdqMetricMapFunction;
import com.ebay.tdq.functions.TdqMetricProcessWindowTagFunction;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.sources.BehaviorPathfinderSource;
import com.ebay.tdq.utils.FlinkEnvFactory;
import java.time.Duration;
import java.util.List;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.jetbrains.annotations.NotNull;

import static com.ebay.tdq.utils.TdqConstant.OUTPUT_TAG_MAP;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_COLLECTOR_BY_WINDOW;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_PRE_COLLECTOR;
import static com.ebay.tdq.utils.TdqConstant.WINDOW_METRIC_COLLECTOR_BY_WINDOW;
import static com.ebay.tdq.utils.TdqConstant.WINDOW_METRIC_PRE_COLLECTOR;

public class Application1 {
  public void start(String[] args) throws Exception {
    // step0: prepare environment
    final StreamExecutionEnvironment env = FlinkEnvFactory.create(args, false);

    // step1: build data source
    List<DataStream<RawEvent>> rawEventDataStream = BehaviorPathfinderSource.build(env);

    // step2: normalize event to metric
    DataStream<TdqMetric> normalizeOperator = normalizeEvent(env,
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
      // outputTagsOperator.getSideOutput(tag)
      //     .print("PRE OUT " + key)
      //     .uid("pre-" + key)
      //     .name("Pre " + key)
      //     .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR);

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
      DataStream<TdqMetric> normalizeOperator) {
    DataStream<TdqMetric> collectorOperator = normalizeOperator
        .keyBy("partition", "uid")
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
  protected DataStream<TdqMetric> normalizeEvent(
      StreamExecutionEnvironment env,
      List<DataStream<RawEvent>> rawEventDataStream) {

    DataStream<TdqMetric> ans = normalizeEvent(rawEventDataStream.get(0), 0);

    for (int i = 1; i < rawEventDataStream.size(); i++) {
      ans = ans.union(normalizeEvent(rawEventDataStream.get(i), i));
    }
    return ans;
  }

  @NotNull
  private DataStream<TdqMetric> normalizeEvent(
      DataStream<RawEvent> rawEventDataStream,
      int idx) {
    String slotSharingGroup = rawEventDataStream.getTransformation().getSlotSharingGroup();
    int parallelism = rawEventDataStream.getTransformation().getParallelism();

    // return rawEventDataStream.connect(broadcastStream)
    //     .process(new TdqRawEventProcessFunction(stateDescriptor))
    //     .name("Connector" + idx + " Operator")
    //     .uid("connector" + idx + "-operator")
    //     .slotSharingGroup(slotSharingGroup)
    //     .setParallelism(parallelism)

    return rawEventDataStream
        .flatMap(new TdqMetricMapFunction(parallelism, 3000))
        .name("Connector" + idx + " Operator")
        .uid("connector" + idx + "-operator")
        .setParallelism(parallelism)
        .slotSharingGroup(slotSharingGroup)
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

        //.keyBy(TdqMetric::getUid)
        //.window(TumblingEventTimeWindows.of(Time.seconds(WINDOW_METRIC_PRE_COLLECTOR)))
        //.aggregate(new TdqAggregateFunction())
        //.setParallelism(PARALLELISM_METRIC_PRE_COLLECTOR)
        //.slotSharingGroup("metric" + idx + "-pre-collector")
        //.name("Metric" + idx + " Pre Collector")
        //.uid("metric" + idx + "-pre-collector")

        ;
  }
}
