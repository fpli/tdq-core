package com.ebay.tdq.connector.kafka.schema;

import com.ebay.sojourner.common.model.RawEvent;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class PathFinderRawEventKafkaDeserializationSchemaWrapper implements
    KafkaDeserializationSchema<RawEvent> {

  private PathFinderRawEventDeserializationSchema rawEventDeserializationSchema;

  public PathFinderRawEventKafkaDeserializationSchemaWrapper(
      PathFinderRawEventDeserializationSchema rawEventDeserializationSchema) {
    this.rawEventDeserializationSchema = rawEventDeserializationSchema;
  }

  @Override
  public boolean isEndOfStream(RawEvent nextElement) {
    return rawEventDeserializationSchema.isEndOfStream(nextElement);
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
