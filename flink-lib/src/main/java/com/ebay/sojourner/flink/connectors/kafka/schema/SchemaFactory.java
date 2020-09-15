package com.ebay.sojourner.flink.connectors.kafka.schema;

import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;

public interface SchemaFactory<T> {
  String getSchemaContent(String content);
  void setSchema(String content);
  GenericDatumWriter<T> getWriter();
  GenericDatumReader<T> getReader();
}
