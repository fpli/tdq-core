package com.ebay.tdq.connector.kafka.schema;

import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import io.ebay.rheos.schema.avro.GenericRecordDomainDataDecoder;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;

public class RheosEventSerdeFactory implements Serializable {

  private static RheosEventDeserializer rheosEventHeaderDeserializer;
  private static GenericRecordDomainDataDecoder rheosEventDeserializer;
  private static Schema schema = null;

  public static RheosEventDeserializer getRheosEventHeaderDeserializer() {
    if (rheosEventHeaderDeserializer == null) {
      synchronized (RheosEventSerdeFactory.class) {
        if (rheosEventDeserializer == null) {
          rheosEventHeaderDeserializer = new RheosEventDeserializer();
        }
      }
    }
    return rheosEventHeaderDeserializer;
  }

  public static GenericRecordDomainDataDecoder getRheosEventDeserializer(String schemaRegistryUrl) {
    if (rheosEventDeserializer == null) {
      Map<String, Object> config = new HashMap<>();
      config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, schemaRegistryUrl);
      synchronized (RheosEventSerdeFactory.class) {
        if (rheosEventDeserializer == null) {
          rheosEventDeserializer = new GenericRecordDomainDataDecoder(config);
        }
      }
    }
    return rheosEventDeserializer;
  }

  public static Schema getSchema(String schemaSubject, String schemaRegistryUrl) {
    if (schema == null) {
      if (StringUtils.isBlank(schemaSubject)) {
        return null;
      }
      Map<String, Object> config = new HashMap<>();
      config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, schemaRegistryUrl);
      synchronized (RheosEventSerdeFactory.class) {
        if (schema == null) {
          SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper
              = new SchemaRegistryAwareAvroSerializerHelper<>(config, GenericRecord.class);
          schema = serializerHelper.getSchema(schemaSubject);
        }
      }
    }
    return schema;
  }

}
