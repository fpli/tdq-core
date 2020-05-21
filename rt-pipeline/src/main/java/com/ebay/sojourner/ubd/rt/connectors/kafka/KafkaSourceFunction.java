package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojBytesEvent;
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
    if (groupId.contains("copy")) {
      return kafkaConsumer.setStartFromEarliest();
    } else if (groupId.contains("cross")) {
      // 2020-05-14, 23:40:12
      return kafkaConsumer.setStartFromTimestamp(1589470812000L);
    } else {
      return kafkaConsumer.setStartFromLatest();
    }
  }
}
