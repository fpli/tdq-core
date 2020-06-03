package com.ebay.sojourner.rt.connectors.kafka;

import java.io.IOException;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

public class AvroKeyedDeserializationSchema<T> implements KeyedDeserializationSchema<T> {

  private static final long serialVersionUID = 1L;

  private final Class<T> avroType;
  private transient DatumReader<T> reader;
  private transient BinaryDecoder decoder;

  public AvroKeyedDeserializationSchema(Class<T> avroType) {
    this.avroType = avroType;
  }

  @Override
  public T deserialize(byte[] messageKey, byte[] message, String topic, int partition,
      long offset) throws IOException {
    ensureInitialized();
    try {
      decoder = DecoderFactory.get().binaryDecoder(message, null);
      return reader.read(null, decoder);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public boolean isEndOfStream(T nextElement) {
    return false;
  }

  @Override
  public TypeInformation<T> getProducedType() {
    return TypeInformation.of(avroType);
  }

  private void ensureInitialized() {
    if (reader == null) {
      if (org.apache.avro.specific.SpecificRecordBase.class.isAssignableFrom(avroType)) {
        reader = new SpecificDatumReader<>(avroType);
      } else {
        reader = new ReflectDatumReader<>(avroType);
      }
    }
  }
}
