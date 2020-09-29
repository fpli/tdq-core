package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.common.model.RawEvent;
import java.util.HashSet;
import java.util.Set;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class RawEventKafkaDeserializationSchema implements KafkaDeserializationSchema<RawEvent> {

  private Set<String> skewGuids = new HashSet<>();
  private RawEventDeserializationSchema rawEventDeserializationSchema;

  public RawEventKafkaDeserializationSchema(Set<String> skewGuids,
      RawEventDeserializationSchema rawEventDeserializationSchema) {
    this.skewGuids = skewGuids;
    this.rawEventDeserializationSchema = rawEventDeserializationSchema;
  }

  @Override
  public boolean isEndOfStream(RawEvent nextElement) {
    return false;
  }

  @Override
  public RawEvent deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {

    if (record.key() != null && skewGuids.contains(new String(record.key()))) {
      return null;
    } else {
      return rawEventDeserializationSchema.deserialize(record.value());
    }
  }

  @Override
  public TypeInformation<RawEvent> getProducedType() {
    return TypeInformation.of(RawEvent.class);
  }
}
