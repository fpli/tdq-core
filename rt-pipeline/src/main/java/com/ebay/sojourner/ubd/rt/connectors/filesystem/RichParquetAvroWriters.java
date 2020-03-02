package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.formats.parquet.ParquetBuilder;
import org.apache.flink.formats.parquet.ParquetWriterFactory;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.io.OutputFile;

public class RichParquetAvroWriters {

  public static <T> ParquetWriterFactory<T> forAllowNullReflectRecord(Class<T> type) {
    final String schemaString = ReflectData.AllowNull.get().getSchema(type).toString();
    final ParquetBuilder<T> builder =
        (out) -> createAvroParquetWriter(schemaString, ReflectData.get(), out);
    return new ParquetWriterFactory<>(builder);
  }

  private static <T> ParquetWriter<T> createAvroParquetWriter(
      String schemaString, GenericData dataModel, OutputFile out) throws IOException {

    final Schema schema = new Schema.Parser().parse(schemaString);

    return AvroParquetWriter.<T>builder(out).withSchema(schema).withDataModel(dataModel).build();
  }
}
