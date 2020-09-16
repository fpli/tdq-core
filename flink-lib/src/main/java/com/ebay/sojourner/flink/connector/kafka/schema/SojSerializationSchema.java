package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.flink.connector.kafka.RheosAvroKafkaSerializer;
import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import io.jsonwebtoken.lang.Collections;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

@Slf4j
public class SojSerializationSchema<T> implements
    KafkaSerializationSchema<T> {

  private List<String> keys = null;
  private Schema schema;
  private SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper;
  private Properties producerConfig;
  private RheosAvroKafkaSerializer<T> rheosAvroKafkaSerializer;
  private String subject;
  private String topic;

  public SojSerializationSchema(String topic, String subjetName,
      Properties producerConfig, String... keys) {
    this.subject = subjetName;
    this.topic = topic;
    this.keys = Collections.arrayToList(keys);
    serializerHelper = new SchemaRegistryAwareAvroSerializerHelper(
        producerConfig, GenericRecord.class);
    this.producerConfig = producerConfig;
    rheosAvroKafkaSerializer = new RheosAvroKafkaSerializer(this);
    loadSchema(subjetName);
  }

  @Override
  public ProducerRecord<byte[], byte[]> serialize(T element, @Nullable Long timestamp) {
    return new ProducerRecord(topic, serializeKey(element),
        serializeValue(element));
  }

  private byte[] serializeKey(T element) {

    return rheosAvroKafkaSerializer.encodeKey(element, keys);
  }

  private byte[] serializeValue(T element) {
    return rheosAvroKafkaSerializer.encodeData(element);
  }

  private void loadSchema(String subjectName) {
    serializerHelper.reload(subjectName);
    Schema dataSchema = serializerHelper.getSchema(subjectName);
    this.schema = dataSchema;
    log.info("load schema.");
  }

  public SchemaRegistryAwareAvroSerializerHelper getSerializerHelper() {
    return serializerHelper;
  }

  public String getSubject() {
    return subject;
  }

  public Properties getProducerConfig(){
    return producerConfig;
  }
}
