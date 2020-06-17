package com.ebay.sojourner.flink.connectors.kafka;

import static com.ebay.sojourner.common.util.Property.KAFKA_CONSUMER_BOOTSTRAP_SERVERS_RNO;

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
    KafkaConfig kafkaConsumerConfig = getKafkaConsumerConfig(dc);
    return environment
        .addSource(KafkaSourceFunction.buildSource(kafkaConsumerConfig, tClass))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(slotGroup)
        .name(String.format("Rheos Kafka Consumer From DC: %s, Topic: %s",
            dc, kafkaConsumerConfig.getTopic()))
        .uid(String.format("source-%s-%s-id", dc, kafkaConsumerConfig.getTopic()));
  }

  private KafkaConfig getKafkaConsumerConfig(DataCenter dataCenter) {
    final String topic = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_TOPIC);
    final String groupId = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_GROUP_ID);

    KafkaConfig kafkaConfig = KafkaConfig.builder()
        .topic(topic)
        .groupId(groupId)
        .build();

    switch (dataCenter) {
      case LVS:
        kafkaConfig.setBrokers(getBrokersForDC(DataCenter.LVS));
        break;
      case RNO:
        kafkaConfig.setBrokers(getBrokersForDC(DataCenter.RNO));
        break;
      case SLC:
        kafkaConfig.setBrokers(getBrokersForDC(DataCenter.SLC));
        break;
      default:
        throw new IllegalStateException("Cannot find datacenter kafka bootstrap servers");
    }

    return kafkaConfig;
  }

  private String getBrokersForDC(DataCenter dc) {
    String propKey = KAFKA_CONSUMER_BOOTSTRAP_SERVERS_RNO + "." + dc.getValue().toLowerCase();
    return FlinkEnvUtils.getListString(propKey);
  }
}
