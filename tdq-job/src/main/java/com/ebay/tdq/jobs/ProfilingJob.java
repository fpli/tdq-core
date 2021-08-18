package com.ebay.tdq.jobs;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.functions.TdqMetric1stAggrProcessWindowFunction;
import com.ebay.tdq.functions.TdqMetric2ndAggrProcessWindowFunction;
import com.ebay.tdq.functions.TdqMetricAggregateFunction;
import com.ebay.tdq.sinks.SinkFactory;
import com.ebay.tdq.sources.SourceFactory;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.TdqContext;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * --tdq-profile test|pre-prod|prod
 */
@Slf4j
@Getter
@Setter
public class ProfilingJob {

  protected TdqContext tdqCxt;
  protected TdqEnv tdqEnv;

  public static void main(String[] args) {
    new ProfilingJob().submit(args);
  }

  protected void setup(String[] args) throws Exception {
    // step0: prepare environment
    tdqCxt = new TdqContext(args);
    tdqEnv = tdqCxt.getTdqEnv();
  }

  protected void start() throws Exception {
    // step1: build data source
    // step2: normalize event to metric
    DataStream<InternalMetric> ds = SourceFactory.build(tdqCxt);

    // step3: aggregate metric by key and window
    Map<String, SingleOutputStreamOperator<InternalMetric>> outputTags = reduceByWindow(ds);

    // step4: output metric by window
    outputByWindow(outputTags);

    tdqCxt.getRhsEnv().execute(tdqEnv.getJobName());
  }

  protected void stop() {
  }

  // output metric by window
  protected void outputByWindow(Map<String, SingleOutputStreamOperator<InternalMetric>> outputTags) {
    outputTags.forEach((key, ds) -> {
      SinkFactory.sinkNormalMetric(key + "_o", tdqCxt, ds);
    });
  }

  // aggregate metric by key and window
  protected Map<String, SingleOutputStreamOperator<InternalMetric>> reduceByWindow(
      DataStream<InternalMetric> normalizeOperator) {
    String uid = "1st_aggr_w_" + tdqEnv.getMetric1stAggrWindow();
    SingleOutputStreamOperator<InternalMetric> unifyDataStream = normalizeOperator
        .keyBy((KeySelector<InternalMetric, String>) m -> m.getPartition() + "#" + m.getMetricId())
        .window(TumblingEventTimeWindows.of(Time.seconds(DateUtils.toSeconds(tdqEnv.getMetric1stAggrWindow()))))
        .sideOutputLateData(tdqCxt.getEventLatencyOutputTag())
        .aggregate(new TdqMetricAggregateFunction(),
            new TdqMetric1stAggrProcessWindowFunction(tdqCxt.getOutputTagMap()))
        .setParallelism(tdqEnv.getMetric1stAggrParallelism())
        .slotSharingGroup("metric-1st-aggr")
        .name(uid)
        .uid(uid);

    SinkFactory.sinkLatencyMetric(tdqCxt, unifyDataStream);

    Map<String, SingleOutputStreamOperator<InternalMetric>> ans = Maps.newHashMap();
    tdqCxt.getOutputTagMap().forEach((seconds, tag) -> {
      String key = Duration.ofSeconds(seconds).toString().toLowerCase();
      String tagUid = "aggr_w_" + key;
      SingleOutputStreamOperator<InternalMetric> ds = unifyDataStream
          .getSideOutput(tag)
          .keyBy(InternalMetric::getMetricId)
          .window(TumblingEventTimeWindows.of(Time.seconds(seconds)))
          .aggregate(new TdqMetricAggregateFunction(), new TdqMetric2ndAggrProcessWindowFunction())
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism())
          .name(tagUid)
          .uid(tagUid);
      ans.put(key, ds);
    });
    return ans;
  }

  public void submit(String[] args) {
    try {
      setup(args);
      start();
      stop();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
