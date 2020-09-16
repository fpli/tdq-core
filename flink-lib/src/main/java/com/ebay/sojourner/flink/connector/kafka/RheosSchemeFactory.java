package com.ebay.sojourner.flink.connector.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;

public class RheosSchemeFactory implements SchemaFactory {

  protected volatile Holder holder;

  @Override
  public String getSchemaContent(String content) {
    return content;
  }

  @Override
  public GenericDatumWriter<Record> getWriter() {
    return holder.writer;
  }

  @Override
  public GenericDatumReader<Record> getReader() {
    return holder.reader;
  }

  @Override
  public Schema getSchema() {
    return holder.schema;
  }

  @Override
  public void setSchema(String content) {
    String schemaContent = getSchemaContent(content);
    Holder _holder = new Holder(schemaContent);
    holder = _holder;
  }

  protected class Holder {

    GenericDatumWriter<GenericData.Record> writer;
    GenericDatumReader<GenericData.Record> reader;
    Map<String, Schema> fields = new HashMap<String, Schema>();
    GenericData validator = GenericData.get();
    private Schema schema = null;

    public Holder(String content) {
      schema = new Schema.Parser().parse(content);
      writer = new GenericDatumWriter<GenericData.Record>(schema);
      reader = new GenericDatumReader<GenericData.Record>(schema) {
        @SuppressWarnings("rawtypes")
        @Override
        protected Class findStringClass(Schema schema) {
          return String.class;
        }

      };
      for (Schema.Field field : schema.getFields()) {
        fields.put(field.name(), field.schema());
      }
    }
  }

}
