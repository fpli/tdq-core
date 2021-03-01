package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SojMetric implements Serializable {
    private Map<String, TagMissingCntMetrics> tagMissingCntMetricMap;
    private Map<String, TagSumMetrics> tagSumMetricMap;
    private Map<String, PageCntMetrics> pageCntMetricMap;
    private Map<String, TransformErrorMetrics> transformErrorMetricMap;
}