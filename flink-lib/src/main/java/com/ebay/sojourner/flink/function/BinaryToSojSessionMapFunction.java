package com.ebay.sojourner.flink.function;

import com.ebay.sojourner.common.model.SojSession;
import java.io.IOException;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.functions.RichMapFunction;

public class BinaryToSojSessionMapFunction extends RichMapFunction<byte[], SojSession> {

  private transient DatumReader<SojSession> reader;

  @Override
  public SojSession map(byte[] data) throws Exception {
    if (reader == null) {
      reader = new SpecificDatumReader<>(SojSession.class);
    }

    Decoder decoder = null;
    try {
      decoder = DecoderFactory.get().binaryDecoder(data, null);
      return reader.read(null, decoder);
    } catch (IOException e) {
      throw new RuntimeException("Deserialize SojSession error", e);
    }
  }
}
