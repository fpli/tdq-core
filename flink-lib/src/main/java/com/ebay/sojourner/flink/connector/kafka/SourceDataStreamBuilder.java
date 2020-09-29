package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@Data
@AllArgsConstructor
public class SourceDataStreamBuilder<T> {

  private StreamExecutionEnvironment environment;
  private Class<T> tClass;

  public DataStream<T> buildForRealtime(DataCenter dc, String operatorName, String uid) {
    return this.buildForRealtime(dc, operatorName, uid, null);
  }

  public DataStream<T> buildForDumper(DataCenter dc, String operatorName, String uid) {
    return this.buildForDumper(dc, operatorName, uid, null);
  }

  public DataStream<T> buildForRealtime(DataCenter dc, String operatorName, String uid,
      String slotGroup) {

    KafkaConsumerConfig kafkaConsumerConfig = KafkaConnectorFactory.getKafkaConsumerConfig(dc);
    return environment
        .addSource(KafkaSourceFunction.buildSourceForRealtime(kafkaConsumerConfig, tClass))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(operatorName)
        .uid(uid);
  }

  public DataStream<T> buildForRealtime(DataCenter dc, String operatorName, String uid,
      String slotGroup, Set<String> guidList) {

    KafkaConsumerConfig kafkaConsumerConfig = KafkaConnectorFactory.getKafkaConsumerConfig(dc);
    return environment
        .addSource(
            KafkaSourceFunction.buildSourceForRealtime(kafkaConsumerConfig, tClass, guidList))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(operatorName)
        .uid(uid);
  }

  private DataStream<T> buildForDumper(DataCenter dc, String operatorName, String uid,
      String slotGroup) {

    KafkaConsumerConfig kafkaConsumerConfig = KafkaConnectorFactory.getKafkaConsumerConfig(dc);
    return environment
        .addSource(KafkaSourceFunction.buildSourceForDumper(kafkaConsumerConfig, tClass))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(operatorName)
        .uid(uid);
  }
}
