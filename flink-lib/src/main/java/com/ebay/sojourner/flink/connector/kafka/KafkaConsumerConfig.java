package com.ebay.sojourner.flink.connector.kafka;

import static com.ebay.sojourner.common.util.Property.KAFKA_CONSUMER_BOOTSTRAP_SERVERS;
import static com.ebay.sojourner.common.util.Property.KAFKA_CONSUMER_GROUP_ID;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import java.util.List;
import java.util.Properties;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;

@Data
public class KafkaConsumerConfig {

  private KafkaConsumerConfig() {}

  private List<String> topicList;
  private String brokers;
  private String groupId;
  private int outOfOrderlessInMin;
  private long fromTimestamp;
  private Properties properties;

public static KafkaConsumerConfig ofDC(DataCenter dataCenter) {
    KafkaConsumerConfig config = new KafkaConsumerConfig();

    final List<String> topics = FlinkEnvUtils.getList(Property.KAFKA_CONSUMER_TOPIC);

    if (CollectionUtils.isNotEmpty(topics)) {
      config.setTopicList(topics);
    } else {
      throw new IllegalStateException("the topics size is null");
    }

    switch (dataCenter) {
      case LVS:
        config.setBrokers(getBrokersForDC(DataCenter.LVS));
        config.setGroupId(getGroupIdForDC(DataCenter.LVS));
        break;
      case RNO:
        config.setBrokers(getBrokersForDC(DataCenter.RNO));
        config.setGroupId(getGroupIdForDC(DataCenter.RNO));
        break;
      case SLC:
        config.setBrokers(getBrokersForDC(DataCenter.SLC));
        config.setGroupId(getGroupIdForDC(DataCenter.SLC));
        break;
      default:
        throw new IllegalStateException("Cannot find datacenter kafka bootstrap servers");
    }

    config.setProperties(buildKafkaConsumerConfig(config.getBrokers(), config.getGroupId()));

    return config;
  }

  private static String getBrokersForDC(DataCenter dc) {
    String propKey = KAFKA_CONSUMER_BOOTSTRAP_SERVERS + "." + dc.getValue().toLowerCase();
    return FlinkEnvUtils.getListString(propKey);
  }

  private static String getGroupIdForDC(DataCenter dc) {
    String propKey = KAFKA_CONSUMER_GROUP_ID + "." + dc.getValue().toLowerCase();
    return FlinkEnvUtils.getString(propKey);
  }

  private static Properties buildKafkaConsumerConfig(String brokers, String groupId) {

    Properties consumerConfig = KafkaCommonConfig.get();

    consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                       FlinkEnvUtils.getInteger(Property.MAX_POLL_RECORDS));
    consumerConfig.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG,
                       FlinkEnvUtils.getInteger(Property.RECEIVE_BUFFER));
    consumerConfig.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,
                       FlinkEnvUtils.getInteger(Property.FETCH_MAX_BYTES));
    consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,
                       FlinkEnvUtils.getInteger(Property.FETCH_MAX_WAIT_MS));
    consumerConfig.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                       FlinkEnvUtils.getInteger(Property.MAX_PARTITIONS_FETCH_BYTES));
    consumerConfig.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
                       RoundRobinAssignor.class.getName());
    consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                       FlinkEnvUtils.getString(Property.AUTO_RESET_OFFSET));
    return consumerConfig;
  }
}
