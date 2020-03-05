package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

public class HdfsSinkUtil {

  public static <T> StreamingFileSink createWithParquet(String sinkPath, Class<T> sinkClass) {
    return StreamingFileSink.forBulkFormat(
        new Path(sinkPath), RichParquetAvroWriters.forAllowNullReflectRecord(sinkClass))
        .withBucketAssigner(new DateTimeBucketAssignerForEventTime<>())
        .build();
  }
}
