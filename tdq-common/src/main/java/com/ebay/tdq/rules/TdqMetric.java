package com.ebay.tdq.rules;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author juntzhang
 */
@AllArgsConstructor
@Data
public class TdqMetric implements Serializable {
  private String uid;      // metricKey + tags
  private Integer partition = -1;
  private Long window;      // seconds
  private String metricKey;
  private Object physicalPlan;
  private Map<String, Object> tags = Maps.newTreeMap();
  private Long eventTime;
  private Map<String, Object> exprMap = Maps.newHashMap();
  private Double value = 0d;

  public TdqMetric() {
  }

  public TdqMetric(String metricKey, Long eventTime) {
    this.metricKey = metricKey;
    this.eventTime = eventTime;
  }

  public TdqMetric copy() {
    return (TdqMetric) SerializationUtils.clone(this);
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
    setUid(DigestUtils.md5Hex(sb.toString().getBytes()));
    return this;
  }

  public String getCacheId() {
    // 1:01:01 => 1:01:59
    // 1:01:59 => 1:01:59
    return uid + "_" + getEventTime() / 1000 / 60 * 60 + 59;
  }

  public TdqMetric putTag(String k, Object v) {
    tags.put(k, v);
    return this;
  }

  public TdqMetric putExpr(String k, Double v) {
    exprMap.put(k, v);
    return this;
  }

  public TdqMetric putExpr2(String k, Object v) {
    exprMap.put(k, v);
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
    exprMap.forEach((k, v) -> sj.add("e-" + k + "=" + v));
    sj.add("window" + "=" + window);
    sj.add("partition" + "=" + partition);
    sj.add("uid" + "=" + uid);
    sj.add("eventTime" + "=" + FastDateFormat
        .getInstance("yyyy-MM-dd HH:mm:ss").format(eventTime));
    sb.append(sj).append("}").append(" ").append(value);
    return sb.toString();
  }
}
