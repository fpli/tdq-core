package com.ebay.sojourner.flink.connectors.kafka.schema;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

@Slf4j
public class SojEventKeyedSerializationSchema<T> implements KeyedSerializationSchema<T> {

  private static final long serialVersionUID = 2L;
  private static final String CHAR_SET = "utf-8";
  private Class<T> tClass;
  private transient Field keyField1;
  private transient Field keyField2;
  private String delimiter = ",";
  // private Schema schema;
  private String keyFieldStr1;
  private String keyFieldStr2;
  private transient GenericDatumWriter<T> writer;
  private transient BinaryEncoder encoder;

  public SojEventKeyedSerializationSchema(Class<T> tClass, String keyField1, String keyField2) {
    this.tClass = tClass;
    this.keyFieldStr1 = keyField1;
    this.keyFieldStr2 = keyField2;
    // this.schema = RheosSchemaUtils.getSchema("pulsar_event-dump");
  }

  @Override
  public byte[] serializeKey(T element) {
    ensureInitialized();
    byte[] serializedKey = new byte[0];
    try {
      String messageKey =
          keyField1.get(element).toString() + delimiter + keyField2.get(element).toString();
      serializedKey = messageKey.getBytes(CHAR_SET);
    } catch (Exception e) {
      log.error("serialize key failed", e);
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
      log.error("serialize value failed", e);
    }
    return serializedValue;
  }

  @Override
  public String getTargetTopic(T element) {
    return null;
  }

  private void ensureInitialized() {

    if (writer == null) {
      if (org.apache.avro.specific.SpecificRecordBase.class.isAssignableFrom(tClass)) {
        writer = new SpecificDatumWriter<>(tClass);
        // writer = new SpecificDatumWriter<>(schema);
      } else {
        writer = new ReflectDatumWriter<>(tClass, ReflectData.AllowNull.get());
        // writer = new ReflectDatumWriter<>(schema, ReflectData.AllowNull.get());
      }
    }

    if (keyField1 == null) {
      try {
        this.keyField1 = tClass.getDeclaredField(this.keyFieldStr1);
        this.keyField1.setAccessible(true);
      } catch (Exception e) {
        log.error("init keyField1 failed,the key1 is " + keyFieldStr1, e);
      }
    }

    if (keyField2 == null) {
      try {
        this.keyField2 = tClass.getDeclaredField(this.keyFieldStr2);
        this.keyField2.setAccessible(true);
      } catch (Exception e) {
        log.error("init keyField2 failed,the key2 is " + keyFieldStr2, e);
      }
    }
  }
}
