package com.ebay.sojourner.ubd.rt.connectors.kafka;

import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumerBase generateWatermark(String topic, String brokers,
      String groupId, Class<T> tClass) {
    return KafkaConnectorFactory
        .createKafkaConsumer(topic, brokers, groupId, tClass)
        .setStartFromLatest()
        .assignTimestampsAndWatermarks(
            new SobBoundedOutOfOrderlessTimestampExtractor(Time.seconds(10)));
  }
}
