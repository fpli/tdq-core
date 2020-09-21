package com.ebay.sojourner.flink.connector.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;

@Slf4j
public class RheosSchemeFactory implements SchemaFactory {

  protected volatile Holder holder;

  @Override
  public Schema getSchemaContent(Schema content) {
    return content;
  }

  @Override
  public GenericDatumWriter<GenericRecord> getWriter() {
    return holder.writer;
  }

  @Override
  public Schema getSchema() {
    return holder.schema;
  }

  @Override
  public void setSchema(Schema content) {

    Schema schemaContent = getSchemaContent(content);
    Holder _holder = new Holder(schemaContent);
    holder = _holder;
  }

  public Object validateField(String key, Object value) {

    Schema schema = holder.fields.get(key);
    List<Schema> schemas;
    if (schema == null) {
      return null;
    } else if (!holder.validator.validate(schema, value)) {
      String cls = value.getClass().getSimpleName();
      try {
        schemas = schema.getTypes();
      } catch (Exception e) {
        log.error("Schema can not get types:", e, schema.toString());
        return value;
      }

      if (schemas != null && schemas.size() > 0) {
        String type = null;
        for (Schema s : schemas) {
          if (!s.getName().equalsIgnoreCase("null")) {
            type = s.getName();
            break;
          }
        }
        if (type != null) {
          try {
            if (type.equalsIgnoreCase("string")) {
              return value.toString();
            } else if (type.equalsIgnoreCase("int")) {
              if (cls.equalsIgnoreCase("boolean")) {
                return (Boolean) value ? 1 : 0;
              }
              return Integer.valueOf(value.toString());
            } else if (type.equalsIgnoreCase("long")) {
              return Long.valueOf(value.toString());
            } else if (type.equalsIgnoreCase("boolean")) {
              if (cls.equalsIgnoreCase("long")) {
                return ((Long) value) > 0;
              } else if (cls.equalsIgnoreCase("int")) {
                return ((Integer) value) > 0;
              }
            }
          } catch (Exception e) {
            throw new RuntimeException("Fail to cast " + key
                + " from type " + cls + " to " + schema, e);
          }
        }
      }
      throw new RuntimeException(
          "Type mismatch (" + key + "): expected " + schema + ", found " + cls);
    }
    return value;
  }

  protected class Holder {

    GenericDatumWriter<GenericRecord> writer;
    Map<String, Schema> fields = new HashMap<String, Schema>();
    GenericData validator = GenericData.get();
    private Schema schema;

    public Holder(Schema content) {
      this.schema = content;
      writer = new GenericDatumWriter(schema);
      for (Field field : content.getFields()) {
        fields.put(field.name(), field.schema());
      }
    }
  }
}
