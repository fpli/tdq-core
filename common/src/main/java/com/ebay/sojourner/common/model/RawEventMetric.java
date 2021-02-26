package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RawEventMetric implements Serializable {
    private String guid;
    private TagMissingCntMetric tagMissingCntMetric;
    private TagSumMetric tagSumMetric;
    private PageCntMetric pageCntMetric;
    private TransformErrorMetric transformErrorMetric;
}