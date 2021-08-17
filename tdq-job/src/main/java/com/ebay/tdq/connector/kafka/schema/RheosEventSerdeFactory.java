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

public class RheosEventSerdeFactory implements Serializable {

  private static RheosEventDeserializer rheosEventHeaderDeserializer;
  private static GenericRecordDomainDataDecoder rheosEventDeserializer;

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
    Map<String, Object> config = new HashMap<>();
    config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, schemaRegistryUrl);
    SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper
        = new SchemaRegistryAwareAvroSerializerHelper<>(config, GenericRecord.class);
    return serializerHelper.getSchema(schemaSubject);
  }

  public static int getSchemaId(String schemaSubject, String schemaRegistryUrl) {
    Map<String, Object> config = new HashMap<>();
    config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, schemaRegistryUrl);
    SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper
        = new SchemaRegistryAwareAvroSerializerHelper<>(config, GenericRecord.class);
    return serializerHelper.getSchemaId(schemaSubject);
  }

}
