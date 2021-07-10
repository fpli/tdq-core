//package com.ebay.tdq.functions;
//
//import com.ebay.tdq.rules.TdqMetric;
//import com.ebay.tdq.utils.TdqEnv;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.flink.api.common.ExecutionConfig;
//import org.apache.flink.api.common.typeutils.base.VoidSerializer;
//import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.metrics.Counter;
//import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.message.BasicHeader;
//import org.elasticsearch.action.DocWriteRequest;
//import org.elasticsearch.action.bulk.BackoffPolicy;
//import org.elasticsearch.action.bulk.BulkItemResponse;
//import org.elasticsearch.action.bulk.BulkProcessor;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.Requests;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.unit.ByteSizeUnit;
//import org.elasticsearch.common.unit.ByteSizeValue;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.rest.RestStatus;
//
//import static com.ebay.tdq.utils.DateUtils.calculateIndexDate;
//
///**
// * @author juntzhang
// */
//@Slf4j
//public class Pronto2PhaseCommitSinkFunction extends TwoPhaseCommitSinkFunction<TdqMetric, Void, Void> {
//  private final TdqEnv tdqEnv;
//  private transient Map<String, Counter> counterMap;
//  private transient RestHighLevelClient client;
//  private transient BulkProcessor processor;
//
//  private final String indexPattern;
//
//  public Pronto2PhaseCommitSinkFunction(TdqEnv tdqEnv) {
//    super(
//        new KryoSerializer<>(Void.class, new ExecutionConfig()),
//        VoidSerializer.INSTANCE, clock);
//    this.tdqEnv       = tdqEnv;
//    this.indexPattern = tdqEnv.getProntoConfig().getIndexPattern();
//  }
//
//  @Override
//  public void open(Configuration parameters) {
//    counterMap = new HashMap<>();
//    String username = tdqEnv.getProntoConfig().getUsername();
//    String password = tdqEnv.getProntoConfig().getPassword();
//    HttpHost httpHost = new HttpHost(
//        tdqEnv.getProntoConfig().getHostname(),
//        tdqEnv.getProntoConfig().getPort(),
//        tdqEnv.getProntoConfig().getSchema()
//    );
//    RestClientBuilder builder = RestClient
//        .builder(httpHost)
//        .setHttpClientConfigCallback(httpClientBuilder -> {
//          CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//          credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//          return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
//        })
//        .setDefaultHeaders(new BasicHeader[]{new BasicHeader("Content-Type", "application/json")});
//
//    client = new RestHighLevelClient(builder);
//    //    builder.setFailureListener();
//
//    BulkProcessor.Listener listener = new BulkProcessor.Listener() {
//
//      @Override
//      public void beforeBulk(long l, BulkRequest bulkRequest) {
//
//      }
//
//      public Throwable extractFailureCauseFromBulkItemResponse(BulkItemResponse bulkItemResponse) {
//        if (!bulkItemResponse.isFailed()) {
//          return null;
//        } else {
//          return bulkItemResponse.getFailure().getCause();
//        }
//      }
//
//      @Override
//      public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
//        if (response.hasFailures()) {
//          for (int i = 0; i < response.getItems().length; ++i) {
//            BulkItemResponse itemResponse = response.getItems()[i];
//            Throwable failure = extractFailureCauseFromBulkItemResponse(itemResponse);
//            if (failure != null) {
//              RestStatus restStatus = itemResponse.getFailure().getStatus();
//              DocWriteRequest<?> actionRequest = request.requests().get(i);
//              log.error("onFailure => {}, restStatusCode={}, because of {}", actionRequest, restStatus, failure);
//            }
//          }
//        }
//      }
//
//      @Override
//      public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
//
//      }
//    };
//    processor = BulkProcessor.builder((bulkRequest, bulkListener) ->
//        client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkListener), listener)
//        .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.MB))
//        .setBulkActions(1)
//        .setConcurrentRequests(1)
//        .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(3), 3))
//        .build();
//  }
//
//  @Override
//  protected void invoke(Void transaction, TdqMetric value, Context context) throws Exception {
//    processor.add(createIndexRequest(value));
//  }
//
//  private IndexRequest createIndexRequest(TdqMetric tdqMetric) {
//    String index = indexPattern + calculateIndexDate(tdqMetric.getEventTime());
//    try {
//      return Requests.indexRequest()
//          .id(tdqMetric.getTagIdWithET())
//          .index(index)
//          .source(tdqMetric.toIndexRequest(System.currentTimeMillis()));
//    } catch (Exception e) {
//      log.error("metric={}, msg={}, index={}", tdqMetric, e.getMessage(), index);
//      throw e;
//    }
//  }
//
//  @Override
//  protected Void beginTransaction() throws Exception {
//    return null;
//  }
//
//  @Override
//  protected void preCommit(Void transaction) throws Exception {
//
//  }
//
//  @Override
//  protected void commit(Void transaction) {
//
//  }
//
//  @Override
//  protected void abort(Void transaction) {
//
//  }
//}
