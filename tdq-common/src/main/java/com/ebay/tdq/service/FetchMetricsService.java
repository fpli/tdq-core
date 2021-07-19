package com.ebay.tdq.service;

import com.ebay.tdq.dto.TdqQryParam;
import com.ebay.tdq.dto.TdqQryRs;

import java.io.IOException;

/**
 * @author xiaoding
 * @since 2021/7/11 4:02 PM
 */
@Deprecated
public interface FetchMetricsService<in extends TdqQryParam,out extends TdqQryRs> {
    out fetchMetrics(in input) throws IOException;

}
