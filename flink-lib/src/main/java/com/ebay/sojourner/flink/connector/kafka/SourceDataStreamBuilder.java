package com.ebay.sojourner.flink.connector.kafka;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.DataCenter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;

public class SourceDataStreamBuilder<T> {

  private final StreamExecutionEnvironment environment;
  private DataCenter dc;
  private String operatorName;
  private String uid;
  private String slotGroup;
  private int parallelism = getInteger(Property.SOURCE_PARALLELISM);
  private int outOfOrderlessInMin;
  private long fromTimestamp;
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

  public SourceDataStreamBuilder<T> parallelism(int parallelism) {
    this.parallelism = parallelism;
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

  public SourceDataStreamBuilder<T> outOfOrderlessInMin(int outOfOrderlessInMin) {
    this.outOfOrderlessInMin = outOfOrderlessInMin;
    return this;
  }

  public SourceDataStreamBuilder<T> fromTimestamp(long fromTimestamp) {
    this.fromTimestamp = fromTimestamp;
    return this;
  }

  public DataStream<T> build(KafkaDeserializationSchema<T> schema) {
    return this.build(schema, dc, operatorName, parallelism, uid, slotGroup, rescaled);
  }

  public DataStream<T> buildRescaled(KafkaDeserializationSchema<T> schema) {
    return this.build(schema, dc, operatorName, parallelism, uid, slotGroup, true);
  }

  public DataStream<T> build(KafkaDeserializationSchema<T> schema, DataCenter dc,
                             String operatorName, int parallelism, String uid, String slotGroup,
                             boolean rescaled) {

    KafkaConsumerConfig config = KafkaConsumerConfig.ofDC(dc);
    FlinkKafkaSourceConfigWrapper configWrapper = new FlinkKafkaSourceConfigWrapper(
        config, outOfOrderlessInMin, fromTimestamp);
    FlinkKafkaConsumerFactory factory = new FlinkKafkaConsumerFactory(configWrapper);

    DataStream<T> dataStream = environment
        .addSource(factory.get(schema))
        .setParallelism(parallelism)
        .slotSharingGroup(slotGroup)
        .name(operatorName)
        .uid(uid);

    if (rescaled) {
      return dataStream.rescale();
    }

    return dataStream;
  }
}
