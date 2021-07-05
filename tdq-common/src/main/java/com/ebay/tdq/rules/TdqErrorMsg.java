package com.ebay.tdq.rules;

import com.ebay.sojourner.common.model.RawEvent;
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
  private String rawEvent;
  private String exception;

  public TdqErrorMsg(RawEvent rawEvent, Exception e, String metricKey) {
    this.processTime = System.currentTimeMillis();
    this.rawEvent    = rawEvent.toString();
    this.metricKey   = metricKey;
    this.exception   = e.toString();
  }
}
