package com.ebay.tdq.dto;

import lombok.AllArgsConstructor;

/**
 * @author xiaoding
 * @since 2021/7/18 5:04 PM
 */
@AllArgsConstructor
public enum AggMethod {
    SUM(0,"sum") ,
    AVG(1,"average"),
    COUNT(2,"count");
    private final int id;
    private final String desc;


}
