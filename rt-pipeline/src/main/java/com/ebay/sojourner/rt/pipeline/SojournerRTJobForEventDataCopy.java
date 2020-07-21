package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.LVS;
import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;
import static com.ebay.sojourner.flink.common.util.DataCenter.SLC;

import com.ebay.sojourner.common.model.SojBytesEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.KafkaProducerFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.operators.event.SojBytesEventFilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerRTJobForEventDataCopy {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // for soj nrt output
    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
    // 1.2 Assign timestamps and emit watermarks.
    SourceDataStreamBuilder<SojBytesEvent> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, SojBytesEvent.class
    );

    DataStream<SojBytesEvent> bytesDataStreamForRNO = dataStreamBuilder.buildOfDC(RNO);
    DataStream<SojBytesEvent> bytesDataStreamForSLC = dataStreamBuilder.buildOfDC(SLC);
    DataStream<SojBytesEvent> bytesDataStreamForLVS = dataStreamBuilder.buildOfDC(LVS);

    DataStream<SojBytesEvent> sojBytesDataStream = bytesDataStreamForLVS
        .union(bytesDataStreamForRNO)
        .union(bytesDataStreamForSLC);

    DataStream<SojBytesEvent> bytesFilterDataStream = sojBytesDataStream
        .filter(new SojBytesEventFilterFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("Bytes Filter")
        .uid("byte-filter-id");

    // sink for session dq
    bytesFilterDataStream
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_TOPIC),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS_RNO),
            null,null))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("RawEvent")
        .uid("event-sink-id");

    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
