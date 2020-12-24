package com.ebay.sojourner.flink.connector.kafka;

import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class RheosAvroKafkaSerializer<T extends SpecificRecord> implements RheosKafkaSerializer<T> {

  private static final String FIELD_DELIM = ",";
  private final RheosKafkaProducerConfig rheosKafkaConfig;
  private final SchemaRegistryAwareAvroSerializerHelper<T> serializerHelper;

  public RheosAvroKafkaSerializer(RheosKafkaProducerConfig rheosKafkaConfig, Class<T> clazz) {
    this.rheosKafkaConfig = rheosKafkaConfig;
    this.serializerHelper =
        new SchemaRegistryAwareAvroSerializerHelper<>(rheosKafkaConfig.toConfigMap(), clazz);
  }

  @Override
  public byte[] encodeKey(T data, List<String> keyList) {
    StringBuilder stringBuilder = new StringBuilder();
    if (data == null || CollectionUtils.isEmpty(keyList)) {
      return null;
    } else {
      Field field = null;
      for (String keyName : keyList) {
        try {
          field = data.getClass().getDeclaredField(keyName);
          field.setAccessible(true);
          Object value = field.get(data);
          stringBuilder.append(value).append(FIELD_DELIM);
        } catch (NoSuchFieldException | IllegalAccessException e) {
          log.error("get field error:", e, keyName);
        }
      }
      if (StringUtils.isNotBlank(stringBuilder.toString())) {
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1)
                            .getBytes(Charsets.UTF_8);
      } else {
        return null;
      }
    }
  }

  @Override
  public byte[] encodeValue(T data) {
    Schema schema = serializerHelper.getSchema(rheosKafkaConfig.getSchemaSubject());
    GenericRecord record = (GenericRecord) GenericData.get().deepCopy(schema, data);

    RheosEvent rheosEvent = new RheosEvent(record);

    rheosEvent.setEventCreateTimestamp(System.currentTimeMillis());
    rheosEvent.setEventSentTimestamp(System.currentTimeMillis());
    rheosEvent.setSchemaId(serializerHelper.getSchemaId(rheosKafkaConfig.getSchemaSubject()));
    rheosEvent.setProducerId(rheosKafkaConfig.getProducerId());

    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      DatumWriter<RheosEvent> writer = new GenericDatumWriter<>(rheosEvent.getSchema());

      byte[] serializedValue = null;
      BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
      writer.write(rheosEvent, encoder);
      encoder.flush();
      serializedValue = out.toByteArray();
      out.close();
      return serializedValue;
    } catch (Exception e) {
      throw new SerializationException("Error serializing Avro schema for schema " +
                                       schema.getName(), e);
    }
  }
}