package com.ebay.sojourner.batch.connector.pipeline;

import com.ebay.sojourner.batch.connector.util.Constants;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JetstreamKafkaToHdfsJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String botSourceTopic = FlinkEnvUtils.getString(Constants.KAFKA_COMMON_CONSUMER_TOPIC_BOT);
    String nonBotSourceTopic = FlinkEnvUtils
        .getString(Constants.KAFKA_COMMON_CONSUMER_TOPIC_NON_BOT);
    Class<?> deserializeClass = Class.forName(FlinkEnvUtils.getString(Constants.HDFS_DUMP_CLASS));
    String hdfsPath = FlinkEnvUtils.getString(FlinkEnvUtils.getString(Constants.HDFS_DUMP_PATH));
    String botGroupId = FlinkEnvUtils.getString(Constants.KAFKA_COMMON_CONSUMER_GROUP_ID_BOT);
    String nonBotGroupId = FlinkEnvUtils
        .getString(Constants.KAFKA_COMMON_CONSUMER_GROUP_ID_NON_BOT);
    int botSourceParallelNum = FlinkEnvUtils.getInteger(Constants.SOURCE_BOT_PARALLELISM);
    int nonBotSourceParallelNum = FlinkEnvUtils.getInteger(Constants.SOURCE_NON_BOT_PARALLELISM);
    int sinkParallelNum = FlinkEnvUtils.getInteger(Constants.SINK_HDFS_PARALLELISM);
    String botBootstrapServers = FlinkEnvUtils
        .getString(Constants.KAFKA_COMMON_CONSUMER_BROKERS_BOT);
    String nonBotBootstrapServers = FlinkEnvUtils
        .getString(Constants.KAFKA_COMMON_CONSUMER_BROKERS_NON_BOT);
    String dc = FlinkEnvUtils.getString(com.ebay.sojourner.flink.common.util.Constants.RNO);

    // bot event kafka source
    DataStream jetStreamOutputBotEventDataStream = SourceDataStreamBuilder
        .build(executionEnvironment, botSourceTopic, botBootstrapServers, botGroupId, dc,
            botSourceParallelNum, null, deserializeClass);

    // non bot event kafka source
    DataStream jetStreamOutputNonBotEventDataStream = SourceDataStreamBuilder
        .build(executionEnvironment, nonBotSourceTopic, nonBotBootstrapServers, nonBotGroupId, dc,
            nonBotSourceParallelNum, null, deserializeClass);

    // union bot and non bot
    DataStream jetStreamOutputDataStream =
        jetStreamOutputBotEventDataStream.union(jetStreamOutputNonBotEventDataStream);

    // hdfs sink
    jetStreamOutputDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, deserializeClass))
        .setParallelism(sinkParallelNum)
        .name(String.format("Hdfs Sink To Location: %s", hdfsPath))
        .uid("event-sink-id");

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Constants.NAME_HDFS_DUMP_PIPELINE));
  }
}
