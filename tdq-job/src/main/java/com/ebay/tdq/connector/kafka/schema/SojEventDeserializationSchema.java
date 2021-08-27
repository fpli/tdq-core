package com.ebay.tdq.connector.kafka.schema;

import com.ebay.tdq.common.model.SojEvent;
import java.io.IOException;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class SojEventDeserializationSchema implements DeserializationSchema<SojEvent> {

  private transient DatumReader<SojEvent> reader;

  public SojEventDeserializationSchema() {
  }

  @Override
  public TypeInformation<SojEvent> getProducedType() {
    return TypeInformation.of(SojEvent.class);
  }

  @Override
  public SojEvent deserialize(byte[] message) {
    if (reader == null) {
      reader = new SpecificDatumReader<>(SojEvent.class);
    }
    SojEvent sojEvent/* = SojEvent.getDecoder().decode(message)*/;
    Decoder decoder;
    try {
      decoder = DecoderFactory.get().binaryDecoder(message, null);
      sojEvent = reader.read(null, decoder);
    } catch (IOException e) {
      throw new RuntimeException("Deserialize SojEvent error", e);
    }
    sojEvent.getApplicationPayload().put("tdq_ingest_time", String.valueOf(System.currentTimeMillis()));
    return sojEvent;
  }

  @Override
  public boolean isEndOfStream(SojEvent nextElement) {
    return false;
  }
}


