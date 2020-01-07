package com.ebay.sojourner.ubd.common.util;

import lombok.Data;

@Data
public class MetricsTestCase {
    private String name;
    private MetricsTestInputObjects inputs;
    private MetricsTestExpect expect;
}
