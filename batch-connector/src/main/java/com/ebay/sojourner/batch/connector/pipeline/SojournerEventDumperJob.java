package com.ebay.sojourner.batch.connector.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;

import com.ebay.sojourner.batch.connector.common.ExtractEventWatermarkProcessFunction;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerEventDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // kafka source
    SourceDataStreamBuilder dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, SojEvent.class
    );

    DataStream<SojEvent> sourceDataStream = dataStreamBuilder.buildOfDC(RNO);

    // extract timestamp
    DataStream<Long> sojEventWatermarkStream = sourceDataStream
        .process(new ExtractEventWatermarkProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("sojEvent timestamp extract")
        .uid("extract-timestamp-id");

    // sink timestamp to hdfs
    sojEventWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), Long.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH)))
        .uid("sink-timestamp-id");

    // hdfs sink
    sourceDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH), SojEvent.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM))
        .name(String.format("Hdfs Sink To Location: %s",
            FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH)))
        .uid("sink-id");

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
