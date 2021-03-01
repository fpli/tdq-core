package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RawEventMetrics implements Serializable {
    private String guid;
    private Map<String,TagMissingCntMetrics> tagMissingCntMetricsMap ;
    private Map<String,TagSumMetrics> tagSumMetricsMap ;
    private Map<String,PageCntMetrics> pageCntMetricsMap ;
    private Map<String,TransformErrorMetrics> transformErrorMetricsMap ;
}