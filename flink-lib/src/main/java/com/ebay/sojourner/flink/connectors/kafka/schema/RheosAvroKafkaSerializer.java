package com.ebay.sojourner.flink.connectors.kafka.schema;

import io.ebay.rheos.schema.event.RheosEvent;
import io.jsonwebtoken.lang.Collections;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
public class RheosAvroKafkaSerializer<T> implements RheosKafkaSerializer<T> {

  private ThreadLocal<BinaryDecoder> decoderHolder = new ThreadLocal<BinaryDecoder>();
  private ThreadLocal<BinaryEncoder> encoderHolder = new ThreadLocal<BinaryEncoder>();
  private SchemaFactory schemaFactory;
  private SojSerializationSchema sojSerializationSchema;

  public RheosAvroKafkaSerializer(SchemaFactory schemaFactory,
      SojSerializationSchema sojSerializationSchema) {
    this.schemaFactory = schemaFactory;
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
          stringBuilder.append(value)
        } catch (NoSuchFieldException | IllegalAccessException e) {
          log.error("get field error:",e );
        }

      }
      SpecificRecordBase sb = (SpecificRecordBase) data;
      sb.getSchema().getField()
      List<Field> list = Collections.arrayToList(data.getClass().get());
      data.getClass().getDeclaredField()
      for (Field field : list) {
        field.get()
      }

    }
  }

  @Override
  public byte[] encodeData(T data) {
    if (org.apache.avro.specific.SpecificRecordBase.class.isAssignableFrom(data.getClass())) {

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      BinaryEncoder reusedEncoder = encoderHolder.get();
      BinaryEncoder directBinaryEncoder = EncoderFactory
          .get().directBinaryEncoder(out, reusedEncoder);
      if (reusedEncoder == null) {
        encoderHolder.set(directBinaryEncoder);
      }
      try {
        schemaFactory.getWriter()
            .write((org.apache.avro.specific.SpecificRecordBase) data, directBinaryEncoder);
      } catch (IOException e) {
        throw new IllegalArgumentException(
            "Can not encodeMessage the event to avro format: " + data, e);
      }
      return out.toByteArray();
    } else {
      reader = new ReflectDatumReader<>(avroType);
    }
  }

  private RheosEvent constructREvent(T data) {
    if (org.apache.avro.specific.SpecificRecordBase.class.isAssignableFrom(data.getClass())) {
      GenericRecord record = (GenericRecord) data;
      long createTimestamp = System.currentTimeMillis();
      RheosEvent rheosEvent = new RheosEvent(record);
      rheosEvent.setEventCreateTimestamp(createTimestamp);
      rheosEvent.setEventSentTimestamp(System.currentTimeMillis());
      rheosEvent.setProducerId(sojSerializationSchema..getProducerId());
      rheosEvent.setSchemaId(serializerHelper.getSchemaId(schemaBean.getSubject()));
    }
  }
}
