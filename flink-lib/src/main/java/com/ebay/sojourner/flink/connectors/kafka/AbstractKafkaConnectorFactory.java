package com.ebay.sojourner.flink.connectors.kafka;

import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

public abstract class AbstractKafkaConnectorFactory<T> {

  public abstract FlinkKafkaConsumer<T> getConsumer(
      String topic, Properties config, Class<T> tClass);

  public abstract FlinkKafkaProducer<T> getProducer() ;
}
