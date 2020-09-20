package com.ebay.sojourner.flink.connector.kafka;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;

public interface SchemaFactory {

  Schema getSchemaContent(Schema content);

  GenericDatumWriter<GenericRecord> getWriter();

  Schema getSchema();

  void setSchema(Schema content);

  Object validateField(String key, Object value);
}
