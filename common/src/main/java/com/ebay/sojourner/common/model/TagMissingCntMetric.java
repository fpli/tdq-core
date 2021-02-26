package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@Data
public class TagMissingCntMetric implements Serializable {
    private int metricType;
    private String metricName;
    private String pageFamily;
    private Map<String,Long> tagCntMap;

}
