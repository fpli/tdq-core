package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.LVS;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.common.metrics.PipelineMetricsCollectorProcessFunction;
import com.ebay.sojourner.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.rt.operators.event.RawEventFilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerRTJobForHotDeploy {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // for soj nrt output
    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic(RNO/LVS/SLC)
    // 1.2 Assign timestamps and emit watermarks.
    SourceDataStreamBuilder<RawEvent> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, RawEvent.class
    );

    DataStream<RawEvent> rawEventDataStream = dataStreamBuilder.buildOfDC(LVS);

    // filter 5% throughput group by guid for reduce kafka consumer lag
    DataStream<RawEvent> filteredRawEvent = rawEventDataStream
        .filter(new RawEventFilterFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.EVENT_PARALLELISM))
        .name("RawEvent Filter Operator")
        .uid("event-filter-id");

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStream = filteredRawEvent
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.EVENT_PARALLELISM))
        .name("Event Operator")
        .uid("event-id");

    // event metrics collector
    ubiEventDataStream
        .process(new PipelineMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.METRICS_PARALLELISM))
        .name("Event Metrics Collector")
        .uid("event-metrics-id");

    // Submit this job
    FlinkEnvUtils.execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}

