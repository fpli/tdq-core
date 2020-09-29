package com.ebay.sojourner.flink.connector.kafka.schema;

import java.io.IOException;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class PassThroughDeserializationSchema implements DeserializationSchema<byte[]> {

  @Override
  public byte[] deserialize(byte[] message) throws IOException {
    return message;
  }

  @Override
  public boolean isEndOfStream(byte[] nextElement) {
    return false;
  }

  @Override
  public TypeInformation<byte[]> getProducedType() {
    return TypeInformation.of(byte[].class);
  }
}
