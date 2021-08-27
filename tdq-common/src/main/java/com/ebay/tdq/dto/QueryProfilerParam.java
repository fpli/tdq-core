package com.ebay.tdq.dto;

import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author juntzhang
 */
@Data
@RequiredArgsConstructor
public class QueryProfilerParam {
  private final String tdqConfig;
  private final long from;
  private final long to;
  private final Map<String, Set<String>> dimensions;
}
