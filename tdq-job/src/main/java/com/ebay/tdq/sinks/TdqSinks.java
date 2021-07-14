package com.ebay.tdq.sinks;

import com.ebay.tdq.common.model.TdqAvroMetric;
import com.ebay.tdq.functions.ProntoSinkFunction;
import com.ebay.tdq.rules.TdqErrorMsg;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.rules.TdqSampleData;
import com.ebay.tdq.sources.HdfsConnectorFactory;
import com.ebay.tdq.sources.TdqMetricDateTimeBucketAssigner;
import com.ebay.tdq.utils.ProntoConfig;
import com.ebay.tdq.utils.TdqEnv;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
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
@Slf4j
public class TdqSinks implements Serializable {

  public static void sinkNormalMetric(String id, TdqEnv tdqEnv, DataStream<TdqMetric> ds) {
    if (tdqEnv.getSinkTypes().contains("console")) {
      String uid = id + "_std";
      ds
          .print(uid.toUpperCase())
          .uid(uid)
          .name(uid)
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
    if (tdqEnv.getSinkTypes().contains("pronto")) {
      ds
          .addSink(buildPronto(tdqEnv, 1, new ProntoSinkFunction(tdqEnv.getProntoConfig().getIndexPattern())))
          .uid(id + "_pronto")
          .name(id + "_pronto")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
    sinkHDFS(id, tdqEnv, ds);
  }

  public static void sinkLatencyMetric(TdqEnv tdqEnv, SingleOutputStreamOperator<TdqMetric> ds) {
    if (tdqEnv.getSinkTypes().contains("console")) {
      ds
          .print("EVT_LATENCY_O_STD")
          .uid("evt_latency_o_std")
          .name("evt_latency_o_std")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
    if (tdqEnv.getSinkTypes().contains("pronto")) {
      ds.getSideOutput(tdqEnv.getEventLatencyOutputTag())
          .addSink(buildPronto(tdqEnv, 10,
              new ElasticsearchSinkFunction<TdqMetric>() {
                private final ProntoConfig cfg = tdqEnv.getProntoConfig();

                @Override
                public void process(TdqMetric tdqMetric, RuntimeContext runtimeContext,
                    RequestIndexer requestIndexer) {
                  long processTime = System.currentTimeMillis();
                  String index = cfg.getLatencyIndexPattern() + cfg.getIndexDateSuffix(processTime);
                  requestIndexer
                      .add(Requests.indexRequest().index(index).source(tdqMetric.toIndexRequest(processTime)));
                }
              }))
          .uid("evt_latency_o_pronto")
          .name("evt_latency_o_pronto")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
  }

  public static void sinkDebugLog(TdqEnv tdqEnv, SingleOutputStreamOperator<TdqMetric> ds) {
    if (tdqEnv.getSinkTypes().contains("pronto")) {
      ds.getSideOutput(tdqEnv.getDebugOutputTag())
          .addSink(buildPronto(tdqEnv, 1,
              new ElasticsearchSinkFunction<TdqSampleData>() {
                private final ProntoConfig cfg = tdqEnv.getProntoConfig();

                @Override
                public void process(TdqSampleData tdqSampleData, RuntimeContext runtimeContext,
                    RequestIndexer requestIndexer) {
                  String index =
                      cfg.getDebugIndexPattern() + cfg.getIndexDateSuffix(tdqSampleData.getProcessTime());
                  requestIndexer.add(Requests.indexRequest().index(index).source(tdqSampleData.toIndexRequest()));
                }
              }))
          .uid("log_debug_o_pronto")
          .name("log_debug_o_pronto")
          .setParallelism(1);
    }
  }

  public static void sinkSampleLog(TdqEnv tdqEnv, SingleOutputStreamOperator<TdqMetric> ds) {
    if (tdqEnv.getSinkTypes().contains("pronto")) {
      ds.getSideOutput(tdqEnv.getSampleOutputTag())
          .addSink(buildPronto(tdqEnv, 20,
              new ElasticsearchSinkFunction<TdqSampleData>() {
                private final ProntoConfig cfg = tdqEnv.getProntoConfig();

                @Override
                public void process(TdqSampleData tdqSampleData, RuntimeContext runtimeContext,
                    RequestIndexer requestIndexer) {
                  String index =
                      cfg.getSampleIndexPattern() + cfg.getIndexDateSuffix(tdqSampleData.getProcessTime());
                  requestIndexer.add(Requests.indexRequest().index(index).source(tdqSampleData.toIndexRequest()));
                }
              }))
          .uid("log_sample_o_pronto")
          .name("log_sample_o_pronto")
          .setParallelism(3);
    }
  }

  public static void sinkException(TdqEnv tdqEnv, SingleOutputStreamOperator<TdqMetric> ds) {
    if (tdqEnv.getSinkTypes().contains("pronto")) {
      ds.getSideOutput(tdqEnv.getExceptionOutputTag())
          .addSink(buildPronto(tdqEnv, 1,
              new ElasticsearchSinkFunction<TdqErrorMsg>() {
                private final ProntoConfig cfg = tdqEnv.getProntoConfig();

                @Override
                public void process(TdqErrorMsg tdqErrorMsg, RuntimeContext runtimeContext,
                    RequestIndexer requestIndexer) {
                  String index = cfg.getExceptionIndexPattern() + cfg.getIndexDateSuffix(tdqErrorMsg.getProcessTime());
                  requestIndexer.add(Requests.indexRequest().index(index).source(tdqErrorMsg.toIndexRequest()));
                }
              }))
          .uid("log_exception_o_pronto")
          .name("log_exception_o_pronto")
          .setParallelism(1);
    }
  }

  private static <T> ElasticsearchSink<T> buildPronto(TdqEnv tdqEnv, int numMaxActions,
      ElasticsearchSinkFunction<T> function) {
    String username = tdqEnv.getProntoConfig().getUsername();
    String password = tdqEnv.getProntoConfig().getPassword();
    List<HttpHost> httpHosts = new ArrayList<>();
    HttpHost httpHost = new HttpHost(
        tdqEnv.getProntoConfig().getHostname(),
        tdqEnv.getProntoConfig().getPort(),
        tdqEnv.getProntoConfig().getSchema()
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


  public static void sinkHDFS(String id, TdqEnv tdqEnv, DataStream<TdqMetric> ds) {
    StreamingFileSink<TdqAvroMetric> sink = HdfsConnectorFactory
        .createWithParquet(tdqEnv.getHdfsConfig().getNormalMetricPath(),
            TdqAvroMetric.class, new TdqMetricDateTimeBucketAssigner());
    ds.map(TdqMetric::toTdqAvroMetric)
        .uid(id + "_avro_transformer")
        .name(id + "_avro_transformer")
        .setParallelism(1)
        .slotSharingGroup(id + "_hdfs")
        .addSink(sink)
        .uid(id + "_hdfs")
        .name(id + "_hdfs")
        .slotSharingGroup(id + "_hdfs")
        .setParallelism(1);

  }
}
