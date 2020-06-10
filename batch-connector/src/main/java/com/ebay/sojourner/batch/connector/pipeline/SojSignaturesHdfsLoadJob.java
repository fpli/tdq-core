package com.ebay.sojourner.batch.connector.pipeline;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojSignaturesHdfsLoadJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String sourceIpTopic = FlinkEnvUtils.getString(Property.KAFKA_COMMON_CONSUMER_TOPIC_IP);
    String sourceAgentTopic = FlinkEnvUtils.getString(Property.KAFKA_COMMON_CONSUMER_TOPIC_AGENT);
    String sourceAgentIpTopic = FlinkEnvUtils
        .getString(Property.KAFKA_COMMON_CONSUMER_TOPIC_AGENT_IP);
    String sourceGuidTopic = FlinkEnvUtils.getString(Property.KAFKA_COMMON_CONSUMER_TOPIC_GUID);
    Class<?> deserializeClass = Class.forName(FlinkEnvUtils.getString(Property.HDFS_DUMP_CLASS));
    String hdfsPath = FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH);
    String groupIdIp = FlinkEnvUtils.getString(Property.KAFKA_COMMON_CONSUMER_GROUP_ID_IP);
    String groupIdAgent = FlinkEnvUtils.getString(Property.KAFKA_COMMON_CONSUMER_GROUP_ID_AGENT);
    String groupIdAgentIp = FlinkEnvUtils
        .getString(Property.KAFKA_COMMON_CONSUMER_GROUP_ID_AGENT_IP);
    String groupIdGuid = FlinkEnvUtils.getString(Property.KAFKA_COMMON_CONSUMER_GROUP_ID_GUID);
    int sourceIpParallelNum = FlinkEnvUtils.getInteger(Property.SOURCE_IP_PARALLELISM);
    int sourceAgentParallelNum = FlinkEnvUtils.getInteger(Property.SOURCE_AGENT_PARALLELISM);
    int sourceAgentIpParallelNum = FlinkEnvUtils.getInteger(Property.SOURCE_AGENT_IP_PARALLELISM);
    int sourceGuidParallelNum = FlinkEnvUtils.getInteger(Property.SOURCE_GUID_PARALLELISM);
    int sinkParallelNum = FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM);
    String bootstrapServers = FlinkEnvUtils
        .getString(Property.KAFKA_COMMON_CONSUMER_BROKERS_DEFAULT);

    // kafka source
    DataStream<BotSignature> sourceIpDataStream = SourceDataStreamBuilder
        .buildForSignature(executionEnvironment, sourceIpTopic, groupIdIp, sourceIpParallelNum);

    DataStream<BotSignature> sourceAgentDataStream = SourceDataStreamBuilder
        .buildForSignature(executionEnvironment, sourceAgentTopic, groupIdAgent,
            sourceAgentParallelNum);

    DataStream<BotSignature> sourceAgentIpDataStream = SourceDataStreamBuilder
        .buildForSignature(executionEnvironment, sourceAgentIpTopic, groupIdAgentIp,
            sourceAgentIpParallelNum);

    DataStream<BotSignature> sourceGuidDataStream = SourceDataStreamBuilder
        .buildForSignature(executionEnvironment, sourceGuidTopic, groupIdGuid,
            sourceGuidParallelNum);

    // union signature
    DataStream<BotSignature> signatureDataStream = sourceIpDataStream
        .union(sourceAgentDataStream)
        .union(sourceAgentIpDataStream)
        .union(sourceGuidDataStream);

    // hdfs sink
    signatureDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, deserializeClass))
        .setParallelism(sinkParallelNum)
        .name(String.format("Hdfs Sink To Location: %s", hdfsPath))
        .uid("signature-sink-id");

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.NAME_HDFS_DUMP_PIPELINE));
  }
}
