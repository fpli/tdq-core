package com.ebay.tdq.sinks;

import com.ebay.tdq.rules.TdqMetric;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStream;
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

import static com.ebay.tdq.utils.TdqConstant.PRONTO_HOSTNAME;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_INDEX_PATTERN;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_PASSWORD;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_PORT;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_SCHEME;
import static com.ebay.tdq.utils.TdqConstant.PRONTO_USERNAME;

/**
 * @author juntzhang
 */
public class ProntoSink {
  public static ElasticsearchSink<TdqMetric> build() {
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
        restClientBuilder.setDefaultHeaders(
            new BasicHeader[]{new BasicHeader("Content-Type", "application/json")});
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
          CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
          credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
          return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
      });
    }
    return esSinkBuilder.build();
  }

  public static void output(DataStream<TdqMetric> ds, String uid, int parallelism) {
    ds.addSink(build())
        .uid(uid)
        .name(uid)
        .setParallelism(parallelism);
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
}
