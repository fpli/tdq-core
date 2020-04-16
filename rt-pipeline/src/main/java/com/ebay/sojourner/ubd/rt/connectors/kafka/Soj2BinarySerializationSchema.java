package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojBytes;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class Soj2BinarySerializationSchema<T> implements KeyedSerializationSchema<T> {

  @Override
  public byte[] serializeKey(T element) {
    SojBytes kafkaBytes = (SojBytes) element;
    return kafkaBytes.getMessagekey();
  }

  @Override
  public byte[] serializeValue(T element) {
    SojBytes kafkaBytes = (SojBytes) element;
    return kafkaBytes.getMessage();
  }

  @Override
  public String getTargetTopic(T element) {
    return null;
  }
}
