package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.HdfsSinkUtil;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import org.apache.flink.api.java.utils.ParameterTool;
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
    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
    AppEnv.config(parameterTool);

    String sourceTopic = parameterTool.get("dump.source.topic");
    Class<?> deserializeClass = Class.forName(parameterTool.get("dump.source.class"));
    String hdfsPath = parameterTool.get("dump.hdfs.path");
    String groupId = parameterTool.get("dump.group.id");
    int parallelNum = Integer.parseInt(parameterTool.get("dump.parallel.number"));
    String bootstrapServers = Constants.BOOTSTRAP_SERVERS_SESSION;

    //Prepare execution environment
    final StreamExecutionEnvironment exeEnv = ExecutionEnvUtil.prepare(parameterTool);

    DataStream<SojSession> sourceDataStream = exeEnv.addSource(KafkaSourceFunction
        .buildSource(sourceTopic, bootstrapServers, groupId, deserializeClass))
        .setParallelism(parallelNum)
        .name(String.format("Rheos Kafka Consumer from topic: %s", sourceTopic))
        .uid("KafkaSource");
    sourceDataStream.addSink(HdfsSinkUtil.createWithParquet(hdfsPath, deserializeClass))
        .setParallelism(parallelNum)
        .name(String.format("Hdfs sink to location: %s", hdfsPath))
        .uid("HdfsSink")
        .disableChaining();
    exeEnv.execute();
  }
}
