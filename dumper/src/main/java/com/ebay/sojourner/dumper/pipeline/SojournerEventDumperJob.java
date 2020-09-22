package com.ebay.sojourner.dumper.pipeline;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojWatermark;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.dumper.common.ExtractWatermarkProcessFunction;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerEventDumperJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // kafka source
    SourceDataStreamBuilder<SojEvent> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, SojEvent.class
    );

    DataStream<SojEvent> sourceDataStream = dataStreamBuilder
        .buildOfDC(DataCenter.valueOf(dc), FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME),
            FlinkEnvUtils.getString(Property.SOURCE_UID));

    // extract timestamp
    DataStream<SojWatermark> sojEventWatermarkStream = sourceDataStream
        .process(new ExtractWatermarkProcessFunction<>(
            FlinkEnvUtils.getString(Property.FLINK_APP_METRIC_NAME)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("SojEvent Timestamp Extract")
        .uid("sojevent-timestamp-extract");

    // sink timestamp to hdfs
    sojEventWatermarkStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_DUMP_WATERMARK_PATH), SojWatermark.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME_WATERMARK))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID_WATERMARK));

    // hdfs sink
    sourceDataStream
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
