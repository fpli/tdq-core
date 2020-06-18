package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.schema.AvroKeyedSerializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojBytesEventSerializationSchema;
import java.util.Optional;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaProducerFactory {

  public static <T> FlinkKafkaProducer<T> getProducer(String topic, String brokers,
      String messagekey, Class<T> tClass) {

    Properties producerConfig = KafkaConnectorUtils.getKafkaCommonConfig();

    producerConfig
        .put(ProducerConfig.BATCH_SIZE_CONFIG, FlinkEnvUtils.getInteger(Property.BATCH_SIZE));
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);

    if (tClass == null && messagekey == null) {
      return new FlinkKafkaProducer<>(topic, new SojBytesEventSerializationSchema<>(),
          producerConfig, Optional.of(new SojKafkaPartitioner<>()));
    } else {
      return new FlinkKafkaProducer<>(topic,
          new AvroKeyedSerializationSchema<>(tClass, messagekey), producerConfig,
          Optional.of(new SojKafkaPartitioner<>()));
    }
  }
}
