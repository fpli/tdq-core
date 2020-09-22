package com.ebay.sojourner.dumper.pipeline;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.dumper.common.ExtractWatermarkProcessFunction;
import com.ebay.sojourner.dumper.common.SplitSessionProcessFunction;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import com.ebay.sojourner.flink.common.util.OutputTagConstants;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerSessionDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // kafka source
    SourceDataStreamBuilder<SojSession> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, SojSession.class
    );

    DataStream<SojSession> sourceDataStream = dataStreamBuilder
        .buildOfDC(DataCenter.valueOf(dc), FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME),
            FlinkEnvUtils.getString(Property.SOURCE_UID));

    // extract timestamp
    DataStream<SojWatermark> sojSessionWatermarkStream = sourceDataStream
        .process(new ExtractWatermarkProcessFunction<>(
            FlinkEnvUtils.getString(Property.FLINK_APP_METRIC_NAME)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("SojSession Timestamp Extract")
        .uid("sojsession-timestamp-extract");

    // sink timestamp to hdfs
    sojSessionWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), SojWatermark.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_WATERMARK))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_WATERMARK));

    SingleOutputStreamOperator<SojSession> sameDaySessionStream = sourceDataStream
        .process(new SplitSessionProcessFunction(OutputTagConstants.crossDaySessionOutputTag,
            OutputTagConstants.openSessionOutputTag))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("SojSession Split")
        .uid("sojsession-split");

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
