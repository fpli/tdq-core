package com.ebay.sojourner.business.ubd.metric;

import lombok.Data;

@Data
public class MetricsTestCase {

  private String name;
  private MetricsTestInputObjects inputs;
  private MetricsTestExpect expect;
}
