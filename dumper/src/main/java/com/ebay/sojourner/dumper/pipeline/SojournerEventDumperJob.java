package com.ebay.sojourner.dumper.pipeline;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_FROM_TIMESTAMP;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.dumper.common.event.ByteToSojEventMapFunction;
import com.ebay.sojourner.dumper.common.watermark.ExtractWatermarkProcessFunction;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SojBoundedOutOfOrderlessTimestampExtractor;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.flink.connector.kafka.schema.PassThroughDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaDeserializationSchemaWrapper;

public class SojournerEventDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // rescaled kafka source
    SourceDataStreamBuilder<byte[]> dataStreamBuilder =
        new SourceDataStreamBuilder<>(executionEnvironment);

    DataStream<byte[]> rescaledByteEventDataStream = dataStreamBuilder
        .dc(DataCenter.valueOf(dc))
        .operatorName(FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.SOURCE_UID))
        .fromTimestamp(FlinkEnvUtils.getLong(FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .buildRescaled(
            new KafkaDeserializationSchemaWrapper<>(new PassThroughDeserializationSchema()));

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
