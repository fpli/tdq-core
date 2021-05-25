package com.ebay.tdq;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.tdq.functions.TdqAggregateFunction;
import com.ebay.tdq.functions.TdqMetricProcessWindowTagFunction;
import com.ebay.tdq.functions.TdqRawEventProcessFunction;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.sinks.ProntoSink;
import com.ebay.tdq.sources.BehaviorPathfinderSource;
import com.ebay.tdq.sources.TdqConfigSource;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.FlinkEnvFactory;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getStringOrDefault;
import static com.ebay.tdq.utils.TdqConstant.METRIC_1ST_AGGR_PARALLELISM;
import static com.ebay.tdq.utils.TdqConstant.METRIC_1ST_AGGR_W;
import static com.ebay.tdq.utils.TdqConstant.METRIC_1ST_AGGR_W_MILLI;
import static com.ebay.tdq.utils.TdqConstant.METRIC_2ND_AGGR_PARALLELISM;
import static com.ebay.tdq.utils.TdqConstant.OUTPUT_TAG_MAP;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_INDEX_PATTERN;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_LATENCY_INDEX_PATTERN;
import static com.ebay.tdq.utils.TdqConstant.SINK_TYPES;

/**
 * --tdq-profile tdq-pre-prod|tdq-prod [default is test]
 */
