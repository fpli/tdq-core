package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.TdqAvroMetric;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.util.Preconditions;

public class TdqMetricDateTimeBucketAssigner implements BucketAssigner<TdqAvroMetric, String> {

  private static final long serialVersionUID = 1L;

  private static final String DEFAULT_FORMAT_STRING = "yyyyMMdd/HH";

  private final String formatString;

  private final ZoneId zoneId;

  private transient DateTimeFormatter dateTimeFormatter;

  public TdqMetricDateTimeBucketAssigner() {
    this(DEFAULT_FORMAT_STRING);
  }

  public TdqMetricDateTimeBucketAssigner(String formatString) {
    this(formatString, ZoneId.of("-7"));
  }

  public TdqMetricDateTimeBucketAssigner(String formatString, ZoneId zoneId) {
    this.formatString = Preconditions.checkNotNull(formatString);
    this.zoneId = Preconditions.checkNotNull(zoneId);
  }

  @Override
  public String getBucketId(TdqAvroMetric metric, Context context) {
    String defaultTsStr;
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern(formatString).withZone(zoneId);
    }
    defaultTsStr = dateTimeFormatter.format(Instant.ofEpochMilli(metric.getEventTime()));
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
