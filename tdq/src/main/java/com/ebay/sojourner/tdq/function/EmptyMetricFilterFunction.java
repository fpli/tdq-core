package com.ebay.sojourner.tdq.function;

import com.ebay.sojourner.common.model.RawEventMetrics;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class EmptyMetricFilterFunction extends RichFilterFunction<RawEventMetrics> {

    @Override
    public boolean filter(RawEventMetrics rawEventMetrics) throws Exception {
        return rawEventMetrics.getPageCntMetricsMap().size() != 0 ||
                rawEventMetrics.getTagMissingCntMetricsMap().size() != 0 ||
                rawEventMetrics.getTransformErrorMetricsMap().size() != 0 ||
                rawEventMetrics.getTagSumMetricsMap().size() != 0;
    }
}