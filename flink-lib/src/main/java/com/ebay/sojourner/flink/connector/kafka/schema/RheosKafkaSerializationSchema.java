package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.flink.connector.kafka.RheosAvroKafkaSerializer;
import com.ebay.sojourner.flink.connector.kafka.RheosKafkaProducerConfig;
import com.ebay.sojourner.flink.connector.kafka.RheosKafkaSerializer;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.Nullable;

public class RheosKafkaSerializationSchema<T extends SpecificRecord> implements
    KafkaSerializationSchema<T> {

  private final RheosKafkaProducerConfig rheosKafkaConfig;
  private final List<String> keys;
  private final Class<T> clazz;
  private transient RheosKafkaSerializer<T> rheosKafkaSerializer;

  public RheosKafkaSerializationSchema(Class<T> clazz, RheosKafkaProducerConfig rheosKafkaConfig,
                                       String... keys) {
    this.rheosKafkaConfig = rheosKafkaConfig;
    this.keys = Lists.newArrayList(keys);
    this.clazz = clazz;
    this.rheosKafkaSerializer = new RheosAvroKafkaSerializer<>(rheosKafkaConfig, clazz);
  }

  @Override
  public ProducerRecord<byte[], byte[]> serialize(T element, @Nullable Long timestamp) {
    if (rheosKafkaSerializer == null) {
      rheosKafkaSerializer = new RheosAvroKafkaSerializer<>(rheosKafkaConfig, clazz);
    }

    return new ProducerRecord<>(rheosKafkaConfig.getTopic(),
                                rheosKafkaSerializer.encodeKey(element, keys),
                                rheosKafkaSerializer.encodeValue(element));
  }
}
