package com.ebay.tdq.jobs;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getStringOrDefault;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.functions.TdqMetric1stAggrProcessWindowFunction;
import com.ebay.tdq.functions.TdqMetric2ndAggrProcessWindowFunction;
import com.ebay.tdq.functions.TdqMetricAggregateFunction;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.sinks.TdqSinks;
import com.ebay.tdq.sources.BehaviorPathfinderSource;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.FlinkEnvFactory;
import com.ebay.tdq.utils.TdqContext;
import com.ebay.tdq.common.env.TdqEnv;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.TdqTimestampsAndWatermarksOperator;

/**
 * --tdq-profile tdq-pre-prod|tdq-prod [default is test]
 */
@Slf4j
@Getter
@Setter
public class ProfilingJob {

  protected TdqEnv tdqEnv;
  protected TdqContext tdqCxt;
  protected transient StreamExecutionEnvironment env;

  protected void setup(String[] args) {
    // step0: prepare environment
    tdqCxt = new TdqContext(args);
    tdqEnv = tdqCxt.getTdqEnv();
    env = FlinkEnvFactory.create(tdqEnv);
  }

  protected void start() {
    try {
      // step1: build data source
      List<DataStream<RawEvent>> rawEventDataStream = new BehaviorPathfinderSource(tdqEnv, env).build();

      // step2: normalize event to metric
      DataStream<TdqMetric> normalizeOperator = normalizeMetric(rawEventDataStream);

      // step3: aggregate metric by key and window
      Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags = reduceMetric(normalizeOperator);

      // step4: output metric by window
      outputMetricByWindow(outputTags);

      env.execute(tdqEnv.getJobName());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  protected void stop() {
  }

  // output metric by window
  protected void outputMetricByWindow(Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags) {
    outputTags.forEach((key, ds) -> {
      TdqSinks.sinkNormalMetric(key + "_o", tdqCxt, ds);
    });
  }

  // aggregate metric by key and window
  protected Map<String, SingleOutputStreamOperator<TdqMetric>> reduceMetric(DataStream<TdqMetric> normalizeOperator) {
    String uid = "1st_aggr_w_" + tdqEnv.getMetric1stAggrW();
    SingleOutputStreamOperator<TdqMetric> unifyDataStream = normalizeOperator
        .keyBy((KeySelector<TdqMetric, String>) m -> m.getPartition() + "#" + m.getTagId())
        .window(TumblingEventTimeWindows.of(Time.seconds(tdqEnv.getMetric1stAggrWMilli())))
        .sideOutputLateData(tdqCxt.getEventLatencyOutputTag())
        .aggregate(new TdqMetricAggregateFunction(),
            new TdqMetric1stAggrProcessWindowFunction(tdqCxt.getOutputTagMap()))
        .setParallelism(tdqEnv.getMetric1stAggrParallelism())
        .slotSharingGroup("metric-1st-aggr")
        .name(uid)
        .uid(uid);

    TdqSinks.sinkException(tdqCxt, unifyDataStream);
    TdqSinks.sinkSampleLog(tdqCxt, unifyDataStream);
    TdqSinks.sinkLatencyMetric(tdqCxt, unifyDataStream);

    Map<String, SingleOutputStreamOperator<TdqMetric>> ans = Maps.newHashMap();
    tdqCxt.getOutputTagMap().forEach((seconds, tag) -> {
      String key = Duration.ofSeconds(seconds).toString().toLowerCase();
      String tagUid = "aggr_w_" + key;
      SingleOutputStreamOperator<TdqMetric> ds = unifyDataStream.getSideOutput(tag).keyBy(TdqMetric::getTagId)
          .window(TumblingEventTimeWindows.of(Time.seconds(seconds)))
          .aggregate(new TdqMetricAggregateFunction(), new TdqMetric2ndAggrProcessWindowFunction())
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism())
          .name(tagUid)
          .uid(tagUid);
      ans.put(key, ds);
    });
    return ans;
  }

  // normalize event to metric
  protected DataStream<TdqMetric> normalizeMetric(List<DataStream<RawEvent>> rawEventDataStream) {

    DataStream<TdqMetric> ans = normalizeMetric(rawEventDataStream.get(0), 0);

    for (int i = 1; i < rawEventDataStream.size(); i++) {
      ans = ans.union(normalizeMetric(rawEventDataStream.get(i), i));
    }
    return ans;
  }

  private DataStream<TdqMetric> normalizeMetric(DataStream<RawEvent> rawEventDataStream, int idx) {
    String slotSharingGroup = rawEventDataStream.getTransformation().getSlotSharingGroup();
    int parallelism = rawEventDataStream.getTransformation().getParallelism();
    String uid = "normalize_evt_" + idx;
    SingleOutputStreamOperator<TdqMetric> ds = rawEventDataStream
        .process(new RawEventProcessFunction(tdqCxt))
        .name(uid)
        .uid(uid)
        .slotSharingGroup(slotSharingGroup)
        .setParallelism(parallelism);

    long orderless = DateUtils.toSeconds(getStringOrDefault("flink.app.advance.watermark.out-of-orderless", "3min"));
    long timeout = DateUtils.toSeconds(getStringOrDefault("flink.app.advance.watermark.idle-source-timeout", "10min"));
    SerializableTimestampAssigner<TdqMetric> assigner =
        (SerializableTimestampAssigner<TdqMetric>) (event, timestamp) -> event.getEventTime();

    WatermarkStrategy<TdqMetric> watermarkStrategy = WatermarkStrategy
        .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(orderless))
        .withTimestampAssigner(assigner)
        .withIdleness(Duration.ofSeconds(timeout));
    TdqTimestampsAndWatermarksOperator<TdqMetric> operator =
        new TdqTimestampsAndWatermarksOperator<>(env.clean(watermarkStrategy));

    ds = ds.transform("Timestamps/Watermarks", ds.getTransformation().getOutputType(), operator)
        .slotSharingGroup(slotSharingGroup)
        .name(uid + "_wks")
        .uid(uid + "_wks")
        .slotSharingGroup(slotSharingGroup)
        .setParallelism(parallelism);

    //    ds = ds.assignTimestampsAndWatermarks(watermarkStrategy)
    //        .name(uid + "_wks")
    //        .uid(uid + "_wks")
    //        .slotSharingGroup(slotSharingGroup)
    //        .setParallelism(parallelism);
    return ds;
  }


  public void submit(String[] args) {
    setup(args);
    start();
    stop();
  }

  public static void main(String[] args) {
    new ProfilingJob().submit(args);
  }
}
