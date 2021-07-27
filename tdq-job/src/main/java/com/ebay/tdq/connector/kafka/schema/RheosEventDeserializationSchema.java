package com.ebay.tdq.connector.kafka.schema;

import com.ebay.tdq.common.model.TdqEvent;
import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import io.ebay.rheos.schema.avro.GenericRecordDomainDataDecoder;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import io.ebay.rheos.schema.event.RheosEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class RheosEventDeserializationSchema implements DeserializationSchema<TdqEvent> {

  private static final RheosEventDeserializer deserializer = new RheosEventDeserializer();
  private final String rheosServiceURL;
  private final Long endTimestamp;
  private final String eventTimeField;
  private final String schemaSubject;
  private transient volatile GenericRecordDomainDataDecoder decoder = null;
  private transient volatile Schema schema = null;

  public RheosEventDeserializationSchema(String rheosServiceURL, Long endTimestamp, String eventTimeField,
      String schemaSubject) {
    this.rheosServiceURL = rheosServiceURL;
    this.endTimestamp = endTimestamp;
    this.eventTimeField = eventTimeField;
    Validate.isTrue(StringUtils.isNotBlank(schemaSubject), "schema-subject is empty!");
    this.schemaSubject = schemaSubject;
  }

  public Schema getSchema() {
    if (schema == null) {
      if (StringUtils.isBlank(schemaSubject)) {
        return null;
      }
      Map<String, Object> config = new HashMap<>();
      config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, this.rheosServiceURL);
      synchronized (schemaSubject) {
        if (schema == null) {
          SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper
              = new SchemaRegistryAwareAvroSerializerHelper<>(config, GenericRecord.class);
          schema = serializerHelper.getSchema(schemaSubject);
        }
      }
    }
    return schema;
  }

  private GenericRecordDomainDataDecoder getDecoder() {
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
  public TypeInformation<TdqEvent> getProducedType() {
    return TypeInformation.of(TdqEvent.class);
  }

  @Override
  public TdqEvent deserialize(byte[] message) {
    RheosEvent event = deserializer.deserialize("", message);
    GenericRecord record = getDecoder().decode(event);
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


