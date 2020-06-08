package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.SojBytesEvent;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumerBase buildSource(String topic, String brokers,
      String groupId, Class<T> tClass) {

    FlinkKafkaConsumerBase<T> flinkKafkaConsumerBase
        = initKafkaConsumer(topic, brokers, groupId, tClass);

    if (tClass.isAssignableFrom(SojBytesEvent.class)) {
      return flinkKafkaConsumerBase;
    } else {
      return flinkKafkaConsumerBase
          .assignTimestampsAndWatermarks(
              new SojBoundedOutOfOrderlessTimestampExtractor(Time.seconds(10)));
    }
  }

  private static <T> FlinkKafkaConsumerBase initKafkaConsumer(String topic, String brokers,
      String groupId, Class<T> tClass) {

    FlinkKafkaConsumer kafkaConsumer = KafkaConnectorFactory
        .createKafkaConsumer(topic, brokers, groupId, tClass);
    if (groupId.contains("copy")
        || groupId.contains("cross") || groupId.contains("dq")) {
      return kafkaConsumer.setStartFromEarliest();
    } else {
      return kafkaConsumer.setStartFromLatest();
    }
  }
}
