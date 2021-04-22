package com.ebay.tdq.sample;

import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.functions.TDQMetricSourceFunction;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author juntzhang
 */
public class TDQMetricJobTest {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<TdqMetric> src1 = env
                .addSource(new TDQMetricSourceFunction())
                .name("TDQ Metric Example Source1")
                .uid("tdq_metric_example_source1");
        DataStream<TdqMetric> src2 = env
                .addSource(new TDQMetricSourceFunction())
                .name("TDQ Metric Example Source2")
                .uid("tdq_metric_example_source2");

        DataStream<TdqMetric> windowCounts = src1
                .union(src2)
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                                .withTimestampAssigner((SerializableTimestampAssigner<TdqMetric>) (event,
                                        timestamp) -> event.getEventTime())
                                .withIdleness(Duration.ofSeconds(3))
                )
                .slotSharingGroup("metrics-collector-post")
                .keyBy(TdqMetric::getUid)
                .timeWindow(Time.seconds(5))
                .aggregate(new TdqAggregateFunction());

        windowCounts.print().setParallelism(2);

        env.execute("TDQMetric Job Test");
    }
}
