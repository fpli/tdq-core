package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.connector.kafka.schema.SojSerializationSchema;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RheosAvroKafkaSerializer<T> implements RheosKafkaSerializer<T> {

  private static final String FIELD_DELIM = ",";
  private ThreadLocal<BinaryDecoder> decoderHolder = new ThreadLocal<BinaryDecoder>();
  private ThreadLocal<BinaryEncoder> encoderHolder = new ThreadLocal<BinaryEncoder>();
  private SchemaFactory schemaFactory;
  private SojSerializationSchema sojSerializationSchema;

  public RheosAvroKafkaSerializer(
      SojSerializationSchema sojSerializationSchema) {
    this.schemaFactory = new RheosSchemeFactory();
    this.sojSerializationSchema = sojSerializationSchema;
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
    BinaryEncoder reusedEncoder = encoderHolder.get();
    BinaryEncoder directBinaryEncoder = EncoderFactory
        .get().directBinaryEncoder(out, reusedEncoder);
    if (reusedEncoder == null) {
      encoderHolder.set(directBinaryEncoder);
    }
    try {
      schemaFactory.getWriter()
          .write((Record) genericRecord, directBinaryEncoder);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Can not encodeMessage the event to avro format: " + data, e);
    }
    return out.toByteArray();

  }

  private RheosEvent constructREvent(T data) {
    GenericRecord record = null;
    if (org.apache.avro.specific.SpecificRecordBase.class.isAssignableFrom(data.getClass())) {
      record = (GenericRecord) data;
    } else {
      record = constructGenericRecord(data);
    }
    long createTimestamp = System.currentTimeMillis();
    RheosEvent rheosEvent = new RheosEvent(record);
    rheosEvent.setEventCreateTimestamp(createTimestamp);
    rheosEvent.setEventSentTimestamp(System.currentTimeMillis());
    rheosEvent
        .setProducerId(
            sojSerializationSchema.getProducerConfig().getProperty(Property.PRODUCER_ID));
    rheosEvent.setSchemaId(sojSerializationSchema.getSerializerHelper()
        .getSchemaId(sojSerializationSchema.getSubject()));
    return rheosEvent;
  }

  private GenericRecord constructGenericRecord(T data) {
    Schema schema = schemaFactory.getSchema();
    GenericData.Record record = new GenericData.Record(schemaFactory.getSchema());
    List<Schema.Field> fields = schema.getFields();
    for (Schema.Field field : fields) {
      try {
        Field column = data.getClass().getDeclaredField(field.name());
        column.setAccessible(true);
        Object value = column.get(data);
        record.put(field.name(), value);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return record;
  }
}