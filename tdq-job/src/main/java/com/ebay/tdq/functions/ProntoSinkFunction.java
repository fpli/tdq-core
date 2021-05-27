package com.ebay.tdq.functions;

import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import static com.ebay.tdq.utils.DateUtils.calculateIndexDate;

/**
 * @author juntzhang
 */
@Slf4j
public class ProntoSinkFunction implements ElasticsearchSinkFunction<TdqMetric> {
  private final String indexPattern;
  private transient Map<String, Counter> counterMap;

  public ProntoSinkFunction(String indexPattern) {
    this.indexPattern = indexPattern;
  }

  @Override
  public void open() {
    counterMap = new HashMap<>();
  }


  public void inc(RuntimeContext runtimeContext, String key, long v) {
    if (indexPattern.contains("latency")) {
      key = key + "_latency";
    } else {
      key = key + "_current";
    }
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
    try {
      indexer.add(createIndexRequest(m, indexPattern));
    } catch (Throwable e) {
      inc(runtimeContext, "pronto_index_error", 1);
      throw e;
    }
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
      IndexRequest ir;
      if (indexPattern.contains("latency")) {
        // todo at least once
        ir = Requests.indexRequest().index(index).source(json);
      } else {
        ir = Requests.indexRequest().id(element.getTagIdWithET()).index(index).source(json);
      }
      return ir;
    } catch (Exception e) {
      log.error("metric={}, msg={}, index={}", element, e.getMessage(), index);
      throw e;
    }
  }
}
