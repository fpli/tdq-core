package com.ebay.tdq.rules;

import com.ebay.sojourner.common.model.RawEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntzhang
 */
@Data
@NoArgsConstructor
public class TdqSampleData {
  private long processTime;
  private String metricKey;
  private String rawEvent;
  private String tdqMetric;
  private String metricConfig;

  public TdqSampleData(RawEvent rawEvent, TdqMetric tdqMetric, String metricKey, String metricConfig) {
    this.processTime  = System.currentTimeMillis();
    this.rawEvent     = rawEvent.toString();
    this.metricKey    = metricKey;
    this.tdqMetric    = tdqMetric.toString();
    this.metricConfig = metricConfig;
  }
}
