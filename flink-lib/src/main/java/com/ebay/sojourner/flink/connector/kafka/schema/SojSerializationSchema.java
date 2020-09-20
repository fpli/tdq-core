package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.connector.kafka.RheosAvroKafkaSerializer;
import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import io.jsonwebtoken.lang.Collections;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

@Slf4j
public class SojSerializationSchema<T> implements
    KafkaSerializationSchema<T> {

  private List<String> keys;
  private transient SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper;
  private Properties producerConfig;
  private transient RheosAvroKafkaSerializer<T> rheosAvroKafkaSerializer;
  private String subject;
  private String topic;

  public SojSerializationSchema(String topic, String subjetName,
      Properties producerConfig, String... keys) {
    this.subject = subjetName;
    this.topic = topic;
    this.keys = Collections.arrayToList(keys);
    this.producerConfig = producerConfig;

  }

  @Override
  public ProducerRecord<byte[], byte[]> serialize(T element, @Nullable Long timestamp) {
    return new ProducerRecord(topic, serializeKey(element),
        serializeValue(element));
  }

  private byte[] serializeKey(T element) {
    init();
    return rheosAvroKafkaSerializer.encodeKey(element, keys);
  }

  private byte[] serializeValue(T element) {
    init();
    return rheosAvroKafkaSerializer.encodeData(element);
  }

  private void loadSchema(String subjectName) {
    init();
    serializerHelper.reload(subjectName);
  }

  public SchemaRegistryAwareAvroSerializerHelper getSerializerHelper() {
    init();
    return serializerHelper;
  }

  public String getSubject() {
    return subject;
  }

  public Properties getProducerConfig() {
    return producerConfig;
  }

  private void init() {
    if (serializerHelper == null) {
      serializerHelper = new SchemaRegistryAwareAvroSerializerHelper(
          producerConfig, GenericRecord.class);
      loadSchema(subject);
    }
    if (rheosAvroKafkaSerializer == null) {
      rheosAvroKafkaSerializer =
          new RheosAvroKafkaSerializer(serializerHelper.getSchema(subject),
              serializerHelper.getSchemaId(subject), producerConfig.getProperty(
              Property.PRODUCER_ID
          ));
    }
  }
}
