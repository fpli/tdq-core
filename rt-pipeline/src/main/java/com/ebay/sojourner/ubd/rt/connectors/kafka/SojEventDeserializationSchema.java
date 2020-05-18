package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputEvent;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class SojEventDeserializationSchema implements DeserializationSchema<JetStreamOutputEvent> {

  @Override
  public JetStreamOutputEvent deserialize(byte[] message) throws IOException {
    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);
    return new JetStreamOutputEvent();
  }

  @Override
  public boolean isEndOfStream(JetStreamOutputEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<JetStreamOutputEvent> getProducedType() {
    return TypeInformation.of(JetStreamOutputEvent.class);
  }
}
