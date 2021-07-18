package com.ebay.tdq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author xiaoding
 * @since 2021/7/12 3:47 PM
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TdqQryParam implements Serializable {
    String metricName;
    String metricType;
    Long from;
    Long to;
    int precision;
    AggMethod aggMethod;
    MetricsMethod metricsMethod;
}
