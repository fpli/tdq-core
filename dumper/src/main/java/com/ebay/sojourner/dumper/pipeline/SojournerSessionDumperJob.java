package com.ebay.sojourner.dumper.pipeline;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_FROM_TIMESTAMP;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getLong;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.dumper.common.session.ByteToSojSessionMapFunction;
import com.ebay.sojourner.dumper.common.session.SplitSessionProcessFunction;
import com.ebay.sojourner.dumper.common.watermark.ExtractWatermarkProcessFunction;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.OutputTagConstants;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SojBoundedOutOfOrderlessTimestampExtractor;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.flink.connector.kafka.schema.PassThroughDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaDeserializationSchemaWrapper;

public class SojournerSessionDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = getString(Property.FLINK_APP_SOURCE_DC);

    // rescaled kafka source
    SourceDataStreamBuilder<byte[]> dataStreamBuilder =
        new SourceDataStreamBuilder<>(executionEnvironment);

    DataStream<byte[]> rescaledByteSessionDataStream = dataStreamBuilder
        .dc(DataCenter.of(dc))
        .operatorName(getString(Property.SOURCE_OPERATOR_NAME))
        .uid(getString(Property.SOURCE_UID))
        .fromTimestamp(getLong(FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .buildRescaled(
            new KafkaDeserializationSchemaWrapper<>(new PassThroughDeserializationSchema()));

    // byte to sojsession
    DataStream<SojSession> sojSessionDataStream = rescaledByteSessionDataStream
        .map(new ByteToSojSessionMapFunction())
        .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(getString(Property.PASS_THROUGH_OPERATOR_NAME))
        .uid(getString(Property.PASS_THROUGH_UID));

    // assgin watermark
    DataStream<SojSession> assignedWatermarkSojSessionDataStream = sojSessionDataStream
        .assignTimestampsAndWatermarks(new SojBoundedOutOfOrderlessTimestampExtractor<>(
            Time.milliseconds(0)))
        .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(getString(Property.ASSIGN_WATERMARK_OPERATOR_NAME))
        .uid(getString(Property.ASSIGN_WATERMARK_UID));

    // extract timestamp
    DataStream<SojWatermark> sojSessionWatermarkStream = assignedWatermarkSojSessionDataStream
        .process(new ExtractWatermarkProcessFunction<>(
            getString(Property.FLINK_APP_METRIC_NAME)))
        .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(getString(Property.TIMESTAMP_EXTRACT_OPERATOR_NAME))
        .uid(getString(Property.TIMESTAMP_EXTRACT_UID));

    // sink timestamp to hdfs
    sojSessionWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            getString(Property.FLINK_APP_SINK_HDFS_WATERMARK_PATH), SojWatermark.class))
        .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(getString(Property.SINK_OPERATOR_NAME_WATERMARK))
        .uid(getString(Property.SINK_UID_WATERMARK));

    SingleOutputStreamOperator<SojSession> sameDaySessionStream =
        assignedWatermarkSojSessionDataStream
            .process(new SplitSessionProcessFunction(OutputTagConstants.crossDaySessionOutputTag,
                OutputTagConstants.openSessionOutputTag))
            .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
            .name(getString(Property.SESSION_SPLIT_OPERATOR_NAME))
            .uid(getString(Property.SESSION_SPLIT_UID));

    DataStream<SojSession> crossDaySessionStream = sameDaySessionStream
        .getSideOutput(OutputTagConstants.crossDaySessionOutputTag);

    DataStream<SojSession> openSessionStream = sameDaySessionStream
        .getSideOutput(OutputTagConstants.openSessionOutputTag);

    // same day session hdfs sink
    sameDaySessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            getString(Property.FLINK_APP_SINK_HDFS_SAME_DAY_SESSION_PATH), SojSession.class))
        .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(getString(Property.SINK_OPERATOR_NAME_SESSION_SAME_DAY))
        .uid(getString(Property.SINK_UID_SESSION_SAME_DAY));

    // cross day session hdfs sink
    crossDaySessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            getString(Property.FLINK_APP_SINK_HDFS_CROSS_DAY_SESSION_PATH), SojSession.class))
        .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(getString(Property.SINK_OPERATOR_NAME_SESSION_CROSS_DAY))
        .uid(getString(Property.SINK_UID_SESSION_CROSS_DAY));

    // open session hdfs sink
    openSessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            getString(Property.FLINK_APP_SINK_HDFS_OPEN_SESSION_PATH), SojSession.class))
        .setParallelism(getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(getString(Property.SINK_OPERATOR_NAME_SESSION_OPEN))
        .uid(getString(Property.SINK_UID_SESSION_OPEN));

    // submit job
    FlinkEnvUtils.execute(executionEnvironment, getString(Property.FLINK_APP_NAME));
  }
}
