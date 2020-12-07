package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.schema.RheosGenericKafkaSerializationSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class FlinkKafkaProducerFactory {

  private final KafkaProducerConfig config;

  public FlinkKafkaProducerFactory(KafkaProducerConfig config) {
    this.config = config;
  }

  public <T> FlinkKafkaProducer<T> get(String topic, KafkaSerializationSchema<T> serializer) {
      return new FlinkKafkaProducer<>(topic,
                                      serializer, config.getProperties(),
                                      Semantic.AT_LEAST_ONCE);
  }

  public FlinkKafkaProducer get(String topic, String subject, String[] keys) {
    String rheosServiceUrls = FlinkEnvUtils.getString(Property.RHEOS_KAFKA_REGISTRY_URL);
    String producerId = FlinkEnvUtils.getString(Property.PRODUCER_ID);

    RheosKafkaConfig rheosKafkaConfig = new RheosKafkaConfig(rheosServiceUrls,
                                                             topic, subject,
                                                             producerId, config.getProperties());

    return new FlinkKafkaProducer<>(topic,
                                  new RheosGenericKafkaSerializationSchema(rheosKafkaConfig, keys),
                                  config.getProperties(), Semantic.AT_LEAST_ONCE);
  }

  @Deprecated
  public <T> FlinkKafkaProducer<T> get(String topic,
                                       KeyedSerializationSchema<T> serializationSchema) {
    return new FlinkKafkaProducer<>(topic, serializationSchema, config.getProperties());
  }
}
