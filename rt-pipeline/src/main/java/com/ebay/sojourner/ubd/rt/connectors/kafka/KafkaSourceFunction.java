package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojBytesEvent;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumerBase generateWatermark(String topic, String brokers,
      String groupId, Class<T> tClass) {

    FlinkKafkaConsumerBase flinkKafkaConsumerBase
        = initKafkaConsumer(topic, brokers, groupId, tClass);

    if (tClass.isAssignableFrom(SojBytesEvent.class)) {
      return flinkKafkaConsumerBase;
    } else {
      return flinkKafkaConsumerBase
          .assignTimestampsAndWatermarks(
              new SobBoundedOutOfOrderlessTimestampExtractor(Time.seconds(10)));
    }
  }

  private static <T> FlinkKafkaConsumerBase initKafkaConsumer(String topic, String brokers,
      String groupId, Class<T> tClass) {
    return KafkaConnectorFactory
        .createKafkaConsumer(topic, brokers, groupId, tClass)
        .setStartFromLatest();
  }
}
