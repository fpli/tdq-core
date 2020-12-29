package com.ebay.sojourner.flink.function;

import com.ebay.sojourner.common.model.SojEvent;
import java.io.IOException;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.functions.RichMapFunction;

public class BinaryToSojEventMapFunction extends RichMapFunction<byte[], SojEvent> {

  private transient DatumReader<SojEvent> reader;

  @Override
  public SojEvent map(byte[] data) throws Exception {
    if (reader == null) {
      reader = new SpecificDatumReader<>(SojEvent.class);
    }

    Decoder decoder = null;
    try {
      decoder = DecoderFactory.get().binaryDecoder(data, null);
      return reader.read(null, decoder);
    } catch (IOException e) {
      throw new RuntimeException("Deserialize SojEvent error", e);
    }
  }
}
