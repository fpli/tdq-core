package com.ebay.tdq.connector.kafka.schema;

import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import io.ebay.rheos.schema.avro.GenericRecordDomainDataDecoder;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import io.ebay.rheos.schema.event.RheosEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class RheosEventDeserializationSchema implements DeserializationSchema<RheosEvent> {

  static RheosEventDeserializer deserializer = new RheosEventDeserializer();
  private final String rheosServiceURL;
  private transient volatile GenericRecordDomainDataDecoder decoder = null;
  private Long endTimestamp;

  public RheosEventDeserializationSchema(String rheosServiceURL, Long endTimestamp) {
    this.rheosServiceURL = rheosServiceURL;
    this.endTimestamp = endTimestamp;
  }

  public GenericRecordDomainDataDecoder getDecoder() {
    if (null == decoder) {
      Map<String, Object> config = new HashMap<>();
      config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, this.rheosServiceURL);
      synchronized (this.rheosServiceURL) {
        if (null == decoder) {
          decoder = new GenericRecordDomainDataDecoder(config);
        }
      }
    }
    return decoder;
  }

  @Override
  public TypeInformation<RheosEvent> getProducedType() {
    return TypeInformation.of(RheosEvent.class);
  }

  @Override
  public RheosEvent deserialize(byte[] message) {
    RheosEvent event = deserializer.deserialize("", message);
    GenericRecord record = getDecoder().decode(event);
    return new RheosEvent(record);
  }


  @Override
  public boolean isEndOfStream(RheosEvent nextElement) {
    return isEndOfStream((long) nextElement.get("viTimestamp"));
  }

  public boolean isEndOfStream(long t) {
    return this.endTimestamp > 0 && t > this.endTimestamp;
  }

}


