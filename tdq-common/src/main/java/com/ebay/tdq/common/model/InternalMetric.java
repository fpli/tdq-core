package com.ebay.tdq.common.model;

import com.ebay.tdq.common.model.TdqMetric.Builder;
import com.ebay.tdq.utils.DateUtils;
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
import org.jetbrains.annotations.NotNull;

/**
 * @author juntzhang
 */
@AllArgsConstructor
@Data
public class InternalMetric implements Serializable {

  private String metricId;      // metricName + tags
  private Integer partition = 0;
  private Long window;          // seconds
  private Long eventTime;
  private String metricName;
  private Map<String, String> aggrExpresses = new HashMap<>();
  private Map<String, Object> tags = new TreeMap<>();
  private Map<String, Double> values = new HashMap<>();
  private Double value = 0d;

  public InternalMetric() {
  }

  public InternalMetric(String metricName, long eventTime) {
    this.metricName = metricName;
    this.eventTime = eventTime;
  }

  public InternalMetric genMetricId() {
    StringBuilder sb = new StringBuilder(metricName);
    if (MapUtils.isNotEmpty(tags)) {
      sb.append("{");
      StringJoiner sj = new StringJoiner(",");
      for (Map.Entry<String, Object> e : tags.entrySet()) {
        sj.add(e.getKey() + "=" + e.getValue());
      }
      sb.append(sj).append("}");
    }
    setMetricId(DigestUtils.md5Hex(sb.toString().getBytes()));
    return this;
  }

  public String getMetricIdWithEventTime() {
    return metricId + "_" + getEventTime();
  }

  public InternalMetric removeTag(String k) {
    tags.remove(k);
    return this;
  }

  public InternalMetric putTag(String k, Object v) {
    tags.put(k, v);
    return this;
  }

  public InternalMetric putExpr(String k, Double v) {
    values.put(k, v);
    return this;
  }

  public InternalMetric putAggrExpress(String k, String v) {
    aggrExpresses.put(k, v);
    return this;
  }

  public InternalMetric setValue(Double d) {
    this.value = d;
    return this;
  }

  // global_mandatory_tag_item_rate{page_family='BID',timestamp='2021-03-30 18:00'} 0.712
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(metricName).append("{");
    StringJoiner sj = new StringJoiner(",");
    tags.forEach((k, v) -> sj.add("t-" + k + "=" + v));
    values.forEach((k, v) -> sj.add("e-" + k + "=" + v));
    sj.add("window" + "=" + window);
    sj.add("partition" + "=" + partition);
    sj.add("tagId" + "=" + metricId);
    sj.add("eventTime" + "=" + DateUtils.format(eventTime));
    sb.append(sj).append("}").append(" ").append(value);
    return sb.toString();
  }


  public Map<String, Object> toIndexRequest(Long processTime) {
    Map<String, Object> json = new HashMap<>();
    json.put("metric_key", getMetricName());
    json.put("event_time", getEventTime());
    json.put("event_time_fmt", new Date(getEventTime()));
    json.put("process_time", new Date(processTime));
    if (MapUtils.isNotEmpty(getTags())) {
      json.put("tags", getNonTags());
    }
    json.put("expr", getDoubleValues());
    // json.put("value", element.getValue());
    return json;
  }

  public TdqMetric toTdqMetric(String producerId, int schemaId) {
    final Builder builder = TdqMetric.newBuilder();
    builder.setRheosHeader(RheosHeader.newBuilder()
        .setEventCreateTimestamp(System.currentTimeMillis())
        .setEventSentTimestamp(System.currentTimeMillis())
        .setSchemaId(schemaId)
        .setEventId(null)
        .setProducerId(producerId)
        .build());

    return builder
        .setTags(getNonTags())
        .setValues(getDoubleValues())
        .setMetricName(getMetricName())
        .setMetricId(getMetricId())
        .setProcessTime(System.currentTimeMillis())
        .setEventTime(getEventTime())
        .build();

  }

  @NotNull
  private Map<String, String> getNonTags() {
    Map<String, String> tags = new HashMap<>();
    getTags().forEach((k, v) -> {
      if (v != null && k != null) {
        tags.put(k, v.toString());
      }
    });
    return tags;
  }

  @NotNull
  private Map<String, Double> getDoubleValues() {
    Map<String, Double> values = new HashMap<>();
    getValues().forEach((k, v) -> {
      if (v != null) {
        values.put(k, Double.valueOf(v.toString()));
      } else {
        values.put(k, 0d);
      }
    });
    return values;
  }
}
