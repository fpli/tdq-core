package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagMissingCntMetrics implements Serializable {
    private MetricType metricType;
    private String metricName;
    private Set<String> pageFamilySet;
    private Map<String,Map<String,Long>> tagCntMap;

}
