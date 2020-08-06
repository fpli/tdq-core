package com.ebay.sojourner.batch.connector.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;

import com.ebay.sojourner.batch.connector.common.ExtractSessionWatermarkProcessFunction;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerSessionDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // kafka source
    SourceDataStreamBuilder dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, SojSession.class
    );

    DataStream<SojSession> sourceDataStream = dataStreamBuilder.buildOfDC(RNO);

    // extract timestamp
    DataStream<Long> sojSessionWatermarkStream = sourceDataStream
        .process(new ExtractSessionWatermarkProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("sojSession timestamp extract")
        .uid("extract-timestamp-id");

    // sink timestamp to hdfs
    sojSessionWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), Long.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH)))
        .uid("sink-timestamp-id");

    // hdfs sink
    sourceDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH), SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH)))
        .uid("sink-id");

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
