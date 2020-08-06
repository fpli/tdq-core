package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import io.ebay.rheos.schema.avro.SchemaRegistryAwareAvroSerializerHelper;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

public class RheosSchemaUtils {

  private static SchemaRegistryAwareAvroSerializerHelper<GenericRecord> serializerHelper;
  private static Map<String, Object> config = new HashMap<>();
  private static Schema schema;

  public static Map<String, Object> getConfig() {

    config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, FlinkEnvUtils.getString(
        Property.RHEOS_KAFKA_REGISTRY_URL));
    return config;
  }

  private static SchemaRegistryAwareAvroSerializerHelper<GenericRecord> getSchemaRegistryHelper() {

    Map<String, Object> config = getConfig();
    serializerHelper = new SchemaRegistryAwareAvroSerializerHelper<>(config, GenericRecord.class);
    return serializerHelper;
  }

  public static Schema getSchema(String schemaName) {

    schema = getSchemaRegistryHelper().getSchema(schemaName);
    return schema;
  }
}
