package com.ebay.tdq.common.model;

import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
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
  private TdqEvent tdqEvent;
  private TdqMetric tdqMetric;
  private String params;

  public TdqSampleData(TdqEvent tdqEvent, TdqMetric tdqMetric, String metricKey, String params) {
    this.processTime = System.currentTimeMillis();
    this.tdqEvent = tdqEvent;
    this.metricKey = metricKey;
    this.tdqMetric = tdqMetric;
    this.params = params;
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
    if (tdqEvent != null) {
      try {
        json.put("tdq_event", JsonUtils.toJSONString(tdqEvent));
      } catch (JsonProcessingException ignore) {
        json.put("tdq_event", tdqEvent.toString());
      }
    }
    json.put("process_time", new Date(processTime));
    json.put("params", params);
    return json;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TdqSampleData.class.getSimpleName() + "[", "]")
        .add("processTimeFmt=" + DateUtils.format(processTime))
        .add("processTime=" + processTime)
        .add("metricKey='" + metricKey + "'")
        .add("tdqEvent=" + tdqEvent)
        .add("tdqMetric=" + tdqMetric)
        .add("params='" + params + "'")
        .toString();
  }
}
