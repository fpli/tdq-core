package com.ebay.sojourner.business.ubd.metrics;

import lombok.Data;

@Data
public class MetricsTestCase {

  private String name;
  private MetricsTestInputObjects inputs;
  private MetricsTestExpect expect;
}
