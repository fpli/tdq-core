package com.ebay.sojourner.flink.connectors.kafka.schema;

import com.ebay.sojourner.common.model.PulsarEvents;
import com.ebay.sojourner.flink.connectors.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class PulsarEventsDeserializationSchema implements DeserializationSchema<PulsarEvents> {

  @Override
  public PulsarEvents deserialize(byte[] message) throws IOException {

    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    long eventCreateTimestamp = rheosEvent.getEventCreateTimestamp();
    String content = getString(genericRecord.get("pulsarEvent"));

    return new PulsarEvents(eventCreateTimestamp, content);
  }

  @Override
  public boolean isEndOfStream(PulsarEvents nextElement) {
    return false;
  }

  @Override
  public TypeInformation<PulsarEvents> getProducedType() {
    return TypeInformation.of(PulsarEvents.class);
  }

  private String getString(Object o) {
    return (o != null && !"null".equals(o.toString())) ? o.toString() : null;
  }
}
