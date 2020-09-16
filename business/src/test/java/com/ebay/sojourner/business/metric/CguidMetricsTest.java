package com.ebay.sojourner.business.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.apache.datasketches.hll.HllSketch;
import org.apache.datasketches.hll.TgtHllType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class CguidMetricsTest extends BaseMetricsTest {

  private CguidMetrics cguidMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    cguidMetrics = new CguidMetrics();
    yaml = loadTestCasesYaml("CguidMetricsTest.yaml");
    int lgK = 12; //This is log-base2 of k, so k = 4096. lgK can be from 4 to 21
    HllSketch sketch = new HllSketch(lgK, TgtHllType.HLL_8);
    long n = 100;
    for (int i = 0; i < n; i++) {
      sketch.update(i);
    }

    for (int i = 100; i < 200; i++) {
      sketch.update(i);
    }
    System.out.println(sketch.getEstimate());
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, cguidMetrics);
  }
}
