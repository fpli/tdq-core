package com.ebay.sojourner.flink.connectors.kafka.schema;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;

public interface SchemaFactory {

  String getSchemaContent(String content);

  GenericDatumWriter<GenericData.Record> getWriter();

  GenericDatumReader<GenericData.Record> getReader();

  Schema getSchema();

  void setSchema(String content);
}
