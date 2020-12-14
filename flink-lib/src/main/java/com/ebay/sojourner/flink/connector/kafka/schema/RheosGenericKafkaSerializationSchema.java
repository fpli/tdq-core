package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.flink.connector.kafka.RheosAvroKafkaSerializer;
import com.ebay.sojourner.flink.connector.kafka.RheosKafkaConfig;
import com.google.common.collect.Lists;
import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

public class RheosGenericKafkaSerializationSchema implements
    KafkaSerializationSchema<GenericRecord> {

  private final RheosKafkaConfig rheosKafkaConfig;
  private final List<String> keys;
  private transient SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper;
  private transient RheosAvroKafkaSerializer<GenericRecord> rheosAvroKafkaSerializer;

  public RheosGenericKafkaSerializationSchema(RheosKafkaConfig rheosKafkaConfig, String... keys) {
    this.rheosKafkaConfig = rheosKafkaConfig;
    this.keys = Lists.newArrayList(keys);
  }

  @Override
  public ProducerRecord<byte[], byte[]> serialize(GenericRecord element, @Nullable Long timestamp) {
    return new ProducerRecord<>(rheosKafkaConfig.getTopic(),
                                serializeKey(element), serializeValue(element));
  }

  private byte[] serializeKey(GenericRecord element) {
    init();
    return rheosAvroKafkaSerializer.encodeKey(element, keys);
  }

  private byte[] serializeValue(GenericRecord element) {
    init();
    return rheosAvroKafkaSerializer.encodeData(element);
  }

  private void loadSchema(String subjectName) {
    init();
    serializerHelper.reload(subjectName);
  }

  private void init() {
    if (serializerHelper == null) {

      serializerHelper = new SchemaRegistryAwareAvroSerializerHelper<>(
          rheosKafkaConfig.toProducerConfigMap(), GenericRecord.class);
      loadSchema(rheosKafkaConfig.getSubject());
    }
    if (rheosAvroKafkaSerializer == null) {
      rheosAvroKafkaSerializer =
          new RheosAvroKafkaSerializer<>(serializerHelper.getSchema(rheosKafkaConfig.getSubject()),
              serializerHelper.getSchemaId(rheosKafkaConfig.getSubject()),
                                         rheosKafkaConfig.getProducerId());
    }
  }
}
