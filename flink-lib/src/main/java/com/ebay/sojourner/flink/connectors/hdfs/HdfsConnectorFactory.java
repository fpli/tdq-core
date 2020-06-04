package com.ebay.sojourner.flink.connectors.hdfs;

import com.ebay.sojourner.common.model.CrossSessionSignature;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.common.model.JetStreamOutputSession;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.flink.common.util.Constants;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

public class HdfsConnectorFactory {

  public static <T> StreamingFileSink createWithParquet(String sinkPath, Class<T> sinkClass) {
    return StreamingFileSink.forBulkFormat(
        new Path(sinkPath), RichParquetAvroWriters.forAllowNullReflectRecord(sinkClass))
        .withBucketAssigner(new DateTimeBucketAssignerForEventTime<>())
        .build();
  }

  public static StreamingFileSink sojEventSinkWithParquet() {
    return HdfsConnectorFactory.createWithParquet(Constants.EVENT_NON_BOT_PATH, SojEvent.class);
  }

  public static StreamingFileSink sojSessionSinkWithParquet() {
    return HdfsConnectorFactory.createWithParquet(Constants.SESSION_NON_BOT_PATH, SojSession.class);
  }

  public static StreamingFileSink signatureSinkWithParquet() {
    return HdfsConnectorFactory
        .createWithParquet(Constants.SIGNATURE_PATH, CrossSessionSignature.class);
  }

  public static StreamingFileSink intermediateSessionSinkWithParquet() {
    return HdfsConnectorFactory
        .createWithParquet(Constants.INTERMEDIATE_SESSION_PATH, IntermediateSession.class);
  }

  public static StreamingFileSink lateEventSinkWithParquet() {
    return HdfsConnectorFactory.createWithParquet(Constants.LATE_EVENT_PATH, SojEvent.class);
  }

  public static StreamingFileSink jetstreamSojEventSinkWithParquet() {
    return HdfsConnectorFactory
        .createWithParquet(Constants.JETSTREAM_EVENT_PATH, JetStreamOutputEvent.class);
  }

  public static StreamingFileSink jetstreamSojSessionSinkWithParquet() {
    return HdfsConnectorFactory
        .createWithParquet(Constants.JETSTREAM_SESSION_PATH, JetStreamOutputSession.class);
  }
}
