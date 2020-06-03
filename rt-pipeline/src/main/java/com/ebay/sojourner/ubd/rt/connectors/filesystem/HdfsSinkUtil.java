package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import com.ebay.sojourner.ubd.common.model.CrossSessionSignature;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.ubd.common.model.JetStreamOutputSession;
import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.rt.util.HdfsPathConstants;
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
    return HdfsSinkUtil.createWithParquet(HdfsPathConstants.EVENT_NON_BOT_PATH, SojEvent.class);
  }

  public static StreamingFileSink sojSessionSinkWithParquet() {
    return HdfsSinkUtil.createWithParquet(HdfsPathConstants.SESSION_NON_BOT_PATH, SojSession.class);
  }

  public static StreamingFileSink signatureSinkWithParquet() {
    return HdfsSinkUtil
        .createWithParquet(HdfsPathConstants.SIGNATURE_PATH, CrossSessionSignature.class);
  }

  public static StreamingFileSink intermediateSessionSinkWithParquet() {
    return HdfsSinkUtil
        .createWithParquet(HdfsPathConstants.INTERMEDIATE_SESSION_PATH, IntermediateSession.class);
  }

  public static StreamingFileSink lateEventSinkWithParquet() {
    return HdfsSinkUtil.createWithParquet(HdfsPathConstants.LATE_EVENT_PATH, SojEvent.class);
  }

  public static StreamingFileSink jetstreamSojEventSinkWithParquet() {
    return HdfsSinkUtil
        .createWithParquet(HdfsPathConstants.JETSTREAM_EVENT_PATH, JetStreamOutputEvent.class);
  }

  public static StreamingFileSink jetstreamSojSessionSinkWithParquet() {
    return HdfsSinkUtil
        .createWithParquet(HdfsPathConstants.JETSTREAM_SESSION_PATH, JetStreamOutputSession.class);
  }
}
