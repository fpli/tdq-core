package com.ebay.tdq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author juntzhang
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
final public class TdqDataResult<T> extends TdqResult {
  private T data;

}
