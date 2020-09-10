package com.ebay.sojourner.dumper.pipeline;

import com.ebay.sojourner.dumper.common.event.ExtractEventWatermarkProcessFunction;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerEventDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // kafka source
    SourceDataStreamBuilder dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, SojEvent.class
    );

    DataStream<SojEvent> sourceDataStream = dataStreamBuilder.buildOfDC(DataCenter.valueOf(dc));

    // extract timestamp
    DataStream<SojWatermark> sojEventWatermarkStream = sourceDataStream
        .process(new ExtractEventWatermarkProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("sojEvent timestamp extract")
        .uid("extract-timestamp-id");

    // sink timestamp to hdfs
    sojEventWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), SojWatermark.class))
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