package com.ebay.tdq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author xiaoding
 * @since 2021/7/14 12:43 AM
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TdqMtrcQryParam extends TdqQryParam {
    List<String> tags;
    List<String> tagMetrics;
    String pageFmy;
    String siteId;
    List<String> excludePgFmys;
}
