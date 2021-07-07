package com.ebay.tdq.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class QueryDropdownResult extends TdqResult {
  private QueryDropdownParam param;
  @Singular
  private List<Record> records;

  @Data
  @Builder
  public static class Record {
    private String name;
    @Singular
    private List<String> items;
  }

}
