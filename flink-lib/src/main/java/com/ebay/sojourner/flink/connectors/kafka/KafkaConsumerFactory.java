package com.ebay.sojourner.flink.connectors.kafka;

import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

public class KafkaConsumerFactory<T> extends AbstractKafkaConnectorFactory<T> {

  @Override
  public FlinkKafkaConsumer<T> getConsumer(String topic, Properties config, Class<T> tClass) {

    FlinkKafkaConsumer<T> flinkKafkaConsumer = new FlinkKafkaConsumer(
        topic,
        DeserializationSchemaManager.getSchema(tClass),
        config
    );

    flinkKafkaConsumer.setStartFromLatest();
    return flinkKafkaConsumer;
  }

  @Override
  public FlinkKafkaProducer<T> getProducer() {
    //TODO(Jason): to be implemented
    return null;
  }
}
