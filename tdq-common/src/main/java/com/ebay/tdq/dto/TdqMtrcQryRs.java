package com.ebay.tdq.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author xiaoding
 * @since 2021/7/14 12:55 AM
 */

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TdqMtrcQryRs extends TdqQryRs {
    Map<String,Map<String,Object>> tagMetrics;
}
