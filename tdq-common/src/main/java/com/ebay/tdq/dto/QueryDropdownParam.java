package com.ebay.tdq.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author juntzhang
 */
@Data
@RequiredArgsConstructor
public class QueryDropdownParam {
  private final String tdqConfig;
  private final long from;
  private final long to;
}
