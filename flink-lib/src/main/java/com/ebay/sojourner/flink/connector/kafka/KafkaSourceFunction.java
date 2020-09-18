package com.ebay.sojourner.flink.connector.kafka;

import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumer<T> buildSource(KafkaConsumerConfig config, Class<T> tClass) {

    FlinkKafkaConsumer<T> kafkaConsumer = KafkaConsumerFactory.getConsumer(config, tClass);

    kafkaConsumer.assignTimestampsAndWatermarks(
        new SojBoundedOutOfOrderlessTimestampExtractor<>(Time.minutes(3)));

    return kafkaConsumer;
  }
}
