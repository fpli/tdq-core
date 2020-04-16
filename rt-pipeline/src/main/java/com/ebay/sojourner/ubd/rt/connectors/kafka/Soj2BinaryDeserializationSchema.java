package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojBytes;
import java.io.IOException;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

public class Soj2BinaryDeserializationSchema implements KeyedDeserializationSchema<SojBytes> {

  @Override
  public SojBytes deserialize(byte[] messageKey, byte[] message, String topic, int partition,
      long offset) throws IOException {
    if (Math.abs(messageKey[messageKey.length - 1] % 10) == 0) {
      return new SojBytes(messageKey, message);
    } else {
      return new SojBytes(new byte[0], new byte[0]);
    }
  }

  @Override
  public boolean isEndOfStream(SojBytes nextElement) {
    return false;
  }

  @Override
  public TypeInformation<SojBytes> getProducedType() {
    return TypeInformation.of(SojBytes.class);
  }
}
