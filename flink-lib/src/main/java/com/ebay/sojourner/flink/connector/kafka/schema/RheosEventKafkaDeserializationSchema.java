package com.ebay.sojourner.flink.connector.kafka.schema;

import com.ebay.sojourner.flink.connector.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class RheosEventKafkaDeserializationSchema implements
    KafkaDeserializationSchema<RheosEvent> {

  @Override
  public boolean isEndOfStream(RheosEvent nextElement) {
    return false;
  }

  @Override
  public RheosEvent deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {

    return RheosEventSerdeFactory.getRheosEventHeaderDeserializer()
                                 .deserialize(null, record.value());
  }

  @Override
  public TypeInformation<RheosEvent> getProducedType() {
    return TypeInformation.of(RheosEvent.class);
  }
}
