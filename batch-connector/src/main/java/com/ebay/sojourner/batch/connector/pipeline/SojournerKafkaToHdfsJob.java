package com.ebay.sojourner.batch.connector.pipeline;

import com.ebay.sojourner.batch.connector.util.Constants;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerKafkaToHdfsJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String sourceTopic = FlinkEnvUtils.getString(Constants.KAFKA_COMMON_CONSUMER_TOPIC_DEFAULT);
    Class<?> deserializeClass = Class.forName(FlinkEnvUtils.getString(Constants.HDFS_DUMP_CLASS));
    String hdfsPath = FlinkEnvUtils.getString(Constants.HDFS_DUMP_PATH);
    String groupId = FlinkEnvUtils.getString(Constants.KAFKA_COMMON_CONSUMER_GROUP_ID_DEFAULT);
    int sourceParallelNum = FlinkEnvUtils.getInteger(Constants.SOURCE_DEFAULT_PARALLELISM);
    int sinkParallelNum = FlinkEnvUtils.getInteger(Constants.SINK_HDFS_PARALLELISM);
    String bootstrapServers = FlinkEnvUtils
        .getString(Constants.KAFKA_COMMON_CONSUMER_BROKERS_DEFAULT);

    // kafka source
    DataStream sourceDataStream = SourceDataStreamBuilder
        .build(executionEnvironment, sourceTopic, bootstrapServers, groupId, DataCenter.RNO,
            sourceParallelNum, null, deserializeClass);

    // hdfs sink
    sourceDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, deserializeClass))
        .setParallelism(sinkParallelNum)
        .name(String.format("Hdfs Sink To Location: %s", hdfsPath))
        .uid("sink-id");

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Constants.NAME_HDFS_DUMP_PIPELINE));
  }
}
