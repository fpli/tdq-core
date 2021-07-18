package com.ebay.tdq.rules;

import com.ebay.sojourner.common.model.RawEvent;
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
  private RawEvent rawEvent;
  private String exception;

  public TdqErrorMsg(RawEvent rawEvent, Exception e, String metricKey) {
    this.processTime = System.currentTimeMillis();
    this.rawEvent    = rawEvent;
    this.metricKey   = metricKey;
    if (e != null) {
      this.exception = e.toString();
    }
  }

  public Map<String, Object> toIndexRequest() {
    Map<String, Object> json = new HashMap<>();
    json.put("metric_key", getMetricKey());
    json.put("process_time", new Date(processTime));
    try {
      json.put("raw_event", JsonUtils.toJSONString(rawEvent));
    } catch (JsonProcessingException ignore) {
      json.put("raw_event", rawEvent.toString());
    }
    json.put("exception", exception);
    return json;
  }
}
