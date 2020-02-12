package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.util.Preconditions;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeBucketAssignerForEventTime<IN> implements BucketAssigner<IN, String> {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_FORMAT_STRING = "yyyy-MM-dd--HH";

    private final String formatString;

    private final ZoneId zoneId;

    private transient DateTimeFormatter dateTimeFormatter;

    public DateTimeBucketAssignerForEventTime() {
        this(DEFAULT_FORMAT_STRING);
    }

    public DateTimeBucketAssignerForEventTime(String formatString) {
        this(formatString, ZoneId.systemDefault());
    }

    public DateTimeBucketAssignerForEventTime(ZoneId zoneId) {
        this(DEFAULT_FORMAT_STRING, zoneId);
    }

    public DateTimeBucketAssignerForEventTime(String formatString, ZoneId zoneId) {
        this.formatString = Preconditions.checkNotNull(formatString);
        this.zoneId = Preconditions.checkNotNull(zoneId);
    }

    @Override
    public String getBucketId(IN element, BucketAssigner.Context context) {
        if (dateTimeFormatter == null) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(formatString).withZone(zoneId);
        }
        return dateTimeFormatter.format(Instant.ofEpochMilli(context.timestamp()));
    }

    @Override
    public SimpleVersionedSerializer<String> getSerializer() {
        return SimpleVersionedStringSerializer.INSTANCE;
    }

    @Override
    public String toString() {
        return "DateTimeBucketAssignerForEventTime{" +
                "formatString='" + formatString + '\'' +
                ", zoneId=" + zoneId +
                '}';
    }
}
