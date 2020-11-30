package com.ebay.sojourner.flink.connector.kafka;

import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;

public class KafkaConsumerFactory {

  private final KafkaConsumerConfig config;

  public KafkaConsumerFactory(KafkaConsumerConfig config) {
    this.config = config;
  }

  public <T> FlinkKafkaConsumer<T> getConsumer(KafkaDeserializationSchema<T> deserializer) {

    FlinkKafkaConsumer<T> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
        config.getTopicList(),
        deserializer,
        config.getProperties());

    if (config.getOutOfOrderlessInMin() > 0) {

      flinkKafkaConsumer.assignTimestampsAndWatermarks(
          WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofMinutes(3))
              .withTimestampAssigner(new SojSerializableTimestampAssigner())
              .withIdleness(Duration.ofMinutes(10)));
      /*
      flinkKafkaConsumer.assignTimestampsAndWatermarks(
          new SojBoundedOutOfOrderlessTimestampExtractor<>(
              Time.minutes(config.getOutOfOrderlessInMin())));
              */
    }

    if (config.getFromTimestamp() > 0) {
      flinkKafkaConsumer.setStartFromTimestamp(config.getFromTimestamp());
    } else {
      flinkKafkaConsumer.setStartFromLatest();
    }

    return flinkKafkaConsumer;
  }
}
