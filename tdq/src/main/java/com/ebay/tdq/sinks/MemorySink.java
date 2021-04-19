package com.ebay.tdq.sinks;

import com.ebay.tdq.rules.TdqMetric;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @author juntzhang
 */
public class MemorySink extends RichSinkFunction<TdqMetric> {

}
