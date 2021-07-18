package com.ebay.sojourner.flink.connector.hdfs;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.ParquetBuilder;
import org.apache.flink.formats.parquet.ParquetWriterFactory;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

public class HdfsConnectorFactory {

  private HdfsConnectorFactory() {
  }

  public static <T> StreamingFileSink<T> createWithParquet(String sinkPath, Class<T> type) {
    final String schemaString = ReflectData.AllowNull.get().getSchema(type).toString();
    final ParquetBuilder<T> builder = out ->
        AvroParquetWriter.<T>builder(out).withSchema(new Schema.Parser().parse(schemaString))
            .withDataModel(ReflectData.get())
            .withCompressionCodec(CompressionCodecName.SNAPPY)
            .build();

    return StreamingFileSink.<T>forBulkFormat(new Path(sinkPath),
        new ParquetWriterFactory<>(builder))
        .withRollingPolicy(OnCheckpointRollingPolicy.build())
        .withBucketAssigner(new DateTimeBucketAssignerForEventTime<>())
        .build();
  }
}
