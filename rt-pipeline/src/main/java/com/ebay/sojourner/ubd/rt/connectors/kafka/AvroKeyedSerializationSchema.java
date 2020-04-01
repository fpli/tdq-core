package com.ebay.sojourner.ubd.rt.connectors.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class AvroKeyedSerializationSchema<T> implements KeyedSerializationSchema<T> {

  private static final long serialVersionUID = 1L;
  private Class<T> avroType;
  private transient Field keyField = null;
  private transient GenericDatumWriter<T> writer;
  private transient BinaryEncoder encoder;

  public AvroKeyedSerializationSchema(Class<T> avroType, String keyField) {
    this.avroType = avroType;

    try {
      this.keyField = avroType.getDeclaredField(keyField);
      this.keyField.setAccessible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public byte[] serializeKey(T element) {
    byte[] serializedKey = new byte[0];
    try {
      serializedKey = keyField.get(element).toString().getBytes();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return serializedKey;
  }

  @Override
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

  @Override
  public String getTargetTopic(T element) {
    return null;
  }

  private void ensureInitialized() {
    if (writer == null) {
      if (org.apache.avro.specific.SpecificRecordBase.class.isAssignableFrom(avroType)) {
        writer = new SpecificDatumWriter<>(avroType);
      } else {
        writer = new ReflectDatumWriter<>(avroType, ReflectData.AllowNull.get());
      }
    }
  }
}
