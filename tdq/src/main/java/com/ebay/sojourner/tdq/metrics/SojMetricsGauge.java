package com.ebay.sojourner.tdq.metrics;

import java.util.Map;
import org.apache.flink.metrics.Gauge;

public class SojMetricsGauge implements Gauge<Object> {
    private Map<String, Object> sojMetricsMap;
    private String              metricKey;

    public SojMetricsGauge(Map<String, Object> sojMetricsMap, String metricKey) {
        this.sojMetricsMap = sojMetricsMap;
        this.metricKey     = metricKey;
    }

    public Object getValue() {
        return sojMetricsMap.get(metricKey);
    }
}