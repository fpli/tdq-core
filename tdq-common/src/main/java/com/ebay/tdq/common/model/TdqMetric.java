package com.ebay.tdq.common.model;

import com.ebay.tdq.common.model.TdqMetricAvro.Builder;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author juntzhang
 */
@AllArgsConstructor
@Data
public class TdqMetric implements Serializable {

  private String tagId;      // metricKey + tags
  private Integer partition = 0;
  private Long window;       // seconds
  private Long eventTime;
  private String metricKey;
  private Map<String, String> aggrExpresses = new HashMap<>();
  private Map<String, Object> tags = new TreeMap<>();
  private Map<String, Double> values = new HashMap<>();
  private Double value = 0d;

  public TdqMetric() {
  }

  public TdqMetric(String metricKey, long eventTime) {
    this.metricKey = metricKey;
    this.eventTime = eventTime;
  }

  public TdqMetric genUID() {
    StringBuilder sb = new StringBuilder(metricKey);
    if (MapUtils.isNotEmpty(tags)) {
      sb.append("{");
      StringJoiner sj = new StringJoiner(",");
      for (Map.Entry<String, Object> e : tags.entrySet()) {
        sj.add(e.getKey() + "=" + e.getValue());
      }
      sb.append(sj).append("}");
    }
    setTagId(DigestUtils.md5Hex(sb.toString().getBytes()));
    return this;
  }

  public String getTagIdWithEventTime() {
    return tagId + "_" + getEventTime();
  }

  public TdqMetric removeTag(String k) {
    tags.remove(k);
    return this;
  }

  public TdqMetric putTag(String k, Object v) {
    tags.put(k, v);
    return this;
  }

  public TdqMetric putExpr(String k, Double v) {
    values.put(k, v);
    return this;
  }

  public TdqMetric putAggrExpress(String k, String v) {
    aggrExpresses.put(k, v);
    return this;
  }

  public TdqMetric setValue(Double d) {
    this.value = d;
    return this;
  }

  @Override
  public String toString() {
    // global_mandatory_tag_item_rate{page_family='BID',timestamp='2021-03-30 18:00'} 0.712
    final StringBuilder sb = new StringBuilder();
    sb.append(metricKey).append("{");
    StringJoiner sj = new StringJoiner(",");
    tags.forEach((k, v) -> sj.add("t-" + k + "=" + v));
    values.forEach((k, v) -> sj.add("e-" + k + "=" + v));
    sj.add("window" + "=" + window);
    sj.add("partition" + "=" + partition);
    sj.add("tag_id" + "=" + tagId);
    sj.add("eventTime" + "=" + FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(eventTime));
    sb.append(sj).append("}").append(" ").append(value);
    return sb.toString();
  }


  public Map<String, Object> toIndexRequest(Long processTime) {
    Map<String, Object> json = new HashMap<>();
    json.put("metric_key", getMetricKey());
    json.put("event_time", getEventTime());
    json.put("event_time_fmt", new Date(getEventTime()));
    json.put("process_time", new Date(processTime));
    Map<String, String> tags = new HashMap<>();
    if (MapUtils.isNotEmpty(getTags())) {
      getTags().forEach((k, v) -> {
        if (v != null && k != null) {
          tags.put(k, v.toString());
        }
      });
      json.put("tags", tags);
    }
    Map<String, Double> expr = new HashMap<>();
    getValues().forEach((k, v) -> {
      if (v != null) {
        expr.put(k, Double.valueOf(v.toString()));
      } else {
        expr.put(k, 0d);
      }
    });
    json.put("expr", expr);
    // json.put("value", element.getValue());
    return json;
  }

  public TdqMetricAvro toTdqAvroMetric() {
    final Builder builder = TdqMetricAvro.newBuilder();

    Map<String, String> tags = new HashMap<>();
    getTags().forEach((k, v) -> {
      if (v != null && k != null) {
        tags.put(k, v.toString());
      }
    });

    Map<String, Double> values = new HashMap<>();
    getValues().forEach((k, v) -> {
      if (v != null) {
        values.put(k, Double.valueOf(v.toString()));
      } else {
        values.put(k, 0d);
      }
    });

    return builder
        .setTags(tags)
        .setValues(values)
        .setMetricName(getMetricKey())
        .setMetricId(getTagId())
        .setProcessTime(System.currentTimeMillis())
        .setEventTime(getEventTime())
        .build();
    //m.setRheosHeader(RheosHeader.newBuilder()
    //    .setEventCreateTimestamp(System.currentTimeMillis())
    //    .setEventSentTimestamp(System.currentTimeMillis())
    //    .setSchemaId(100)
    //    .setEventId("101")
    //    .setProducerId("102")
    //    .build());
  }
}
