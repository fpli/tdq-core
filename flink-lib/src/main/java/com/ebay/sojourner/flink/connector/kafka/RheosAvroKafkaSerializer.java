package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.flink.connector.kafka.schema.SojSerializationSchema;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RheosAvroKafkaSerializer<T> implements RheosKafkaSerializer<T> {

  private static final String FIELD_DELIM = ",";
  private static int schemaId;
  private static String producerId;
  private transient BinaryEncoder encoder;
  private SchemaFactory schemaFactory;
  private SojSerializationSchema sojSerializationSchema;

  public RheosAvroKafkaSerializer(
      Schema schema, int schemaId, String producerId) {
    this.schemaFactory = new RheosSchemeFactory();
    this.schemaId = schemaId;
    this.producerId = producerId;
    schemaFactory.setSchema(schema);
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
  public byte[] encodeData(T data) {

    GenericRecord genericRecord = constructREvent(data);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    encoder = EncoderFactory.get().binaryEncoder(out, null);
    byte[] serializedValue = null;
    try {
      schemaFactory.getWriter().write(genericRecord, encoder);
      encoder.flush();
      serializedValue = out.toByteArray();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return serializedValue;

  }

  private RheosEvent constructREvent(T data) {
    GenericRecord record = constructGenericRecord(data);
    long createTimestamp = System.currentTimeMillis();
    RheosEvent rheosEvent = new RheosEvent(record);
    rheosEvent.setEventCreateTimestamp(createTimestamp);
    rheosEvent.setEventSentTimestamp(System.currentTimeMillis());
    rheosEvent
        .setProducerId(this.producerId);
    rheosEvent.setSchemaId(this.schemaId);
    return rheosEvent;
  }

  private GenericRecord constructGenericRecord(T data) {
    GenericData.Record record = new GenericData.Record(schemaFactory.getSchema());
    Field[] columns = data.getClass().getDeclaredFields();
    for (Field column : columns) {
      try {
        column.setAccessible(true);
        Object value = column.get(data);
        Object valueValid = schemaFactory.validateField(column.getName(), value);
        if (valueValid == null) {
          continue;
        }
        record.put(column.getName(), valueValid);

      } catch (IllegalAccessException e) {
        log.error("constructGenericRecord Error：", e);
      }
    }
    return record;
  }
}