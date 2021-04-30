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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RuntimeContext;
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
import org.apache.flink.streaming.connectors.elasticsearch.ActionRequestFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_QUEUE_SIZE;
import static com.ebay.tdq.utils.TdqConstant.OUTPUT_TAG_MAP;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_COLLECTOR_BY_WINDOW;
import static com.ebay.tdq.utils.TdqConstant.PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_HOSTNAME;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_INDEX_PATTERN;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_PASSWORD;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_PORT;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_SCHEME;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_USERNAME;
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
    outputToPronto(outputTags);
  }

  protected void print(Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags) {
    outputTags.forEach((key, ds) -> {
      ds.print("FINAL OUT " + key)
          .uid("final-" + key)
          .name("Final " + key)
          .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR);
    });
  }

  protected void outputToPronto(Map<String, SingleOutputStreamOperator<TdqMetric>> outputTags) {
    outputTags.forEach((key, ds) -> {
      ds.addSink(getProntoSinker())
          .uid("final-" + key)
          .name("Final " + key)
          .setParallelism(PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR);
    });
  }

  protected ElasticsearchSink<TdqMetric> getProntoSinker() {
    String username = PRONTO_USERNAME;
    String password = PRONTO_PASSWORD;
    List<HttpHost> httpHosts = new ArrayList<>();
    httpHosts.add(new HttpHost(PRONTO_HOSTNAME, PRONTO_PORT, PRONTO_SCHEME));
    ElasticsearchSink.Builder<TdqMetric> esSinkBuilder = new ElasticsearchSink.Builder<>(
        httpHosts,
        (TdqMetric element, RuntimeContext ctx, RequestIndexer indexer) -> indexer.add(createIndexRequest(element))
    );
    esSinkBuilder.setFailureHandler(new CustomFailureHandler());
    esSinkBuilder.setBulkFlushMaxActions(1);
    if (StringUtils.isNotBlank(username)) {
      esSinkBuilder.setRestClientFactory(restClientBuilder -> {
        restClientBuilder.setDefaultHeaders(new BasicHeader[]{new BasicHeader("Content-Type", "application/json")});
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
          CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
          credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
          return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
      });
    }
    return esSinkBuilder.build();
  }

  @Slf4j
  private static class CustomFailureHandler implements ActionRequestFailureHandler {
    private static final long serialVersionUID = 942269087742453482L;

    @Override
    public void onFailure(ActionRequest action, Throwable failure, int restStatusCode, RequestIndexer indexer) {
      if (action instanceof IndexRequest) {
        log.error("es onFailure:" + action.toString());
        indexer.add((IndexRequest) action);
      } else {
        throw new IllegalStateException("unexpected");
      }
    }
  }

  private static IndexRequest createIndexRequest(TdqMetric element) {
    Map<String, Object> json = new HashMap<>();
    json.put("metric_key", element.getMetricKey());
    json.put("event_time", element.getEventTime());
    Map<String, String> tags = new HashMap<>();
    element.getTags().forEach((k, v) -> tags.put(k, v.toString()));
    json.put("tags", tags);
    Map<String, Double> expr = new HashMap<>();
    element.getExprMap().forEach((k, v) -> expr.put(k, Double.valueOf(v.toString())));
    json.put("expr", expr);
    json.put("value", element.getValue());
    String index = PRONTO_INDEX_PATTERN + DateFormatUtils.format(element.getEventTime(), "yyyy.MM.dd");
    return Requests.indexRequest().index(index).source(json);
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
