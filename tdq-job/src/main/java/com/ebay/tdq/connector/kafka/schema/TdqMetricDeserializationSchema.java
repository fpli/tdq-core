package com.ebay.tdq.connector.kafka.schema;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.config.KafkaSourceConfig;
import java.io.IOException;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class TdqMetricDeserializationSchema implements DeserializationSchema<TdqMetric> {

  private final Long endTimestamp;
  private transient DatumReader<TdqMetric> reader;

  public TdqMetricDeserializationSchema(KafkaSourceConfig ksc, TdqEnv tdqEnv) {
    this.endTimestamp = ksc.getEndOfStreamTimestamp();
  }

  @Override
  public TypeInformation<TdqMetric> getProducedType() {
    return TypeInformation.of(TdqMetric.class);
  }

  @Override
  public TdqMetric deserialize(byte[] message) throws IOException {
    Decoder decoder = DecoderFactory.get().binaryDecoder(message, null);
    return getReader().read(null, decoder);
  }

  private DatumReader<TdqMetric> getReader() {
    if (reader == null) {
      reader = new SpecificDatumReader<>(TdqMetric.class);
    }
    return reader;
  }

  @Override
  public boolean isEndOfStream(TdqMetric nextElement) {
    return isEndOfStream(nextElement.getEventTime());
  }

  public boolean isEndOfStream(long t) {
    return this.endTimestamp > 0 && t > this.endTimestamp;
  }

}


