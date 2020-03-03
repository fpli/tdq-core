package com.ebay.sojourner.ubd.rt.connectors.kafka;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

@Slf4j
public class Soj2BinaryDeserializationSchema implements DeserializationSchema<byte[]> {

  @Override
  public byte[] deserialize(byte[] message) throws IOException {
    return message;
  }

  private Integer getInteger(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return null;
    } else {
      return Integer.valueOf(getString(o));
    }
  }

  private boolean getBoolean(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return false;
    } else {
      return Boolean.parseBoolean(getString(o));
    }
  }

  private Long getLong(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return null;
    } else {
      return Long.valueOf(getString(o));
    }
  }

  private String getString(Object o) {
    return (o != null) ? o.toString() : null;
  }

  private String getStringNonNull(Object o) {
    return (o != null) ? o.toString() : "";
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
