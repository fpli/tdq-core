package com.ebay.tdq.sinks;


import com.ebay.tdq.common.env.ProntoEnv;
import com.ebay.tdq.common.env.SinkEnv;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.functions.ProntoSinkFunction;
import com.ebay.tdq.sources.HdfsConnectorFactory;
import com.ebay.tdq.sources.TdqMetricDateTimeBucketAssigner;
import com.ebay.tdq.utils.TdqContext;
import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
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

  @Deprecated
  @VisibleForTesting
  private static RichSinkFunction<InternalMetric> memoryFunction;

  @Deprecated
  @VisibleForTesting
  public static RichSinkFunction<InternalMetric> getMemoryFunction() {
    return memoryFunction;
  }

  @Deprecated
  @VisibleForTesting
  public static void setMemoryFunction(RichSinkFunction<InternalMetric> memoryFunction) {
    TdqSinks.memoryFunction = memoryFunction;
  }

  public static void sinkNormalMetric(String id, TdqContext tdqCxt, DataStream<InternalMetric> ds) {
    TdqEnv tdqEnv = tdqCxt.getTdqEnv();
    SinkEnv env = tdqEnv.getSinkEnv();
    if (env.isNormalMetricSinkStd()) {
      String uid = id + "_std";
      ds
          .print(env.getNormalMetricStdName())
          .uid(uid)
          .name(uid)
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
    if (env.isNormalMetricSinkPronto()) {
      ds
          .addSink(buildPronto(tdqEnv.getProntoEnv(), 1, new ProntoSinkFunction(tdqEnv)))
          .uid(id + "_pronto")
          .name(id + "_pronto")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
    if (env.isNormalMetricSinkHdfs()) {
      sinkHDFS(id, env.getNormalMetricPath(), tdqEnv, ds);
    }
    if (env.isNormalMetricSinkMemory()) {
      ds.addSink(memoryFunction)
          .name(id + "_mem")
          .setParallelism(1)
          .uid(id + "_mem");
    }
  }

  public static void sinkLatencyMetric(TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds) {
    TdqEnv tdqEnv = tdqCxt.getTdqEnv();
    SinkEnv env = tdqEnv.getSinkEnv();
    if (env.isLatencyMetricSinkStd()) {
      ds.getSideOutput(tdqCxt.getEventLatencyOutputTag())
          .print(env.getLatencyMetricStdName())
          .uid("evt_latency_o_std")
          .name("evt_latency_o_std")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
    if (env.isLatencyMetricSinkPronto()) {
      ds.getSideOutput(tdqCxt.getEventLatencyOutputTag())
          .addSink(buildPronto(tdqEnv.getProntoEnv(), 10,
              new ElasticsearchSinkFunction<InternalMetric>() {
                @Override
                public void process(InternalMetric tdqMetric, RuntimeContext runtimeContext,
                    RequestIndexer requestIndexer) {
                  long processTime = System.currentTimeMillis();
                  String index = env.getLatencyMetricProntoIndexPattern() + env.getIndexDateSuffix(processTime);
                  requestIndexer
                      .add(Requests.indexRequest().index(index).source(tdqMetric.toIndexRequest(processTime)));
                }
              }))
          .uid("evt_latency_o_pronto")
          .name("evt_latency_o_pronto")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
    if (env.isLatencyMetricSinkHdfs()) {
      sinkHDFS("evt_latency", env.getLatencyMetricPath(), tdqEnv, ds.getSideOutput(tdqCxt.getEventLatencyOutputTag()));
    }
  }

  public static void sinkSampleLog(TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds, String name) {
    TdqEnv tdqEnv = tdqCxt.getTdqEnv();
    SinkEnv env = tdqEnv.getSinkEnv();
    if (env.isSampleLogSinkPronto()) {
      ds.getSideOutput(tdqCxt.getSampleOutputTag())
          .addSink(buildPronto(tdqEnv.getProntoEnv(), 20,
              new ElasticsearchSinkFunction<TdqSampleData>() {
                @Override
                public void process(TdqSampleData tdqSampleData, RuntimeContext runtimeContext,
                    RequestIndexer requestIndexer) {
                  String index =
                      env.getSampleLogProntoIndexPattern() + env.getIndexDateSuffix(tdqSampleData.getProcessTime());
                  requestIndexer.add(Requests.indexRequest().index(index).source(tdqSampleData.toIndexRequest()));
                }
              }))
          .uid(name + "_log_sample_o_pronto")
          .name(name + "_log_sample_o_pronto")
          .setParallelism(3);
    }
    if (env.isSampleLogSinkStd()) {
      ds.getSideOutput(tdqCxt.getSampleOutputTag())
          .print(env.getSampleLogStdName())
          .uid(name + "_log_sample_o_std")
          .name(name + "_log_sample_o_std")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
  }

  public static void sinkException(TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds, String name) {
    TdqEnv tdqEnv = tdqCxt.getTdqEnv();
    SinkEnv env = tdqEnv.getSinkEnv();
    if (env.isExceptionLogSinkPronto()) {
      ds.getSideOutput(tdqCxt.getExceptionOutputTag())
          .addSink(buildPronto(tdqEnv.getProntoEnv(), 1,
              new ElasticsearchSinkFunction<TdqErrorMsg>() {
                @Override
                public void process(TdqErrorMsg tdqErrorMsg, RuntimeContext runtimeContext,
                    RequestIndexer requestIndexer) {
                  String index =
                      env.getExceptionLogProntoIndexPattern() + env.getIndexDateSuffix(tdqErrorMsg.getProcessTime());
                  requestIndexer.add(Requests.indexRequest().index(index).source(tdqErrorMsg.toIndexRequest()));
                }
              }))
          .uid(name + "_log_exception_o_pronto")
          .name(name + "_log_exception_o_pronto")
          .setParallelism(1);
    }
    if (env.isExceptionLogSinkStd()) {
      ds.getSideOutput(tdqCxt.getExceptionOutputTag())
          .print(env.getExceptionLogStdName())
          .uid(name + "_log_exception_o_std")
          .name(name + "_log_exception_o_std")
          .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
    }
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

  public static void sinkHDFS(String id, String path, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    StreamingFileSink<TdqMetric> sink = HdfsConnectorFactory.createWithParquet(
        path, TdqMetric.class, new TdqMetricDateTimeBucketAssigner(tdqEnv.getSinkEnv().getTimeZone().toZoneId()));
    ds.map(m -> m.toTdqMetric(tdqEnv.getJobName(), 6901)) // todo update schemaId
        .uid(id + "_avro")
        .name(id + "_avro")
        .setParallelism(1)
        .slotSharingGroup(id + "_hdfs")
        .addSink(sink)
        .uid(id + "_o_hdfs")
        .name(id + "_o_hdfs")
        .slotSharingGroup(id + "_hdfs")
        .setParallelism(1);

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
}
