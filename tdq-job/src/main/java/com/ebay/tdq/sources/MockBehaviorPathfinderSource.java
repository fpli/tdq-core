package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.flink.connector.kafka.SojSerializableTimestampAssigner;
import com.ebay.tdq.functions.RawEventSourceMockFunction;
import com.google.common.collect.Lists;
import java.time.Duration;
import java.util.List;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author juntzhang
 */
public class MockBehaviorPathfinderSource extends AbstractSource {
  public static List<DataStream<RawEvent>> build(final StreamExecutionEnvironment env) {
    SingleOutputStreamOperator<RawEvent> src1 = env.addSource(new RawEventSourceMockFunction())
        .name("Raw Event Src1")
        .uid("raw-event-src1")
        .slotSharingGroup("src1")
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<RawEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SojSerializableTimestampAssigner<>())
                .withIdleness(Duration.ofSeconds(1))
        )
        .slotSharingGroup("src1")
        .name("Raw Event Watermark Src1")
        .uid("raw-event-watermark-src1")
        .slotSharingGroup("src1");

    SingleOutputStreamOperator<RawEvent> src2 = env.addSource(new RawEventSourceMockFunction())
        .name("Raw Event Src2")
        .uid("raw-event-src2")
        .slotSharingGroup("src2")
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<RawEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SojSerializableTimestampAssigner<>())
                .withIdleness(Duration.ofSeconds(1))
        )
        .slotSharingGroup("src2")
        .name("Raw Event Watermark Src2")
        .uid("raw-event-watermark-src2")
        .slotSharingGroup("src2");

    SingleOutputStreamOperator<RawEvent> src3 = env.addSource(new RawEventSourceMockFunction())
        .name("Raw Event Src3")
        .uid("raw-event-src3")
        .slotSharingGroup("src3")
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<RawEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SojSerializableTimestampAssigner<>())
                .withIdleness(Duration.ofSeconds(1))
        )
        .slotSharingGroup("src3")
        .name("Raw Event Watermark Src3")
        .uid("raw-event-watermark-src3")
        .slotSharingGroup("src3");

    SingleOutputStreamOperator<RawEvent> src4 = env.addSource(new RawEventSourceMockFunction())
        .name("Raw Event Src4")
        .uid("raw-event-src4")
        .slotSharingGroup("src4")
        .assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<RawEvent>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner(new SojSerializableTimestampAssigner<>())
                .withIdleness(Duration.ofSeconds(1))
        )
        .slotSharingGroup("src4")
        .name("Raw Event Watermark Src4")
        .uid("raw-event-watermark-src4")
        .slotSharingGroup("src4");

    return Lists.newArrayList(
        sample(src1, "src1", "1", 1),
        sample(src2, "src2", "2", 1),
        sample(src3, "src3", "3", 1),
        sample(src4, "src4", "4", 1));
  }
}
