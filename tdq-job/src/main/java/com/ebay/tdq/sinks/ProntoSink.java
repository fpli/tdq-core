package com.ebay.tdq.sinks;

import static com.ebay.sojourner.common.env.EnvironmentUtils.replaceStringWithPattern;

import com.ebay.tdq.common.env.ProntoEnv;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.config.SinkConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.elasticsearch.ActionRequestFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkBase;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.flink.util.ExceptionUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;

/**
 * @author juntzhang
 */
public class ProntoSink implements Sinkable {

  @Override
  public void sinkNormalMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    Map<String, Object> props = sinkConfig.getConfig();
    int parallelism = ((int) props.getOrDefault("rhs-parallelism", 2));
    String pattern = replaceStringWithPattern((String) props.get("index-pattern"));
    ds
        .addSink(buildPronto(tdqEnv.getProntoEnv(), 1,
            (ElasticsearchSinkFunction<InternalMetric>) (tdqMetric, runtimeContext, requestIndexer) -> {
              long processTime = System.currentTimeMillis();
              if (tdqMetric.getMetricName().startsWith("exception") &&
                  Math.abs(new Random().nextDouble()) <
                      Double.parseDouble(tdqMetric.getMetricName().split("-")[1])) {
                throw new RuntimeException("this is test sink exception, checking checkpoint recovery.");
              }
              String index = pattern + getIndexDateSuffix(tdqMetric.getEventTime(), tdqEnv.getTimeZone());
              requestIndexer
                  .add(
                      Requests.indexRequest()
                          .id(tdqMetric.getMetricIdWithEventTime())
                          .index(index)
                          .source(tdqMetric.toIndexRequest(processTime))
                  );
            }))
        .uid(id + "_pronto")
        .name(id + "_pronto")
        .setParallelism(parallelism);
  }

  @Override
  public void sinkLatencyMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    Map<String, Object> props = sinkConfig.getConfig();
    String pattern = replaceStringWithPattern((String) props.get("index-pattern"));
    int parallelism = ((int) props.getOrDefault("rhs-parallelism", 2));
    ds
        .addSink(buildPronto(tdqEnv.getProntoEnv(), 10,
            new ElasticsearchSinkFunction<InternalMetric>() {
              @Override
              public void process(InternalMetric tdqMetric, RuntimeContext runtimeContext,
                  RequestIndexer requestIndexer) {
                if (tdqMetric.getMetricName().startsWith("exception_latency") &&
                    Math.abs(new Random().nextDouble()) <
                        Double.parseDouble(tdqMetric.getMetricName().split("-")[1])) {
                  throw new RuntimeException("this is test latency sink exception, checking checkpoint recovery.");
                }
                long processTime = System.currentTimeMillis();
                String index = pattern + getIndexDateSuffix(tdqMetric.getEventTime(), tdqEnv.getTimeZone());
                requestIndexer
                    .add(Requests.indexRequest().index(index).source(tdqMetric.toIndexRequest(processTime)));
              }
            }))
        .uid(id + "_pronto")
        .name(id + "_pronto")
        .setParallelism(parallelism);
  }

  @Override
  public void sinkSampleLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqSampleData> ds) {
    Map<String, Object> props = sinkConfig.getConfig();
    String pattern = replaceStringWithPattern((String) props.get("index-pattern"));
    int parallelism = ((int) props.getOrDefault("rhs-parallelism", 3));
    ds
        .addSink(buildPronto(tdqEnv.getProntoEnv(), 20,
            new ElasticsearchSinkFunction<TdqSampleData>() {
              @Override
              public void process(TdqSampleData tdqSampleData, RuntimeContext runtimeContext,
                  RequestIndexer requestIndexer) {
                if (tdqSampleData.getMetricKey().startsWith("exception_sample") &&
                    Math.abs(new Random().nextDouble()) <
                        Double.parseDouble(tdqSampleData.getMetricKey().split("-")[1])) {
                  throw new RuntimeException("this is test sink exception, checking checkpoint recovery.");
                }
                String index =
                    pattern + getIndexDateSuffix(tdqSampleData.getProcessTime(), tdqEnv.getTimeZone());
                requestIndexer.add(Requests.indexRequest().index(index).source(tdqSampleData.toIndexRequest()));
              }
            }))
        .uid(id + "_log_sample_o_pronto")
        .name(id + "_log_sample_o_pronto")
        .setParallelism(parallelism);
  }

  @Override
  public void sinkExceptionLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqErrorMsg> ds) {
    Map<String, Object> props = sinkConfig.getConfig();
    String pattern = replaceStringWithPattern((String) props.get("index-pattern"));
    int parallelism = (int) props.getOrDefault("rhs-parallelism", 1);
    ds
        .addSink(buildPronto(tdqEnv.getProntoEnv(), 1,
            new ElasticsearchSinkFunction<TdqErrorMsg>() {
              @Override
              public void process(TdqErrorMsg tdqErrorMsg, RuntimeContext runtimeContext,
                  RequestIndexer requestIndexer) {
                String index =
                    pattern + getIndexDateSuffix(tdqErrorMsg.getProcessTime(), tdqEnv.getTimeZone());
                requestIndexer.add(Requests.indexRequest().index(index).source(tdqErrorMsg.toIndexRequest()));
              }
            }))
        .uid(id + "_log_exception_o_pronto")
        .name(id + "_log_exception_o_pronto")
        .setParallelism(parallelism);
  }

  private static <T> ElasticsearchSink<T> buildPronto(ProntoEnv env, int numMaxActions,
      ElasticsearchSinkFunction<T> function) {
    String username = env.getUsername();
    String password = env.getPassword();
    List<HttpHost> httpHosts = new ArrayList<>();
    HttpHost httpHost = new HttpHost(
        env.getHostname(),
        env.getPort(),
        env.getSchema()
    );
    httpHosts.add(httpHost);
    ElasticsearchSink.Builder<T> builder = new ElasticsearchSink.Builder<>(httpHosts, function);
    builder.setFailureHandler(new ProntoActionRequestFailureHandler());
    builder.setBulkFlushMaxActions(numMaxActions);
    builder.setBulkFlushBackoff(true);
    builder.setBulkFlushBackoffRetries(3);
    builder.setBulkFlushBackoffType(ElasticsearchSinkBase.FlushBackoffType.CONSTANT);
    builder.setBulkFlushBackoffDelay(3000);
    if (StringUtils.isNotBlank(username)) {
      builder.setRestClientFactory(restClientBuilder -> {
        restClientBuilder.setDefaultHeaders(
            new BasicHeader[]{new BasicHeader("Content-Type", "application/json")});
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
          CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
          credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
          return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
      });
    }
    return builder.build();
  }


  @Slf4j
  private static class ProntoActionRequestFailureHandler implements ActionRequestFailureHandler {

    private static final long serialVersionUID = 942269087742453482L;

    @Override
    public void onFailure(ActionRequest action, Throwable failure, int restStatusCode,
        RequestIndexer indexer) throws Throwable {
      if (ExceptionUtils.findThrowable(failure, EsRejectedExecutionException.class).isPresent()) {
        // full queue; re-add document for indexing
        if (action instanceof IndexRequest) {
          log.error("onFailure => {}, restStatusCode={}, because of {}", action, restStatusCode, failure);
          indexer.add((IndexRequest) action);
        }
      } else if (ExceptionUtils.findThrowable(failure, ElasticsearchParseException.class).isPresent()) {
        // malformed document; simply drop request without failing sink
        log.error("Pronto onFailure:" + action.toString());
      } else {
        // for all other failures, fail the sink;
        // here the failure is simply rethrown, but users can also choose to throw custom exceptions
        throw failure;
      }
    }
  }


  public static String getIndexDateSuffix(long ts, TimeZone timeZone) {
    return FastDateFormat.getInstance("yyyy-MM-dd", timeZone).format(ts);
  }
}
