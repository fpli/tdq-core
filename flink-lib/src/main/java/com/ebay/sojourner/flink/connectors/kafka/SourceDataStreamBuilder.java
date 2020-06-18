package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@Data
@AllArgsConstructor
public class SourceDataStreamBuilder<T> {

  private StreamExecutionEnvironment environment;
  private Class<T> tClass;

  public DataStream<T> buildOfDC(DataCenter dc) {
    return this.buildOfDC(dc, null);
  }

  public DataStream<T> buildOfDC(DataCenter dc, String slotGroup) {

    KafkaConfig kafkaConsumerConfig = KafkaConnectorUtils.getKafkaConsumerConfig(dc);
    return environment
        .addSource(KafkaSourceFunction.buildSource(kafkaConsumerConfig, tClass))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(String.format("Rheos Kafka Consumer From DC: %s, Topic: %s",
            dc, kafkaConsumerConfig.getTopic()))
        .uid(String.format("source-%s-%s-id", dc, kafkaConsumerConfig.getTopic()));
  }

  public DataStream<T> buildOfDC(DataCenter dc, String slotGroup, int parallelism) {

    KafkaConfig kafkaConsumerConfig = KafkaConnectorUtils.getKafkaConsumerConfig(dc);
    return environment
        .addSource(KafkaSourceFunction.buildSource(kafkaConsumerConfig, tClass))
        .setParallelism(parallelism)
        .slotSharingGroup(slotGroup)
        .name(String.format("Rheos Kafka Consumer From DC: %s, Topic: %s",
            dc, kafkaConsumerConfig.getTopic()))
        .uid(String.format("source-%s-%s-id", dc, kafkaConsumerConfig.getTopic()));
  }
}
