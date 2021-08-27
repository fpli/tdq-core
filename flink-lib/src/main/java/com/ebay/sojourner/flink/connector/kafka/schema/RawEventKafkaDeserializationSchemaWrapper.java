package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.common.model.RawEvent;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class RawEventKafkaDeserializationSchemaWrapper implements
    KafkaDeserializationSchema<RawEvent> {

  private RawEventDeserializationSchema rawEventDeserializationSchema;

  public RawEventKafkaDeserializationSchemaWrapper(RawEventDeserializationSchema rawEventDeserializationSchema) {
    this.rawEventDeserializationSchema = rawEventDeserializationSchema;
  }

  @Override
  public boolean isEndOfStream(RawEvent nextElement) {
    return false;
  }

  @Override
  public RawEvent deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
    return rawEventDeserializationSchema.deserialize(record.value());
  }

  @Override
  public TypeInformation<RawEvent> getProducedType() {
    return TypeInformation.of(RawEvent.class);
  }
}
