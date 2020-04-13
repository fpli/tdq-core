package com.ebay.sojourner.ubd.rt.connectors.kafka;

import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunctionForLoad {

  public static <T> FlinkKafkaConsumerBase generateWatermarkForSession(String topic, String brokers,
      String groupId, Class<T> tClass) {
    return KafkaConnectorFactoryForLoad
        .createKafkaConsumer(topic,brokers,groupId, tClass)
        .setStartFromEarliest()
        .assignTimestampsAndWatermarks(
            new SojSessionBoundedOutOfOrdernessTimestampExtractor(Time.seconds(10)));
  }

  public static <T> FlinkKafkaConsumerBase generateWatermarkForEvent(String topic, String brokers,
      String groupId, Class<T> tClass) {
    return KafkaConnectorFactoryForLoad
        .createKafkaConsumer(topic,brokers,groupId, tClass)
        .setStartFromEarliest()
        .assignTimestampsAndWatermarks(
            new SojEventBoundedOutOfOrdernessTimestampExtractor(Time.seconds(10)));
  }
}
