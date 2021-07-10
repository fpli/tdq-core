package com.ebay.tdq.rules;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntzhang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TdqSampleData {
  private long processTime;
  private String metricKey;
  private RawEvent rawEvent;
  private TdqMetric tdqMetric;
  private String params;

  public TdqSampleData(RawEvent rawEvent, TdqMetric tdqMetric, String metricKey, String params) {
    this.processTime = System.currentTimeMillis();
    this.rawEvent    = rawEvent;
    this.metricKey   = metricKey;
    this.tdqMetric   = tdqMetric;
    this.params      = params;
  }

  public Map<String, Object> toIndexRequest() {
    Map<String, Object> json = new HashMap<>();
    json.put("metric_key", getMetricKey());
    if (tdqMetric != null) {
      try {
        json.put("tdq_metric", JsonUtils.toJSONString(tdqMetric));
      } catch (JsonProcessingException ignore) {
        json.put("tdq_metric", tdqMetric.toString());
      }
    }
    if (rawEvent != null) {
      try {
        json.put("raw_event", JsonUtils.toJSONString(rawEvent));
      } catch (JsonProcessingException ignore) {
        json.put("raw_event", rawEvent.toString());
      }
    }
    json.put("process_time", new Date(processTime));
    json.put("params", params);
    return json;
  }
}
