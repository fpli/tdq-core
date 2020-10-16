package com.ebay.sojourner.dumper.pipeline;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_FROM_TIMESTAMP;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.common.model.JetStreamOutputSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.flink.connector.kafka.schema.AvroKeyedDeserializationSchema;
import com.ebay.sojourner.flink.connector.kafka.schema.JetstreamEventDeserializationSchema;
import com.ebay.sojourner.flink.connector.kafka.schema.JetstreamSessionDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaDeserializationSchemaWrapper;

public class SojournerKafkaToHdfsJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    Class<?> deserializeClass = Class.forName(FlinkEnvUtils.getString(Property.HDFS_DUMP_CLASS));
    String hdfsPath = FlinkEnvUtils.getString(Property.HDFS_DUMP_PATH);
    int sinkParallelNum = FlinkEnvUtils.getInteger(Property.SINK_HDFS_PARALLELISM);
    String dc = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_DATA_CENTER);

    if (deserializeClass.isAssignableFrom(JetStreamOutputEvent.class)) {
      SourceDataStreamBuilder<JetStreamOutputEvent> dataStreamBuilder =
          new SourceDataStreamBuilder<>(executionEnvironment);
      DataStream<JetStreamOutputEvent> sourceDataStream = dataStreamBuilder
          .dc(DataCenter.valueOf(dc))
          .operatorName(FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME))
          .uid(FlinkEnvUtils.getString(Property.SOURCE_UID))
          .fromTimestamp(FlinkEnvUtils.getLong(FLINK_APP_SOURCE_FROM_TIMESTAMP))
          .buildRescaled(
              new KafkaDeserializationSchemaWrapper<>(new JetstreamEventDeserializationSchema()));
      // hdfs sink
      sourceDataStream
          .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, JetStreamOutputEvent.class))
          .setParallelism(sinkParallelNum)
          .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME))
          .uid(FlinkEnvUtils.getString(Property.SINK_UID));

    } else if (deserializeClass.isAssignableFrom(JetStreamOutputSession.class)) {

      SourceDataStreamBuilder<JetStreamOutputSession> dataStreamBuilder =
          new SourceDataStreamBuilder<>(executionEnvironment);
      DataStream<JetStreamOutputSession> sourceDataStream = dataStreamBuilder
          .dc(DataCenter.valueOf(dc))
          .operatorName(FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME))
          .uid(FlinkEnvUtils.getString(Property.SOURCE_UID))
          .fromTimestamp(FlinkEnvUtils.getLong(FLINK_APP_SOURCE_FROM_TIMESTAMP))
          .buildRescaled(
              new KafkaDeserializationSchemaWrapper<>(new JetstreamSessionDeserializationSchema()));

      // hdfs sink
      sourceDataStream
          .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, JetStreamOutputSession.class))
          .setParallelism(sinkParallelNum)
          .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME))
          .uid(FlinkEnvUtils.getString(Property.SINK_UID));
    } else if (deserializeClass.isAssignableFrom(BotSignature.class)) {

      SourceDataStreamBuilder<BotSignature> dataStreamBuilder =
          new SourceDataStreamBuilder<>(executionEnvironment);
      DataStream<BotSignature> sourceDataStream = dataStreamBuilder
          .dc(DataCenter.valueOf(dc))
          .operatorName(FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME))
          .uid(FlinkEnvUtils.getString(Property.SOURCE_UID))
          .fromTimestamp(FlinkEnvUtils.getLong(FLINK_APP_SOURCE_FROM_TIMESTAMP))
          .buildRescaled(new AvroKeyedDeserializationSchema<>(BotSignature.class));

      // hdfs sink
      sourceDataStream
          .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, BotSignature.class))
          .setParallelism(sinkParallelNum)
          .name(FlinkEnvUtils.getString(Property.SINK_OPERATOR_NAME))
          .uid(FlinkEnvUtils.getString(Property.SINK_UID));
    }

    // submit job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
