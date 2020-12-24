package com.ebay.sojourner.flink.function;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.flink.connector.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.functions.RichMapFunction;

public class BinaryToSojSessionMapFunction extends RichMapFunction<byte[], SojSession> {

  @Override
  public SojSession map(byte[] data) throws Exception {
    RheosEvent rheosEvent = RheosEventSerdeFactory.getRheosEventHeaderDeserializer()
                                                  .deserialize(null, data);
    DatumReader<SojSession> reader = new SpecificDatumReader<>(SojSession.class);
    Decoder decoder = null;
    try {
      decoder = DecoderFactory.get().binaryDecoder(rheosEvent.toBytes(), null);
      return reader.read(null, decoder);
    } catch (IOException e) {
      throw new RuntimeException("deserialization error");
    }
  }
}
