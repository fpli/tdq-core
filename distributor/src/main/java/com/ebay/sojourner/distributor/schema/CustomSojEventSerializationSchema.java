package com.ebay.sojourner.distributor.schema;


import com.ebay.sojourner.common.model.RawSojEventWrapper;
import com.ebay.sojourner.distributor.SojEventDispatcher;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.Nullable;

public class CustomSojEventSerializationSchema implements
    KafkaSerializationSchema<RawSojEventWrapper> {

  @Override
  public ProducerRecord<byte[], byte[]> serialize(RawSojEventWrapper element,
                                                  @Nullable Long timestamp) {

    String topics = String.join(",", SojEventDispatcher.mappings.get(element.getPageId()));
    return new ProducerRecord<>(topics, element.getGuid().getBytes(), element.getPayload());
  }
}
