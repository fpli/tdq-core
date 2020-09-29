package com.ebay.sojourner.flink.connector.kafka;

import java.util.Set;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumer<T> buildSourceForRealtime(KafkaConsumerConfig config,
      Class<T> tClass) {

    FlinkKafkaConsumer<T> kafkaConsumer = KafkaConsumerFactory.getConsumer(config, tClass);

    kafkaConsumer.assignTimestampsAndWatermarks(
        new SojBoundedOutOfOrderlessTimestampExtractor<>(Time.minutes(3)));

    return kafkaConsumer;
  }

  public static <T> FlinkKafkaConsumer<T> buildSourceForRealtime(KafkaConsumerConfig config,
      Class<T> tClass, Set<String> guidSet) {

    FlinkKafkaConsumer<T> kafkaConsumer = KafkaConsumerFactory
        .getConsumer(config, tClass, guidSet);

    kafkaConsumer.assignTimestampsAndWatermarks(
        new SojBoundedOutOfOrderlessTimestampExtractor<>(Time.minutes(3)));

    return kafkaConsumer;
  }

  public static <T> FlinkKafkaConsumer<T> buildSourceForDumper(KafkaConsumerConfig config,
      Class<T> tClass) {

    FlinkKafkaConsumer<T> kafkaConsumer = KafkaConsumerFactory.getConsumer(config, tClass);

    return kafkaConsumer;
  }
}
