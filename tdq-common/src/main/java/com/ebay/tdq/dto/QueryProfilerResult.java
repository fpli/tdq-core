package com.ebay.tdq.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * @author juntzhang
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class QueryProfilerResult extends TdqResult {
  private QueryProfilerParam param;
  @Singular
  private List<Record> records;

  @Data
  @RequiredArgsConstructor
  public static class Record {
    private final long timestamp;
    private final double value;
  }

}
