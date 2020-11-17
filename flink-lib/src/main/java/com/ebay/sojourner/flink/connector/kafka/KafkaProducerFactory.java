package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.schema.AvroKeyedSerializationSchema;
import com.ebay.sojourner.flink.connector.kafka.schema.SojEventKeyedSerializationSchema;
import com.ebay.sojourner.flink.connector.kafka.schema.SojSerializationSchema;
import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import java.util.Optional;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaProducerFactory {

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers,
      String subject, Class<T> tClass, String... messageKey) {

    Properties producerConfig = getKafkaProducerConfig(brokers);

    if (subject == null) {
      if (messageKey.length > 1) {
        return new FlinkKafkaProducer<>(topic,
            new SojEventKeyedSerializationSchema<>(tClass, messageKey), producerConfig,
            Optional.of(new SojKafkaPartitioner<>()));
      } else if (messageKey.length == 1) {
        return new FlinkKafkaProducer<>(topic,
            new AvroKeyedSerializationSchema<>(tClass, messageKey[0]), producerConfig,
            Optional.of(new SojKafkaPartitioner<>()));
      } else {
        throw new IllegalStateException("kafka message key length less 1");
      }
    } else {
      return new FlinkKafkaProducer(topic,
          new SojSerializationSchema(topic, subject, producerConfig, messageKey), producerConfig,
          Semantic.AT_LEAST_ONCE);
    }
  }

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers, Class<T> tClass,
      String... messagekey) {

    return getProducer(topic, brokers, null, tClass, messagekey);
  }

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers,
      String subject, String... messageKey) {

    return getProducer(topic, brokers, subject, null, messageKey);
  }

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers,
      String messageKey, Class<T> tClass) {

    return getProducer(topic, brokers, null, tClass, messageKey);
  }

  private static Properties getKafkaProducerConfig(String brokers) {

    Properties producerConfig = KafkaCommonConfig.get();
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
    producerConfig.put(Property.PRODUCER_ID,
        FlinkEnvUtils.getString(Property.PRODUCER_ID));
    producerConfig.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, FlinkEnvUtils
        .getString(Property.RHEOS_KAFKA_REGISTRY_URL));
    return producerConfig;
  }
}
