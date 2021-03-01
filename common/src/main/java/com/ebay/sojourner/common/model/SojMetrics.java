package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.map.HashedMap;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SojMetrics implements Serializable {
    private Map<String, TagMissingCntMetrics> tagMissingCntMetricsMap = new HashedMap();
    private Map<String, TagSumMetrics> tagSumMetricsMap = new HashedMap();
    private Map<String, PageCntMetrics> pageCntMetricsMap = new HashedMap();
    private Map<String, TransformErrorMetrics> transformErrorMetricsMap = new HashedMap();
}