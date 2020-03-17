package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.rt.util.HdfsPathConstants;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

public class HdfsSinkUtil {

  public static <T> StreamingFileSink createWithParquet(String sinkPath, Class<T> sinkClass) {
    return StreamingFileSink.forBulkFormat(
        new Path(sinkPath), RichParquetAvroWriters.forAllowNullReflectRecord(sinkClass))
        .withBucketAssigner(new DateTimeBucketAssignerForEventTime<>())
        .build();
  }

  public static StreamingFileSink sojEventSinkWithParquet() {
    return HdfsSinkUtil.createWithParquet(HdfsPathConstants.EVENT_PATH, SojEvent.class);
  }

  public static StreamingFileSink sojSessionSinkWithParquet() {
    return HdfsSinkUtil.createWithParquet(HdfsPathConstants.SESSION_PATH, SojSession.class);
  }

  public static StreamingFileSink signatureSinkWithParquet() {
    return HdfsSinkUtil.createWithParquet(HdfsPathConstants.SIGNATURE_PATH, Tuple4.class);
  }

  public static StreamingFileSink lateEventSinkWithParquet() {
    return HdfsSinkUtil.createWithParquet(HdfsPathConstants.LATE_EVENT_PATH, SojEvent.class);
  }

}
