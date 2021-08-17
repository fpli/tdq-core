package com.ebay.tdq.functions;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.sinks.ProntoSink;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

/**
 * @author juntzhang
 */
@Slf4j
public class ProntoSinkFunction implements ElasticsearchSinkFunction<InternalMetric> {

  private final TdqEnv tdqEnv;
  private transient Map<String, Counter> counterMap;

  public ProntoSinkFunction(TdqEnv tdqEnv) {
    this.tdqEnv = tdqEnv;
  }

  @Override
  public void open() {
    counterMap = new HashMap<>();
  }


  public void inc(RuntimeContext runtimeContext, String key, long v) {
    Counter counter = counterMap.get(key);
    if (counter == null) {
      counter = runtimeContext.getMetricGroup().addGroup("tdq").counter(key);
      counterMap.put(key, counter);
    }
    counter.inc(v);
  }

  @Override
  public void process(InternalMetric m, RuntimeContext runtimeContext, RequestIndexer indexer) {
    try {
      indexer.add(createIndexRequest(m));
    } catch (Throwable e) {
      inc(runtimeContext, "pronto_index_error", 1);
      throw e;
    }
  }

  private IndexRequest createIndexRequest(InternalMetric tdqMetric) {
    String index = ProntoSink.getIndexDateSuffix(tdqMetric.getEventTime(), tdqEnv.getTimeZone());
    try {
      return Requests.indexRequest()
          .id(tdqMetric.getMetricIdWithEventTime())
          .index(index)
          .source(tdqMetric.toIndexRequest(System.currentTimeMillis()));
    } catch (Exception e) {
      log.error("metric={}, msg={}, index={}", tdqMetric, e.getMessage(), index);
      throw e;
    }
  }
}
