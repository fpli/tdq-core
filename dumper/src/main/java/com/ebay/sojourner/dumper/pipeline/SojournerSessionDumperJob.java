package com.ebay.sojourner.dumper.pipeline;

import com.ebay.sojourner.dumper.common.session.ExtractSessionWatermarkProcessFunction;
import com.ebay.sojourner.dumper.common.session.SplitSessionProcessFunction;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import com.ebay.sojourner.flink.common.util.OutputTagConstants;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerSessionDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // kafka source
    SourceDataStreamBuilder dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, SojSession.class
    );

    DataStream<SojSession> sourceDataStream = dataStreamBuilder.buildOfDC(DataCenter.valueOf(dc));

    // extract timestamp
    DataStream<SojWatermark> sojSessionWatermarkStream = sourceDataStream
        .process(new ExtractSessionWatermarkProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("sojSession timestamp extract")
        .uid("extract-timestamp-id");

    // sink timestamp to hdfs
    sojSessionWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), SojWatermark.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH)))
        .uid("sink-timestamp-id");

    SingleOutputStreamOperator<SojSession> sameDaySessionStream = sourceDataStream
        .process(new SplitSessionProcessFunction(OutputTagConstants.crossDaySessionOutputTag,
            OutputTagConstants.openSessionOutputTag))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("split session")
        .uid("split-session-id");

    DataStream<SojSession> crossDaySessionStream = sameDaySessionStream
        .getSideOutput(OutputTagConstants.crossDaySessionOutputTag);

    DataStream<SojSession> openSessionStream = sameDaySessionStream
        .getSideOutput(OutputTagConstants.openSessionOutputTag);

    // same day session hdfs sink
    sameDaySessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_SAME_DAY_SESSION_DUMP_PATH), SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_SAME_DAY_SESSION_DUMP_PATH)))
        .uid("same-day-session-sink-id");

    // cross day session hdfs sink
    crossDaySessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_CROSS_DAY_SESSION_DUMP_PATH), SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_CROSS_DAY_SESSION_DUMP_PATH)))
        .uid("cross-day-session-sink-id");

    // open session hdfs sink
    openSessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_OPEN_SESSION_DUMP_PATH), SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_OPEN_SESSION_DUMP_PATH)))
        .uid("open-session-sink-id");

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
