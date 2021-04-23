package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.flink.connector.kafka.SojSerializableTimestampAssigner;
import com.ebay.tdq.functions.MockTdqRawEventSourceFunction;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author juntzhang
 */
public class MockBehaviorPathfinderSource {
  public static DataStream<RawEvent> build(final StreamExecutionEnvironment env) {
    SingleOutputStreamOperator<RawEvent> src1 = env.addSource(new MockTdqRawEventSourceFunction())
        .name("Raw Event Src1")
        .uid("raw-event-src1")
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<RawEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SojSerializableTimestampAssigner<>())
                .withIdleness(Duration.ofSeconds(1))
        )
        .name("Raw Event Watermark Src1")
        .uid("raw-event-watermark-src1")
        .slotSharingGroup("src1");

    SingleOutputStreamOperator<RawEvent> src2 = env.addSource(new MockTdqRawEventSourceFunction())
        .name("Raw Event Src2")
        .uid("raw-event-src2")
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<RawEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SojSerializableTimestampAssigner<>())
                .withIdleness(Duration.ofSeconds(1))
        )
        .name("Raw Event Watermark Src2")
        .uid("raw-event-watermark-src2")
        .slotSharingGroup("src2");

    return src1.union(src2);
  }
}
