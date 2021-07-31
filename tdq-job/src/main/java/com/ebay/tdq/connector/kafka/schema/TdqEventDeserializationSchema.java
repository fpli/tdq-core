package com.ebay.tdq.connector.kafka.schema;

import com.ebay.tdq.common.model.TdqEvent;
import io.ebay.rheos.schema.event.RheosEvent;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class TdqEventDeserializationSchema implements DeserializationSchema<TdqEvent> {

  private final String schemaRegistryUrl;
  private final Long endTimestamp;
  private final String eventTimeField;
  private final String schemaSubject;

  public TdqEventDeserializationSchema(String schemaRegistryUrl, Long endTimestamp, String eventTimeField,
      String schemaSubject) {
    this.schemaRegistryUrl = schemaRegistryUrl;
    this.endTimestamp = endTimestamp;
    this.eventTimeField = eventTimeField;
    Validate.isTrue(StringUtils.isNotBlank(schemaSubject), "schema-subject is empty!");
    this.schemaSubject = schemaSubject;
  }

  @Override
  public TypeInformation<TdqEvent> getProducedType() {
    return TypeInformation.of(TdqEvent.class);
  }

  @Override
  public TdqEvent deserialize(byte[] message) {
    RheosEvent event = RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord record = RheosEventSerdeFactory.getRheosEventDeserializer(this.schemaRegistryUrl).decode(event);
    return new TdqEvent(record, eventTimeField);
  }

  @Override
  public boolean isEndOfStream(TdqEvent nextElement) {
    return isEndOfStream(nextElement.getEventTimeMs());
  }

  public boolean isEndOfStream(long t) {
    return this.endTimestamp > 0 && t > this.endTimestamp;
  }

}


