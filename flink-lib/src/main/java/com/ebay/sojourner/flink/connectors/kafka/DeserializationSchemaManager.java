package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.common.model.JetStreamOutputSession;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojBytesEvent;
import com.ebay.sojourner.flink.connectors.kafka.schema.AvroKeyedDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.RawEventDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojBytesEventDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojEventDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojSessionDeserializationSchema;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

public class DeserializationSchemaManager {

  public static <T> DeserializationSchema<?> getSchema(Class<T> clazz) {

    if (clazz.isAssignableFrom(RawEvent.class)) {
      return new RawEventDeserializationSchema();
    } else if (clazz.isAssignableFrom(SojBytesEvent.class)) {
      return new SojBytesEventDeserializationSchema();
    } else if (clazz.isAssignableFrom(JetStreamOutputEvent.class)) {
      return new SojEventDeserializationSchema();
    } else if (clazz.isAssignableFrom(JetStreamOutputSession.class)) {
      return new SojSessionDeserializationSchema();
    }

    throw new IllegalStateException("Cannot find deserialization schema");
  }

  public static <T> KeyedDeserializationSchema<?> getKeyedSchema(Class<T> clazz) {

    return new AvroKeyedDeserializationSchema<>(clazz);
  }
}
