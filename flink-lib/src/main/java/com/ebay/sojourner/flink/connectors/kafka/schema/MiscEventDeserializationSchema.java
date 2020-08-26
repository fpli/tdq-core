package com.ebay.sojourner.flink.connectors.kafka.schema;

import com.ebay.sojourner.common.model.MiscEvent;
import com.ebay.sojourner.flink.connectors.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class MiscEventDeserializationSchema implements DeserializationSchema<MiscEvent> {

  @Override
  public MiscEvent deserialize(byte[] message) throws IOException {

    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    long eventCreateTimestamp = rheosEvent.getEventCreateTimestamp();
    String sojA = getString(genericRecord.get("sojA"));
    String sojC = getString(genericRecord.get("sojC"));
    String sojK = getString(genericRecord.get("sojK"));
    String clientData = getString(genericRecord.get("clientData"));

    return new MiscEvent(eventCreateTimestamp, sojA, sojC, sojK, clientData);
  }

  @Override
  public boolean isEndOfStream(MiscEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<MiscEvent> getProducedType() {
    return TypeInformation.of(MiscEvent.class);
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
      return Boolean.valueOf(getString(o));
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
    return (o != null && !"null".equals(o.toString())) ? o.toString() : null;
  }
}