@Slf4j
public class ProfilingJob {
  public void start(String[] args) {
    try {
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

      env.execute(getString(Property.FLINK_APP_NAME));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  // output metric by window
  protected void outputMetricByWindow(Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags) {
    outputTags.forEach((key, ds) -> {
      output(ds, key + "_o", false);
    });
  }

  private void output(DataStream<TdqMetric> ds, String id, boolean isLateData) {
    if (SINK_TYPES.contains("console")) {
      String uid = id + "_std";
      ds.print(uid.toUpperCase())
          .uid(uid)
          .name(uid)
          .setParallelism(METRIC_2ND_AGGR_PARALLELISM);
    }
    if (SINK_TYPES.contains("pronto")) {
      String uid = id + "_pronto";
      if (isLateData) {
        new ProntoSink(uid, METRIC_2ND_AGGR_PARALLELISM, PRONTO_LATENCY_INDEX_PATTERN, 10)
            .output(ds);
      } else {
        new ProntoSink(uid, METRIC_2ND_AGGR_PARALLELISM, PRONTO_INDEX_PATTERN).output(ds);
      }
    }
  }

  // aggregate metric by key and window
  protected Map<String, SingleOutputStreamOperator<TdqMetric>> reduceMetric(DataStream<TdqMetric> normalizeOperator) {
    String uid = "1st_aggr_w_" + METRIC_1ST_AGGR_W;
    OutputTag<TdqMetric> lateDataTag = new OutputTag<TdqMetric>("1st_aggr_w_latency") {
    };
    SingleOutputStreamOperator<TdqMetric> unifyDataStream = normalizeOperator
        .keyBy(TdqMetric::getTagId)
        .window(TumblingEventTimeWindows.of(Time.seconds(METRIC_1ST_AGGR_W_MILLI)))
        .sideOutputLateData(lateDataTag)
        .aggregate(new TdqAggregateFunction(), new TdqMetricProcessWindowTagFunction(OUTPUT_TAG_MAP))
        .setParallelism(METRIC_1ST_AGGR_PARALLELISM)
        .slotSharingGroup("metric-1st-aggr")
        .name(uid)
        .uid(uid);

    output(unifyDataStream.getSideOutput(lateDataTag), "1st_aggr_w_latency_o", true);

    Map<String, SingleOutputStreamOperator<TdqMetric>> ans = Maps.newHashMap();
    OUTPUT_TAG_MAP.forEach((seconds, tag) -> {
      String key = Duration.ofSeconds(seconds).toString().toLowerCase();
      String tagUid = "2nd_aggr_w_" + key;
      String tagLatencyUid = "2nd_aggr_w_" + key + "_latency";
      OutputTag<TdqMetric> latency = new OutputTag<TdqMetric>(tagUid + "_latency") {
      };
      SingleOutputStreamOperator<TdqMetric> ds = unifyDataStream.getSideOutput(tag).keyBy(TdqMetric::getTagId)
          .window(TumblingEventTimeWindows.of(Time.seconds(seconds)))
          .sideOutputLateData(latency)
          .aggregate(new TdqAggregateFunction(), new ProcessWindowFunction<TdqMetric, TdqMetric, String,
              TimeWindow>() {
            private final Map<String, Counter> counterMap = new HashMap<>();
            private MetricGroup group;

            @Override
            public void open(Configuration parameters) throws Exception {
              group = this.getRuntimeContext().getMetricGroup().addGroup("tdq2");
              super.open(parameters);
            }

            public void inc(String key, long v) {
              Counter counter = counterMap.get(key);
              if (counter == null) {
                counter = group.counter(key);
                counterMap.put(key, counter);
              }
              counter.inc(v);
            }

            @Override
            public void process(String s, Context context, Iterable<TdqMetric> elements,
                Collector<TdqMetric> out) {
              elements.forEach(metric -> {
                if (metric.getExprMap() != null && metric.getExprMap().get("p1") != null) {
                  inc(DateUtils.getMinBuckets(metric.getEventTime(), 5) + "_" + metric.getMetricKey(),
                      (long) (double) metric.getExprMap().get("p1"));
                }
                out.collect(metric);
              });
            }
          })
          .setParallelism(METRIC_2ND_AGGR_PARALLELISM)
          .name(tagUid)
          .uid(tagUid);

      ds.getSideOutput(latency).addSink(new RichSinkFunction<TdqMetric>() {
        private MetricGroup group;
        private final Map<String, Counter> counterMap = new HashMap<>();

        @Override
        public void open(Configuration parameters) throws Exception {
          super.open(parameters);
          group = this.getRuntimeContext().getMetricGroup().addGroup("tdq2");
        }

        public void inc(String key, long v) {
          Counter counter = counterMap.get(key);
          if (counter == null) {
            counter = group.counter(key);
            counterMap.put(key, counter);
          }
          counter.inc(v);
        }

        @Override
        public void invoke(TdqMetric metric, Context context) {
          inc("tag_latency", 1);
        }
      })
          .setParallelism(METRIC_2ND_AGGR_PARALLELISM)
          .uid(tagLatencyUid)
          .name(tagLatencyUid);
      ans.put(key, ds);
    });
    return ans;
  }

  protected DataStream<PhysicalPlans> getConfigDS(StreamExecutionEnvironment env) {
    return TdqConfigSource.build(env);
  }

  // normalize event to metric
  protected DataStream<TdqMetric> normalizeEvent(
      StreamExecutionEnvironment env,
      List<DataStream<RawEvent>> rawEventDataStream) {

    MapStateDescriptor<String, PhysicalPlans> stateDescriptor = new MapStateDescriptor<>(
        "tdqConfigMappingBroadcastState",
        BasicTypeInfo.STRING_TYPE_INFO,
        TypeInformation.of(new TypeHint<PhysicalPlans>() {
        }));

    BroadcastStream<PhysicalPlans> broadcastStream = getConfigDS(env).broadcast(stateDescriptor);

    DataStream<TdqMetric> ans = normalizeEvent(rawEventDataStream.get(0), stateDescriptor, broadcastStream, 0);


    for (int i = 1; i < rawEventDataStream.size(); i++) {
      ans = ans.union(normalizeEvent(rawEventDataStream.get(i), stateDescriptor, broadcastStream, i));
    }
    return ans;
  }

  protected TdqRawEventProcessFunction getTdqRawEventProcessFunction(
      MapStateDescriptor<String, PhysicalPlans> stateDescriptor) {
    return new TdqRawEventProcessFunction(stateDescriptor);
  }

  private DataStream<TdqMetric> normalizeEvent(
      DataStream<RawEvent> rawEventDataStream,
      MapStateDescriptor<String, PhysicalPlans> stateDescriptor,
      BroadcastStream<PhysicalPlans> broadcastStream,
      int idx) {
    String slotSharingGroup = rawEventDataStream.getTransformation().getSlotSharingGroup();
    int parallelism = rawEventDataStream.getTransformation().getParallelism();
    String uid = "normalize_evt_" + idx;
    DataStream<TdqMetric> ds = rawEventDataStream.connect(broadcastStream)
        .process(getTdqRawEventProcessFunction(stateDescriptor))
        .name(uid)
        .uid(uid)
        .slotSharingGroup(slotSharingGroup)
        .setParallelism(parallelism);
    long orderless = DateUtils.toSeconds(getStringOrDefault("flink.app.advance.watermark.out-of-orderless", "0min"));
    long timeout = DateUtils.toSeconds(getStringOrDefault("flink.app.advance.watermark.idle-source-timeout", "0min"));
    if (orderless > 0 && timeout > 0) {
      final SerializableTimestampAssigner<TdqMetric> assigner =
          (SerializableTimestampAssigner<TdqMetric>) (event, timestamp) -> event.getEventTime();
      ds = ds.assignTimestampsAndWatermarks(
          WatermarkStrategy
              .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(orderless))
              .withTimestampAssigner(assigner)
              .withIdleness(Duration.ofSeconds(timeout))
      )
          .name(uid + "_wk")
          .uid(uid + "_wk")
          .slotSharingGroup(slotSharingGroup)
          .setParallelism(parallelism);
    }
    return ds;
  }
}
