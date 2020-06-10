package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SourceDataStreamBuilder {

  public static <T> DataStream<T> build(StreamExecutionEnvironment environment,
      String topic, String brokers, String groupId, DataCenter dc, Integer parallelism,
      String slotGroup, Class<T> tClass) {

    DataStream<T> sourceDataStream = environment
        .addSource(KafkaSourceFunction.buildSource(topic, brokers, groupId, tClass))
        .setParallelism(parallelism)
        .slotSharingGroup(slotGroup)
        .name(String.format("Rheos Kafka Consumer From DC: %s, Topic: %s", dc, topic))
        .uid(String.format("source-%s-%s-id", dc, topic));

    return sourceDataStream;
  }

  public static DataStream<BotSignature> buildForSignature(StreamExecutionEnvironment environment,
      String topic, String groupId, Integer parallelism) {

    DataStream<BotSignature> sourceDataStream = environment
        .addSource(KafkaSourceFunction.buildSource(
            topic,
            FlinkEnvUtils.getString(Property.KAFKA_COMMON_CONSUMER_BROKERS_DEFAULT),
            groupId,
            BotSignature.class))
        .setParallelism(parallelism)
        .slotSharingGroup(null)
        .name(String.format("Rheos Kafka Consumer From RNO, Topic: %s", topic))
        .uid(String.format("source-%s-id", topic));

    return sourceDataStream;
  }
}
