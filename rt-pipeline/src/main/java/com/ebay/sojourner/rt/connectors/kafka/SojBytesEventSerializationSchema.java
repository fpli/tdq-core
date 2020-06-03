package com.ebay.sojourner.rt.connectors.kafka;

import com.ebay.sojourner.common.model.SojBytesEvent;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class SojBytesEventSerializationSchema<T> implements KeyedSerializationSchema<T> {

  @Override
  public byte[] serializeKey(T element) {
    if (element != null) {
      SojBytesEvent kafkaBytes = (SojBytesEvent) element;
      return kafkaBytes.getMessagekey();
    } else {
      return new byte[0];
    }
  }

  @Override
  public byte[] serializeValue(T element) {
    if (element != null) {
      SojBytesEvent kafkaBytes = (SojBytesEvent) element;
      return kafkaBytes.getMessage();
    } else {
      return new byte[0];
    }
  }

  @Override
  public String getTargetTopic(T element) {
    return null;
  }
}
