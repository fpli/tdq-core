package com.ebay.tdq.dto;

import lombok.AllArgsConstructor;

/**
 * @author xiaoding
 * @since 2021/7/18 5:04 PM
 */
@AllArgsConstructor
public enum MetricsMethod {
    PERCENTAGE(0,"percetage") ,
    COUNT(1,"count");
    private final int id;
    private final String desc;


}
