package com.ebay.sojourner.flink.connectors.kafka;

import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaConsumerFactory<T> {

  public FlinkKafkaConsumer<T> getConsumer(String topic, Properties config, Class<T> tClass) {

    FlinkKafkaConsumer<T> flinkKafkaConsumer = new FlinkKafkaConsumer(
        topic,
        DeserializationSchemaManager.getSchema(tClass),
        config
    );

    flinkKafkaConsumer.setStartFromLatest();
    return flinkKafkaConsumer;
  }
}
