package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.common.model.SojBytesEvent;
import java.io.IOException;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class SojBytesEventDeserializationSchema implements
    DeserializationSchema<SojBytesEvent> {

  @Override
  public SojBytesEvent deserialize(byte[] message) throws IOException {
    if (Math.abs(message[message.length - 1] % 10) == 0) {
      return new SojBytesEvent(message, message);
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
