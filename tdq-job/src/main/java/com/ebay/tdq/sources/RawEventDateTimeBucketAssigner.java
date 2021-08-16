package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.RawEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.util.Preconditions;

public class RawEventDateTimeBucketAssigner implements BucketAssigner<RawEvent, String> {

  private static final long serialVersionUID = 1L;

  private static final String DEFAULT_FORMAT_STRING = "yyyyMMdd/HH";

  private final String formatString;

  private final ZoneId zoneId;

  private transient DateTimeFormatter dateTimeFormatter;

  public RawEventDateTimeBucketAssigner(ZoneId zoneId) {
    this(DEFAULT_FORMAT_STRING, zoneId);
  }

  public RawEventDateTimeBucketAssigner(String formatString, ZoneId zoneId) {
    this.formatString = Preconditions.checkNotNull(formatString);
    this.zoneId = Preconditions.checkNotNull(zoneId);
  }

  @Override
  public String getBucketId(RawEvent element, Context context) {
    String defaultTsStr;
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern(formatString).withZone(zoneId);
    }
    defaultTsStr = dateTimeFormatter.format(Instant.ofEpochMilli(element.getEventTimestamp()));
    return "dt=" + defaultTsStr.substring(0, 8) + "/hr=" + defaultTsStr.substring(9);
  }

  @Override
  public SimpleVersionedSerializer<String> getSerializer() {
    return SimpleVersionedStringSerializer.INSTANCE;
  }

}
