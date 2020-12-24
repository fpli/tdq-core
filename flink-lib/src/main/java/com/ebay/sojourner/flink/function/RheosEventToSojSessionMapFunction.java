package com.ebay.sojourner.flink.function;

import com.ebay.sojourner.common.model.SojSession;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.functions.RichMapFunction;

public class RheosEventToSojSessionMapFunction extends RichMapFunction<RheosEvent, SojSession> {

  @Override
  public SojSession map(RheosEvent rheosEvent) throws Exception {
    DatumReader<SojSession> reader
        = new SpecificDatumReader<>(SojSession.class);
    Decoder decoder = null;
    try {
      decoder = DecoderFactory.get().binaryDecoder(rheosEvent.toBytes(), null);
      return reader.read(null, decoder);
    } catch (IOException e) {
      throw new RuntimeException("deserialization error");
    }
  }
}
