package com.ebay.sojourner.flink.connectors.kafka.schema;

import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import io.jsonwebtoken.lang.Collections;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

@Slf4j
public class SojSerializationSchema<T> implements
    KafkaSerializationSchema<T> {

  private List key = null;
  private Schema schema;
  private SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper;
  public SojSerializationSchema(String subjetName, Schema schema,
      Properties producerConfig, String... keys) {
    key = Collections.arrayToList(keys);
    serializerHelper = new SchemaRegistryAwareAvroSerializerHelper(
        producerConfig, GenericRecord.class);
  }

  @Override
  public ProducerRecord<byte[], byte[]> serialize(T element, @Nullable Long timestamp) {
    return null;
  }

  public byte[] serializeKey(T element) {
    ensureInitialized();
    byte[] serializedKey = new byte[0];
    try {
      serializedKey = keyField.get(element).toString().getBytes(CHAR_SET);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return serializedKey;
  }

  public byte[] serializeValue(T element) {
    ensureInitialized();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    encoder = EncoderFactory.get().binaryEncoder(out, null);
    byte[] serializedValue = null;
    try {
      writer.write(element, encoder);
      encoder.flush();
      serializedValue = out.toByteArray();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return serializedValue;
  }

  private void loadSchema(String subjectName){
    serializerHelper.reload(subjectName);
    Schema dataSchema = serializerHelper.getSchema(subjectName);
    serializer.setSchema(dataSchema);
    log.info("load schema.");
  }
}
