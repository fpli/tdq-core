package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojBytesEvent;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumerBase generateWatermark(String topic, String brokers,
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
<<<<<<< HEAD
    if(topic.equals("behavior.trafficjam.pathfinder.copy")) {
      return KafkaConnectorFactory
          .createKafkaConsumer(topic, brokers, groupId, tClass)
          .setStartFromEarliest();
    }else{
      return KafkaConnectorFactory
          .createKafkaConsumer(topic, brokers, groupId, tClass)
          .setStartFromLatest();
=======
    FlinkKafkaConsumer kafkaConsumer = KafkaConnectorFactory
        .createKafkaConsumer(topic, brokers, groupId, tClass);
    if (groupId.contains("copy")) {
      return kafkaConsumer.setStartFromEarliest();
    } else {
      return kafkaConsumer.setStartFromLatest();
>>>>>>> 3a09488a63dd22d0273555f1104afd6f4ae272e6
    }
  }
}
