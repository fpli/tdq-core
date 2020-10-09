package com.ebay.sojourner.dumper.pipeline;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerKafkaToHdfsJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    Class<?> deserializeClass = Class.forName(FlinkEnvUtils.getString(Property.HDFS_DUMP_CLASS));
    String hdfsPath = FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH);
    int sinkParallelNum = FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM);
    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    // kafka source
    SourceDataStreamBuilder dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, deserializeClass
    );

    DataStream sourceDataStream = dataStreamBuilder
        .buildForRealtime(DataCenter.valueOf(dc),
            FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME),
            FlinkEnvUtils.getString(Property.SOURCE_UID));

    // hdfs sink
    sourceDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, deserializeClass))
        .setParallelism(sinkParallelNum)
        .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME))
        .uid(FlinkEnvUtils.getString(Property.SINK_UID));

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
