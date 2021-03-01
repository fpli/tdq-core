package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagMissingCntMetric implements Serializable {
    private MetricType metricType;
    private String metricName;
    private String pageFamily;
    private Map<String,Long> tagCntMap;

}
