package com.ebay.sojourner.batch.connector.pipeline;

import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.Constants;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.KafkaSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * This is a common class used to dump kafka topic to hdfs.
 * Usage:
 * --dump.source.topic behavior.total.new.sojevent
 * --dump.source.class com.ebay.sojourner.ubd.common.model.SojEvent
 * --dump.hdfs.path hdfs://apollo-rno/sys/soj/ubd/events
 * --dump.group.id sojourner-pathfinder-event-dump
 * --dump.parallel.number 200
 */
public class SojournerKafkaToHdfsJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String sourceTopic = FlinkEnvUtils.getString("dump.source.topic");
    Class<?> deserializeClass = Class.forName(FlinkEnvUtils.getString("dump.source.class"));
    String hdfsPath = FlinkEnvUtils.getString("dump.hdfs.path");
    String groupId = FlinkEnvUtils.getString("dump.group.id");
    int sourceParallelNum = FlinkEnvUtils.getInteger("dump.source.parallel.number");
    int sinkParallelNum = FlinkEnvUtils.getInteger("dump.sink.parallel.number");
    String bootstrapServers = FlinkEnvUtils
        .getListString(Constants.BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT);

    DataStream sourceDataStream = executionEnvironment
        .addSource(KafkaSourceFunction
            .buildSource(sourceTopic, bootstrapServers, groupId, deserializeClass))
        .setParallelism(sourceParallelNum)
        .name(String.format("Rheos Kafka Consumer from topic: %s", sourceTopic))
        .uid("non-bot-source-id");

    sourceDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, deserializeClass))
        .setParallelism(sinkParallelNum)
        .name(String.format("Hdfs sink to location: %s", hdfsPath))
        .uid("sink-id");

    FlinkEnvUtils.execute(executionEnvironment, FlinkEnvUtils.getString(Constants.NAME_HDFS_DUMP));
  }
}
