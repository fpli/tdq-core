package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.SojEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.util.Preconditions;

public class SojEventDateTimeBucketAssigner implements BucketAssigner<SojEvent, String> {

  private static final long serialVersionUID = 1L;

  private static final String DEFAULT_FORMAT_STRING = "yyyyMMdd/HH";

  private final String formatString;

  private final ZoneId zoneId;

  private transient DateTimeFormatter dateTimeFormatter;

  public SojEventDateTimeBucketAssigner(ZoneId zoneId) {
    this(DEFAULT_FORMAT_STRING, zoneId);
  }

  public SojEventDateTimeBucketAssigner(String formatString, ZoneId zoneId) {
    this.formatString = Preconditions.checkNotNull(formatString);
    this.zoneId = Preconditions.checkNotNull(zoneId);
  }

  @Override
  public String getBucketId(SojEvent record, Context context) {
    String defaultTsStr;
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern(formatString).withZone(zoneId);
    }
    defaultTsStr = dateTimeFormatter.format(Instant.ofEpochMilli(record.getEventTimestamp()));
    return "dt=" + defaultTsStr.substring(0, 8) + "/hr=" + defaultTsStr.substring(9);
  }

  @Override
  public SimpleVersionedSerializer<String> getSerializer() {
    return SimpleVersionedStringSerializer.INSTANCE;
  }

  @Override
  public String toString() {
    return "TdqMetricDateTimeBucketAssigner{"
        + "formatString='"
        + formatString
        + '\''
        + ", zoneId="
        + zoneId
        + '}';
  }
}
