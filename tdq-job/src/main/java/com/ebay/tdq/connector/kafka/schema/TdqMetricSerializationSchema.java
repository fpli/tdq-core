package com.ebay.tdq.connector.kafka.schema;


import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.config.KafkaSinkConfig;
import io.ebay.rheos.schema.avro.RheosEventSerializer;
import io.ebay.rheos.schema.event.RheosEvent;
import java.util.Random;
import lombok.SneakyThrows;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.Nullable;

public class TdqMetricSerializationSchema implements KafkaSerializationSchema<TdqMetric> {

  private static final RheosEventSerializer serializer = new RheosEventSerializer();
  private final KafkaSinkConfig kafkaSinkConfig;
  private final Random random = new Random();

  public TdqMetricSerializationSchema(KafkaSinkConfig ksc, TdqEnv tdqEnv) {
    this.kafkaSinkConfig = ksc;
  }

  @SneakyThrows
  @Override
  public ProducerRecord<byte[], byte[]> serialize(TdqMetric element, @Nullable Long timestamp) {
    RheosEvent rheosEvent = new RheosEvent(element);
    rheosEvent.setProducerId(element.getRheosHeader().getProducerId());
    rheosEvent.setEventSentTimestamp(element.getRheosHeader().getEventSentTimestamp());
    rheosEvent.setEventCreateTimestamp(element.getRheosHeader().getEventCreateTimestamp());
    rheosEvent.setSchemaId(element.getRheosHeader().getSchemaId());
    return new ProducerRecord<>(
        kafkaSinkConfig.getTopic(),
        random.nextInt(kafkaSinkConfig.getParallelism()),
        element.getMetricId().getBytes(),
        serializer.serialize(kafkaSinkConfig.getTopic(), rheosEvent)
    );
  }
}
