package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.flink.connector.kafka.AvroKafkaSerializer;
import com.ebay.sojourner.flink.connector.kafka.KafkaSerializer;
import java.util.List;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.Nullable;

public class AvroKafkaSerializationSchema<T extends SpecificRecord> implements
    KafkaSerializationSchema<T> {

  public final String defaultTopic;
  private final Schema schema;
  private final List<String> keyFields;
  private transient KafkaSerializer<T> kafkaSerializer;

  public AvroKafkaSerializationSchema(String defaultTopic, Schema schema, List<String> keyFields) {
    this.defaultTopic = defaultTopic;
    this.schema = schema;
    this.keyFields = keyFields;
  }

  @Override
  public ProducerRecord<byte[], byte[]> serialize(T element, @Nullable Long timestamp) {
    if (kafkaSerializer == null) {
      kafkaSerializer = new AvroKafkaSerializer<>(schema);
    }
    return new ProducerRecord<>(defaultTopic,
                                kafkaSerializer.encodeKey(element, keyFields),
                                kafkaSerializer.encodeValue(element));
  }
}
