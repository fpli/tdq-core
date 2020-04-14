package com.ebay.sojourner.ubd.rt.connectors.kafka;

import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunctionForLoad {

  public static <T> FlinkKafkaConsumerBase generateWatermark(String topic, String brokers,
      String groupId, Class<T> tClass) {
    return KafkaConnectorFactoryForLoad
        .createKafkaConsumer(topic,brokers,groupId, tClass)
        .assignTimestampsAndWatermarks(
            new SojBoundedOutOfOrdernessTimestampExtractorForLoad(Time.seconds(10)));
  }
}
