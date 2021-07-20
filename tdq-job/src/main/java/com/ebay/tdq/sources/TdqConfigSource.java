package com.ebay.tdq.sources;

import com.ebay.tdq.functions.TdqConfigSourceFunction;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.utils.TdqEnv;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author juntzhang
 */
public class TdqConfigSource {
  public static DataStream<PhysicalPlans> build(final StreamExecutionEnvironment env, TdqEnv tdqEnv) {
    return env
        .addSource(new TdqConfigSourceFunction(tdqEnv))
        .name("Tdq Config Source")
        .uid("tdq-config-source")
        .assignTimestampsAndWatermarks(WatermarkStrategy
            .<PhysicalPlans>forBoundedOutOfOrderness(Duration.ofMinutes(0))
            .withIdleness(Duration.ofSeconds(1)))
        .setParallelism(1)
        .name("Tdq Config Watermark Source")
        .uid("tdq-config-watermark-source");
  }
}
