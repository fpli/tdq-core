package com.ebay.sojourner.rt.pipeline;

import com.ebay.sojourner.common.model.SojBytesEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.KafkaSourceFunction;
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
    DataStream<SojBytesEvent> bytesDataStreamForRNO =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Property.KAFKA_CONSUMER_BOOTSTRAP_SERVERS_RNO),
                    FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_GROUP_ID),
                    SojBytesEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For RNO")
            .uid("source-rno-id");

    DataStream<SojBytesEvent> bytesDataStreamForSLC =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Property.KAFKA_CONSUMER_BOOTSTRAP_SERVERS_SLC),
                    FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_GROUP_ID),
                    SojBytesEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For SLC")
            .uid("source-slc-id");

    DataStream<SojBytesEvent> bytesDataStreamForLVS =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Property.KAFKA_CONSUMER_BOOTSTRAP_SERVERS_LVS),
                    FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_GROUP_ID),
                    SojBytesEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For LVS")
            .uid("source-lvs-id");

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
        .addSink(KafkaConnectorFactory.createKafkaProducerForCopy(
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_TOPIC),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS_RNO)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("RawEvent")
        .uid("event-sink-id");

    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.NAME_DATA_QUALITY));
  }
}
