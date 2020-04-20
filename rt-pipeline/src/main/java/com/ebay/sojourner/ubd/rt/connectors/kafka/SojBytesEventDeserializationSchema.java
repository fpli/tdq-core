package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojBytesEvent;
import java.io.IOException;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

public class SojBytesEventDeserializationSchema implements
    KeyedDeserializationSchema<SojBytesEvent> {

  @Override
  public SojBytesEvent deserialize(byte[] messageKey, byte[] message, String topic, int partition,
      long offset) throws IOException {
    if (Math.abs(messageKey[messageKey.length - 1] % 10) == 0) {
      return new SojBytesEvent(messageKey, message);
    } else {
      return new SojBytesEvent(null, null);
    }
  }

  @Override
  public boolean isEndOfStream(SojBytesEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<SojBytesEvent> getProducedType() {
    return TypeInformation.of(SojBytesEvent.class);
  }
}
