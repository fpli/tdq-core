package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@Data
public class TransformErrorMetric implements Serializable {
    private int metricType;
    private String metricName;
    private Map<String,Long> tagCntMap;
}
