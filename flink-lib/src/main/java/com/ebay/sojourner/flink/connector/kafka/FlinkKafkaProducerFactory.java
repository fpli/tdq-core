package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.flink.connector.kafka.schema.RheosKafkaSerializationSchema;
import com.google.common.base.Preconditions;
import org.apache.avro.specific.SpecificRecord;
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
    return new FlinkKafkaProducer<>(topic, serializer, config.getProperties(),
        Semantic.AT_LEAST_ONCE);
  }

  // Rheos kafka producer
  public <T extends SpecificRecord> FlinkKafkaProducer<T> get(Class<T> clazz,
                                                              String rheosServiceUrls, String topic,
                                                              String subject, String producerId,
                                                              String... keys) {
    Preconditions.checkNotNull(rheosServiceUrls);
    Preconditions.checkNotNull(topic);
    Preconditions.checkNotNull(subject);
    Preconditions.checkNotNull(producerId);

    RheosKafkaProducerConfig rheosKafkaConfig = new RheosKafkaProducerConfig(
        rheosServiceUrls, topic, subject, producerId, config.getProperties());

    return new FlinkKafkaProducer<>(topic,
                                new RheosKafkaSerializationSchema<>(clazz, rheosKafkaConfig, keys),
                                    config.getProperties(),
                                    Semantic.AT_LEAST_ONCE);
  }

  @Deprecated
  public <T> FlinkKafkaProducer<T> get(String topic,
      KeyedSerializationSchema<T> serializationSchema) {
    return new FlinkKafkaProducer<>(topic, serializationSchema, config.getProperties());
  }
}
