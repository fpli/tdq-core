package com.ebay.sojourner.dumper.pipeline;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.dumper.common.event.ByteToSojEventMapFunction;
import com.ebay.sojourner.dumper.common.watermark.ExtractWatermarkProcessFunction;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.common.util.DataStreamRescaledBuilder;
import com.ebay.sojourner.flink.connector.kafka.SojBoundedOutOfOrderlessTimestampExtractor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojournerEventDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // rescaled kafka source
    DataStream<byte[]> rescaledByteEventDataStream =
        DataStreamRescaledBuilder.buildKafkaSource(executionEnvironment, dc, byte[].class);

    // byte to sojevent
    DataStream<SojEvent> sojEventDataStream = rescaledByteEventDataStream
        .map(new ByteToSojEventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.PASS_THROUGH_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.PASS_THROUGH_UID));

    // assgin watermark
    DataStream<SojEvent> assignedWatermarkSojEventDataStream = sojEventDataStream
        .assignTimestampsAndWatermarks(new SojBoundedOutOfOrderlessTimestampExtractor<>(
            Time.milliseconds(0)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.ASSIGN_WATERMARK_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.ASSIGN_WATERMARK_UID));

    // extract timestamp
    DataStream<SojWatermark> sojEventWatermarkStream = assignedWatermarkSojEventDataStream
        .process(new ExtractWatermarkProcessFunction<>(
            FlinkEnvUtils.getString(Property.FLINK_APP_METRIC_NAME)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.TIMESTAMP_EXTRACT_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.TIMESTAMP_EXTRACT_UID));

    // sink timestamp to hdfs
    sojEventWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), SojWatermark.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_WATERMARK))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_WATERMARK));

    // hdfs sink
    sojEventDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH), SojEvent.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_EVENT))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_EVENT));

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
