package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagSumMetrics implements Serializable {
    private MetricType metricType;
    private String metricName;
    private Map<String,Map<String,Double>> tagSumMap;
}
