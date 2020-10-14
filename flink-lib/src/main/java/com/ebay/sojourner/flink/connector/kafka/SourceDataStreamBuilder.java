package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;

public class SourceDataStreamBuilder<T> {

  private final StreamExecutionEnvironment environment;
  private DataCenter dc;
  private String operatorName;
  private String uid;
  private String slotGroup;
  private boolean rescaled;

  public SourceDataStreamBuilder(StreamExecutionEnvironment environment) {
    this.environment = environment;
  }

  public SourceDataStreamBuilder<T> dc(DataCenter dc) {
    this.dc = dc;
    return this;
  }

  public SourceDataStreamBuilder<T> operatorName(String operatorName) {
    this.operatorName = operatorName;
    return this;
  }

  public SourceDataStreamBuilder<T> uid(String uid) {
    this.uid = uid;
    return this;
  }

  public SourceDataStreamBuilder<T> slotGroup(String slotGroup) {
    this.slotGroup = slotGroup;
    return this;
  }

  public SourceDataStreamBuilder<T> rescaled(boolean rescaled) {
    this.rescaled = rescaled;
    return this;
  }

  public DataStream<T> build(DeserializationSchema<T> schema) {
    return this.build(schema, dc, operatorName, uid, slotGroup, rescaled);
  }

  public DataStream<T> build(KafkaDeserializationSchema<T> schema) {
    return this.build(schema, dc, operatorName, uid, slotGroup, rescaled);
  }

  public DataStream<T> buildRescaled(DeserializationSchema<T> schema) {
    return this.build(schema, dc, operatorName, uid, slotGroup, true);
  }

  public DataStream<T> build(DeserializationSchema<T> schema, DataCenter dc,
                             String operatorName, String uid, String slotGroup, boolean rescaled) {

    KafkaConsumerConfig kafkaConsumerConfig = KafkaConnectorFactory.getKafkaConsumerConfig(dc);

    KafkaSourceFunctionBuilder<T> kafkaSourceFunctionBuilder =
        new KafkaSourceFunctionBuilder<>(kafkaConsumerConfig);

    DataStream<T> dataStream = environment
        .addSource(kafkaSourceFunctionBuilder.build(schema))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(operatorName)
        .uid(uid);

    if (rescaled) {
      return dataStream.rescale();
    }

    return dataStream;
  }

  public DataStream<T> build(KafkaDeserializationSchema<T> schema, DataCenter dc,
                             String operatorName, String uid, String slotGroup, boolean rescaled) {

    KafkaConsumerConfig kafkaConsumerConfig = KafkaConnectorFactory.getKafkaConsumerConfig(dc);

    KafkaSourceFunctionBuilder<T> kafkaSourceFunctionBuilder =
        new KafkaSourceFunctionBuilder<>(kafkaConsumerConfig);

    DataStream<T> dataStream = environment
        .addSource(kafkaSourceFunctionBuilder.build(schema))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(operatorName)
        .uid(uid);

    if (rescaled) {
      return dataStream.rescale();
    }

    return dataStream;
  }
}
