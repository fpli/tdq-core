package com.ebay.sojourner.flink.connector.kafka;

import lombok.AllArgsConstructor;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;

@AllArgsConstructor
public class KafkaSourceFunctionBuilder<T> {

  private final KafkaConsumerConfig config;

  public FlinkKafkaConsumer<T> build(DeserializationSchema<T> deserializer) {

    FlinkKafkaConsumer<T> kafkaConsumer = KafkaConsumerFactory.getConsumer(deserializer, config);

    kafkaConsumer.assignTimestampsAndWatermarks(
        new SojBoundedOutOfOrderlessTimestampExtractor<>(Time.minutes(3)));

    return kafkaConsumer;
  }

  public FlinkKafkaConsumer<T> build(KafkaDeserializationSchema<T> deserializer) {

    FlinkKafkaConsumer<T> kafkaConsumer = KafkaConsumerFactory.getConsumer(deserializer, config);

    kafkaConsumer.assignTimestampsAndWatermarks(
        new SojBoundedOutOfOrderlessTimestampExtractor<>(Time.minutes(3)));

    return kafkaConsumer;
  }
}
