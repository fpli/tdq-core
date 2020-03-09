package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.avro.ParquetAvroWriters;
import org.apache.flink.streaming.api.functions.sink.filesystem.SojHdfsSinkWithKeytab;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

public class StreamingFileSinkFactory {

  public static String BASE_DIR = "viewfs:///user/o_ubi/";

  public static String eventSinkPath = BASE_DIR + "events";
  public static String sessionSinkPath = BASE_DIR + "sessions";
  public static String lateEventSinkPath = BASE_DIR + "events-late";
  public static String ipSignatureSinkPath = BASE_DIR + "ip-signature";

  public static <T> StreamingFileSink create(String sinkPath) {
    return StreamingFileSink.forRowFormat(new Path(sinkPath), new SimpleStringEncoder<T>("UTF-8"))
        .build();
  }

  public static <T> SojHdfsSinkWithKeytab createSojHdfs(String sinkPath) {

    StreamingFileSink streamingFileSink =
        StreamingFileSink.forBulkFormat(
            new Path(sinkPath), ParquetAvroWriters.forReflectRecord(IpSignature.class))
            .build();
    return new SojHdfsSinkWithKeytab(streamingFileSink);
  }

  public static StreamingFileSink<IpSignature> createWithAP(String sinkPath) {
    return StreamingFileSink.forBulkFormat(
        new Path(sinkPath), ParquetAvroWriters.forReflectRecord(IpSignature.class))
        .build();
  }

  public static <T> SojHdfsSinkWithKeytab createWithParquet(String sinkPath, Class<T> sinkClass) {
    StreamingFileSink streamingFileSink =
        StreamingFileSink.forBulkFormat(
            new Path(sinkPath), ParquetAvroWriters.forReflectRecord(sinkClass))
            .build();
    return new SojHdfsSinkWithKeytab(streamingFileSink);
  }

  public static StreamingFileSink eventSink() {
    return StreamingFileSinkFactory.<UbiEvent>create(eventSinkPath);
  }

  public static SojHdfsSinkWithKeytab eventSinkWithSojHdfs() {
    return StreamingFileSinkFactory.createWithParquet(eventSinkPath, UbiEvent.class);
  }

  public static SojHdfsSinkWithKeytab sessionSinkWithSojHdfs() {
    return StreamingFileSinkFactory.createWithParquet(
        sessionSinkPath, UbiSession.class);
  }

  public static SojHdfsSinkWithKeytab sessionSinkWithStrinf() {
    return StreamingFileSinkFactory.<UbiEvent>createSojHdfs(sessionSinkPath);
  }

  public static StreamingFileSink sessionSink() {
    return StreamingFileSinkFactory.<UbiSession>create(sessionSinkPath);
  }

  public static StreamingFileSink lateEventSink() {
    return StreamingFileSinkFactory.<UbiEvent>create(lateEventSinkPath);
  }

  public static StreamingFileSink ipSignatureSink() {
    return StreamingFileSinkFactory.<IpSignature>create(ipSignatureSinkPath);
  }

  public static StreamingFileSink<IpSignature> ipSignatureSinkWithAP() {
    return StreamingFileSinkFactory.createWithAP(ipSignatureSinkPath);
  }
}
