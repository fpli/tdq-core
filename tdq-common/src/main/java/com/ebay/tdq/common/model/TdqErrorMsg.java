package com.ebay.tdq.common.model;

import com.ebay.tdq.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntzhang
 */
@Data
@NoArgsConstructor
public class TdqErrorMsg {

  private long processTime;
  private String metricKey;
  private TdqEvent tdqEvent;
  private String exception;

  public TdqErrorMsg(TdqEvent event, Exception e, String metricKey) {
    this.processTime = System.currentTimeMillis();
    this.tdqEvent = event;
    this.metricKey = metricKey;
    if (e != null) {
      this.exception = e.toString();
    }
  }

  public Map<String, Object> toIndexRequest() {
    Map<String, Object> json = new HashMap<>();
    json.put("metric_key", getMetricKey());
    json.put("process_time", new Date(processTime));
    try {
      json.put("tdq_event", JsonUtils.toJSONString(tdqEvent));
    } catch (JsonProcessingException ignore) {
      json.put("tdq_event", tdqEvent.toString());
    }
    json.put("exception", exception);
    return json;
  }
}
