package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.schema.AvroKeyedSerializationSchema;
import com.ebay.sojourner.flink.connector.kafka.schema.SojEventKeyedSerializationSchema;
import java.util.Optional;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaProducerFactory {

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers,
      String messagekey, Class<T> tClass) {

    Properties producerConfig = getKafkaProducerConfig(brokers);

    return new FlinkKafkaProducer<>(topic,
        new AvroKeyedSerializationSchema<>(tClass, messagekey), producerConfig,
        Optional.of(new SojKafkaPartitioner<>()));
  }

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers,
      String messagekey, String subject) {

    Properties producerConfig = getKafkaProducerConfig(brokers);

    if (tClass == null && messagekey == null) {
      return new FlinkKafkaProducer<>(topic, new SojBytesEventSerializationSchema<>(),
          producerConfig, Optional.of(new SojKafkaPartitioner<>()));
    } else {
      return new FlinkKafkaProducer<>(topic,
          new AvroKeyedSerializationSchema<>(tClass, messagekey), producerConfig,
          Optional.of(new SojKafkaPartitioner<>()));
    }
  }

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers,
      String key1, String key2, Class<T> tClass) {

    Properties producerConfig = getKafkaProducerConfig(brokers);

    return new FlinkKafkaProducer<>(topic,
        new SojEventKeyedSerializationSchema<>(tClass, key1, key2), producerConfig,
        Optional.of(new SojKafkaPartitioner<>()));
  }

  private static Properties getKafkaProducerConfig(String brokers) {

    Properties producerConfig = KafkaConnectorFactory.getKafkaCommonConfig();

    producerConfig
        .put(ProducerConfig.BATCH_SIZE_CONFIG, FlinkEnvUtils.getInteger(Property.BATCH_SIZE));
    producerConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
        FlinkEnvUtils.getInteger(Property.REQUEST_TIMEOUT_MS));
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    producerConfig
        .put(ProducerConfig.RETRIES_CONFIG, FlinkEnvUtils.getInteger(Property.REQUEST_RETRIES));
    producerConfig
        .put(ProducerConfig.LINGER_MS_CONFIG, FlinkEnvUtils.getInteger(Property.LINGER_MS));
    producerConfig
        .put(ProducerConfig.BUFFER_MEMORY_CONFIG, FlinkEnvUtils.getInteger(Property.BUFFER_MEMORY));
    producerConfig
        .put(ProducerConfig.ACKS_CONFIG, String.valueOf(FlinkEnvUtils.getInteger(Property.ACKS)));
    producerConfig.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,
        FlinkEnvUtils.getString(Property.COMPRESSION_TYPE));

    return producerConfig;
  }
}
