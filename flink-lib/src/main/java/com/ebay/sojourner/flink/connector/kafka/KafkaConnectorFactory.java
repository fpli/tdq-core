package com.ebay.sojourner.flink.connector.kafka;

import static com.ebay.sojourner.common.util.Property.KAFKA_CONSUMER_BOOTSTRAP_SERVERS;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.DataCenter;
import java.util.Properties;
import org.apache.kafka.common.config.SaslConfigs;

public class KafkaConnectorFactory {

  public static Properties getKafkaCommonConfig() {
    Properties props = new Properties();
    props.put("sasl.mechanism", "IAF");
    props.put("security.protocol", "SASL_PLAINTEXT");

    final String saslJaasConfig =
        String.format(
            "io.ebay.rheos.kafka.security.iaf.IAFLoginModule required iafConsumerId="
                + "\"urn:ebay-marketplace-consumerid:68a97ac2-013b-4915-9ed7-d6ae2ff01618\" "
                + "iafSecret=\"%s\" iafEnv=\"%s\";",
            FlinkEnvUtils.getString(Property.RHEOS_CLIENT_IAF_SECRET),
            FlinkEnvUtils.getString(Property.RHEOS_CLIENT_IAF_ENV));

    props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
    return props;
  }

  public static KafkaConsumerConfig getKafkaConsumerConfig(DataCenter dataCenter) {
    final String topic = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_TOPIC);
    final String groupId = FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_GROUP_ID);

    KafkaConsumerConfig kafkaConsumerConfig = KafkaConsumerConfig.builder()
        .topic(topic)
        .groupId(groupId)
        .build();

    switch (dataCenter) {
      case LVS:
        kafkaConsumerConfig.setBrokers(getBrokersForDC(DataCenter.LVS));
        break;
      case RNO:
        kafkaConsumerConfig.setBrokers(getBrokersForDC(DataCenter.RNO));
        break;
      case SLC:
        kafkaConsumerConfig.setBrokers(getBrokersForDC(DataCenter.SLC));
        break;
      default:
        throw new IllegalStateException("Cannot find datacenter kafka bootstrap servers");
    }

    return kafkaConsumerConfig;
  }

  private static String getBrokersForDC(DataCenter dc) {
    String propKey = KAFKA_CONSUMER_BOOTSTRAP_SERVERS + "." + dc.getValue().toLowerCase();
    return FlinkEnvUtils.getListString(propKey);
  }
}
