package com.ebay.sojourner.dumper.pipeline;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.dumper.common.session.ByteToSojSessionMapFunction;
import com.ebay.sojourner.dumper.common.session.SplitSessionProcessFunction;
import com.ebay.sojourner.dumper.common.watermark.ExtractWatermarkProcessFunction;
import com.ebay.sojourner.flink.common.DataStreamRescaledBuilder;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.OutputTagConstants;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SojBoundedOutOfOrderlessTimestampExtractor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojournerSessionDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // rescaled kafka source
    DataStream<byte[]> rescaledByteSessionDataStream =
        DataStreamRescaledBuilder.buildKafkaSource(executionEnvironment, dc, byte[].class);

    // byte to sojsession
    DataStream<SojSession> sojSessionDataStream = rescaledByteSessionDataStream
        .map(new ByteToSojSessionMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.PASS_THROUGH_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.PASS_THROUGH_UID));

    // assgin watermark
    DataStream<SojSession> assignedWatermarkSojSessionDataStream = sojSessionDataStream
        .assignTimestampsAndWatermarks(new SojBoundedOutOfOrderlessTimestampExtractor<>(
            Time.milliseconds(0)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.ASSIGN_WATERMARK_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.ASSIGN_WATERMARK_UID));

    // extract timestamp
    DataStream<SojWatermark> sojSessionWatermarkStream = assignedWatermarkSojSessionDataStream
        .process(new ExtractWatermarkProcessFunction<>(
            FlinkEnvUtils.getString(Property.FLINK_APP_METRIC_NAME)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.TIMESTAMP_EXTRACT_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.TIMESTAMP_EXTRACT_UID));

    // sink timestamp to hdfs
    sojSessionWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), SojWatermark.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_WATERMARK))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_WATERMARK));

    SingleOutputStreamOperator<SojSession> sameDaySessionStream = sojSessionDataStream
        .process(new SplitSessionProcessFunction(OutputTagConstants.crossDaySessionOutputTag,
            OutputTagConstants.openSessionOutputTag))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SESSION_SPLIT_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.SESSION_SPLIT_UID));

    DataStream<SojSession> crossDaySessionStream = sameDaySessionStream
        .getSideOutput(OutputTagConstants.crossDaySessionOutputTag);

    DataStream<SojSession> openSessionStream = sameDaySessionStream
        .getSideOutput(OutputTagConstants.openSessionOutputTag);

    // same day session hdfs sink
    sameDaySessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_SAME_DAY_SESSION_DUMP_PATH), SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_SESSION_SAME_DAY))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_SESSION_SAME_DAY));

    // cross day session hdfs sink
    crossDaySessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_CROSS_DAY_SESSION_DUMP_PATH), SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_SESSION_CROSS_DAY))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_SESSION_CROSS_DAY));

    // open session hdfs sink
    openSessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_OPEN_SESSION_DUMP_PATH), SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_SESSION_OPEN))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_SESSION_OPEN));

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
