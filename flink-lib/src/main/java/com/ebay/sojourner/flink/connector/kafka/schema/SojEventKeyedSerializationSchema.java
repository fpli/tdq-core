package com.ebay.sojourner.flink.connector.kafka.schema;

import io.jsonwebtoken.lang.Collections;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

@Slf4j
public class SojEventKeyedSerializationSchema<T> implements KeyedSerializationSchema<T> {

  private static final long serialVersionUID = 2L;
  private static final String CHAR_SET = "utf-8";
  private Class<T> tClass;
  private transient List<Field> keyFieldList = new ArrayList<>();
  private String delimiter = ",";
  // private Schema schema;
  private List<String> keyList;
  private transient GenericDatumWriter<T> writer;
  private transient BinaryEncoder encoder;

  public SojEventKeyedSerializationSchema(Class<T> tClass, String... keys) {
    this.tClass = tClass;
    this.keyList = Collections.arrayToList(keys);
  }

  @Override
  public byte[] serializeKey(T element) {
    ensureInitialized();
    byte[] serializedKey = new byte[0];
    try {
      StringBuilder messageKey = new StringBuilder();

      for (Field filed : keyFieldList) {
        messageKey.append(filed.get(element).toString()).append(delimiter);
      }
      if (StringUtils.isNotBlank(messageKey)) {
        serializedKey =
            messageKey.substring(0, messageKey.length() - 1).getBytes(CHAR_SET);
      }

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

    if (CollectionUtils.isEmpty(keyFieldList)) {
      keyFieldList = new ArrayList<>();
      String keyName = null;
      try {
        for (int i = 0; i < keyList.size(); i++) {
          keyName = keyList.get(i);
          Field field = tClass.getDeclaredField(keyName);
          field.setAccessible(true);
          keyFieldList.add(field);
        }
      } catch (Exception e) {
        log.error("init keyField1 failed,the key1 is " + keyName, e);
      }
    }

  }
}
