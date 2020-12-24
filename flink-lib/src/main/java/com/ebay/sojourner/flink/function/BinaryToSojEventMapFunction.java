package com.ebay.sojourner.flink.function;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.flink.connector.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.functions.RichMapFunction;

public class BinaryToSojEventMapFunction extends RichMapFunction<byte[], SojEvent> {

  @Override
  public SojEvent map(byte[] data) throws Exception {

    RheosEvent rheosEvent = RheosEventSerdeFactory.getRheosEventHeaderDeserializer()
                                                  .deserialize(null, data);

    DatumReader<SojEvent> reader = new SpecificDatumReader<>(SojEvent.class);
    Decoder decoder = null;
    try {
      decoder = DecoderFactory.get().binaryDecoder(rheosEvent.toBytes(), null);
      return reader.read(null, decoder);
    } catch (IOException e) {
      throw new RuntimeException("deserialization error");
    }
  }
}
