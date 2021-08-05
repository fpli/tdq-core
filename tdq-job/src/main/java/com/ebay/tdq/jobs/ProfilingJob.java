package com.ebay.tdq.jobs;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.functions.TdqMetric1stAggrProcessWindowFunction;
import com.ebay.tdq.functions.TdqMetric2ndAggrProcessWindowFunction;
import com.ebay.tdq.functions.TdqMetricAggregateFunction;
import com.ebay.tdq.planner.utils.ConfigService;
import com.ebay.tdq.sinks.TdqSinks;
import com.ebay.tdq.sources.SourceFactory;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.TdqConfigManager;
import com.ebay.tdq.utils.TdqContext;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * --tdq-profile tdq-pre-prod|tdq-prod [default is test]
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

  protected void setup(String[] args) {
    // step0: prepare environment
    tdqCxt = new TdqContext(args);
    tdqEnv = tdqCxt.getTdqEnv();
  }

  protected void start() {
    try {
      ConfigService.register(tdqEnv);

      TdqConfig tdqConfig = TdqConfigManager.getTdqConfig(tdqEnv);
      Validate.isTrue(tdqConfig != null);
      // step1: build data source
      // step2: normalize event to metric
      DataStream<TdqMetric> ds = SourceFactory.build(tdqConfig, tdqCxt);

      // step3: aggregate metric by key and window
      Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags = reduceByWindow(ds);

      // step4: output metric by window
      outputByWindow(outputTags);

      tdqCxt.getRhsEnv().execute(tdqEnv.getJobName());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  protected void stop() {
  }

  // output metric by window
  protected void outputByWindow(Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags) {
    outputTags.forEach((key, ds) -> {
      TdqSinks.sinkNormalMetric(key + "_o", tdqCxt, ds);
    });
  }

  // aggregate metric by key and window
  protected Map<String, SingleOutputStreamOperator<TdqMetric>> reduceByWindow(DataStream<TdqMetric> normalizeOperator) {
    String uid = "1st_aggr_w_" + tdqEnv.getMetric1stAggrWindow();
    SingleOutputStreamOperator<TdqMetric> unifyDataStream = normalizeOperator
        .keyBy((KeySelector<TdqMetric, String>) m -> m.getPartition() + "#" + m.getTagId())
        .window(TumblingEventTimeWindows.of(Time.seconds(DateUtils.toSeconds(tdqEnv.getMetric1stAggrWindow()))))
        .sideOutputLateData(tdqCxt.getEventLatencyOutputTag())
        .aggregate(new TdqMetricAggregateFunction(),
            new TdqMetric1stAggrProcessWindowFunction(tdqCxt.getOutputTagMap()))
        .setParallelism(tdqEnv.getMetric1stAggrParallelism())
        .slotSharingGroup("metric-1st-aggr")
        .name(uid)
        .uid(uid);

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

  public void submit(String[] args) {
    setup(args);
    start();
    stop();
  }
}
