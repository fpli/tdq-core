package com.ebay.tdq.sources;

import com.ebay.tdq.functions.TdqConfigSourceFunction;
import com.ebay.tdq.rules.PhysicalPlan;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author juntzhang
 */
public class TdqConfigSource {
  public static DataStream<PhysicalPlan> build(final StreamExecutionEnvironment env) {
    return env
        .addSource(new TdqConfigSourceFunction(30L))
        .name("Tdq Config Source")
        .uid("tdq-config-source")
        .assignTimestampsAndWatermarks(WatermarkStrategy
            .<PhysicalPlan>forBoundedOutOfOrderness(Duration.ofMinutes(0))
            .withIdleness(Duration.ofSeconds(1)))
        .setParallelism(1)
        .name("Tdq Config Watermark Source")
        .uid("tdq-config-watermark-source");
  }
}
