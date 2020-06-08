package com.ebay.sojourner.flink.connectors.kafka;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SourceDataStreamBuilder {

  public static <T> DataStream<T> build(StreamExecutionEnvironment environment,
      String topic, String brokers, String groupId, String dc, Integer parallelism,
      String slotGroup, Class<T> tClass) {

    DataStream<T> sourceDataStream = environment
        .addSource(KafkaSourceFunction.buildSource(topic, brokers, groupId, tClass))
        .setParallelism(parallelism)
        .slotSharingGroup(slotGroup)
        .name(String.format("Rheos Kafka Consumer From DC: %s, Topic: %s", dc, topic))
        .uid(String.format("source-%s-%s-id", dc, topic));

    return sourceDataStream;
  }
}
