package com.ebay.tdq.dto;

import com.ebay.tdq.config.TdqConfig;
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
  private final TdqConfig tdqConfig;
  private final String metricName;
  private final long from;
  private final long to;
  private final String window;
  private final Map<String, Set<String>> dimensions;
}
