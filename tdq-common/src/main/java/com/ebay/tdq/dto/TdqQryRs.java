package com.ebay.tdq.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author xiaoding
 * @since 2021/7/14 12:51 AM
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TdqQryRs implements Serializable {
    String metricName;
    String metricType;
    Long from;
    Long to;
    AggMethod aggMethod;
}
