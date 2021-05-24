package com.ebay.tdq.sinks;

import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.metrics.Counter;
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

import static com.ebay.tdq.utils.DateUtils.calculateIndexDate;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_HOSTNAME;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_PASSWORD;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_PORT;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_SCHEME;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_USERNAME;

/**
 * @author juntzhang
 */
@Slf4j
public class ProntoSink implements Serializable {
  private final String uid;
  private final int parallelism;
  private final String indexPattern;
  private final int numMaxActions;

  public ProntoSink(String uid, int parallelism, String indexPattern) {
    this(uid, parallelism, indexPattern, 1);
  }

  public ProntoSink(String uid, int parallelism, String indexPattern, int numMaxActions) {
    this.uid           = uid;
    this.parallelism   = parallelism;
    this.indexPattern  = indexPattern;
    this.numMaxActions = numMaxActions;
  }

  public static ElasticsearchSink<TdqMetric> build(String indexPattern, int numMaxActions) {
    String username = PRONTO_USERNAME;
    String password = PRONTO_PASSWORD;
    List<HttpHost> httpHosts = new ArrayList<>();
    HttpHost httpHost = new HttpHost(PRONTO_HOSTNAME, PRONTO_PORT, PRONTO_SCHEME);
    log.info("httpHost={},username={}", httpHost, username);
    httpHosts.add(httpHost);
    ElasticsearchSink.Builder<TdqMetric> builder = new ElasticsearchSink.Builder<>(httpHosts,
        new ProntoSinkFunction(indexPattern));
    builder.setFailureHandler(new ProntoActionRequestFailureHandler());
    builder.setBulkFlushMaxActions(numMaxActions);
    builder.setBulkFlushBackoff(true);
    builder.setBulkFlushBackoffRetries(3);
    builder.setBulkFlushBackoffType(ElasticsearchSinkBase.FlushBackoffType.CONSTANT);
    builder.setBulkFlushBackoffDelay(3000);
    if (StringUtils.isNotBlank(username)) {
      log.info("=== with UsernamePasswordCredentials ===");
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

  public void output(DataStream<TdqMetric> ds) {
    ds.addSink(build(indexPattern, numMaxActions))
        .uid(uid)
        .name(uid)
        .setParallelism(parallelism);
  }

  private static class ProntoSinkFunction implements ElasticsearchSinkFunction<TdqMetric> {
    private final String indexPattern;
    private final Map<String, Counter> counterMap = new HashMap<>();

    ProntoSinkFunction(String indexPattern) {
      this.indexPattern = indexPattern;
    }

    public void inc(RuntimeContext runtimeContext,String key, long v) {
      Counter counter = counterMap.get(key);
      if (counter == null) {
        counter = runtimeContext.getMetricGroup().addGroup("tdq3").counter(key);
        counterMap.put(key, counter);
      }
      counter.inc(v);
    }
    @Override
    public void process(TdqMetric m, RuntimeContext runtimeContext, RequestIndexer indexer) {
      if (m.getExprMap() != null && m.getExprMap().get("p1") != null) {
        inc(runtimeContext,
            m.getMetricKey() + "_" + DateUtils.getMinBuckets(m.getEventTime(), 5),
            (long) (double) m.getExprMap().get("p1")
        );
      }
      indexer.add(createIndexRequest(m, indexPattern));
    }

    private static IndexRequest createIndexRequest(TdqMetric element, String indexPattern) {
      String index = indexPattern + calculateIndexDate(element.getEventTime());
      try {
        Map<String, Object> json = new HashMap<>();
        json.put("metric_key", element.getMetricKey());
        json.put("event_time", element.getEventTime());
        json.put("event_time_fmt", new Date(element.getEventTime()));
        json.put("process_time", new Date());
        Map<String, String> tags = new HashMap<>();
        if (MapUtils.isNotEmpty(element.getTags())) {
          element.getTags().forEach((k, v) -> {
            if (v != null && k != null) {
              tags.put(k, v.toString());
            }
          });
          json.put("tags", tags);
        }
        Map<String, Double> expr = new HashMap<>();
        element.getExprMap().forEach((k, v) -> {
          if (v != null) {
            expr.put(k, Double.valueOf(v.toString()));
          } else {
            expr.put(k, 0d);
          }
        });
        json.put("expr", expr);
        json.put("value", element.getValue());
        IndexRequest ir = Requests.indexRequest().index(index).source(json);
        log.info("pronto index=> {}", ir);
        return ir;
      } catch (Exception e) {
        log.error("metric={}, msg={}, index={}", element, e.getMessage(), index);
        throw e;
      }
    }
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
          indexer.add((IndexRequest) action);
          log.error("onFailure => {}, restStatusCode={}, because of {}", action, restStatusCode, failure);
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
}
