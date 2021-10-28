package com.ebay.tdq.connector.kafka.schema;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.expressions.Expression;
import com.ebay.tdq.rules.ExpressionParser;
import com.ebay.tdq.rules.PhysicalPlan;
import io.ebay.rheos.schema.event.RheosEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.MetricGroup;

@Slf4j
public class TdqEventDeserializationSchema extends AbstractTdqEventDeserializationSchema implements
    DeserializationSchema<TdqEvent> {

  private final String schemaRegistryUrl;
  private final Long endTimestamp;
  private final Expression eventTimeExpr;
  private transient Map<String, Counter> counterMap;
  private transient MetricGroup group;
  private transient long errorMsgCurrentTimeMillis = 0L;
  private final String name;

  public TdqEventDeserializationSchema(KafkaSourceConfig ksc, TdqEnv tdqEnv) {
    this.name = ksc.getName();
    this.schemaRegistryUrl = ksc.getRheosServicesUrls();
    this.endTimestamp = ksc.getEndOfStreamTimestamp();
    Validate.isTrue(StringUtils.isNotBlank(ksc.getSchemaSubject()), "schema-subject is empty!");
    Schema schema = RheosEventSerdeFactory.getSchema(ksc.getSchemaSubject(), schemaRegistryUrl);
    ExpressionParser parser = ExpressionParser.apply(ksc.getEventTimeField(), tdqEnv, schema);
    this.eventTimeExpr = parser.parse();
    Validate.isTrue(this.eventTimeExpr != null);
  }

  @Override
  public void open(InitializationContext context) {
    this.group = context.getMetricGroup();
    counterMap = new HashMap<>();
    this.group = group.addGroup("tdq").addGroup("src", name);
    errorMsgCurrentTimeMillis = 0L;
    log.info("open success");
  }

  @Override
  public TypeInformation<TdqEvent> getProducedType() {
    return TypeInformation.of(TdqEvent.class);
  }

  @Override
  public TdqEvent deserialize(byte[] message) {
    return deserialize0(message);
  }

  @Override
  public TdqEvent deserialize1(byte[] message) {
    RheosEvent event = RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord record = RheosEventSerdeFactory.getRheosEventDeserializer(this.schemaRegistryUrl).decode(event);
    TdqEvent tdqEvent = new TdqEvent(record);
    tdqEvent.buildEventTime(getEventTimeMs(tdqEvent));
    return tdqEvent;
  }

  private Long getEventTimeMs(TdqEvent event) {
    return (long) PhysicalPlan.eval(eventTimeExpr, event);
  }

  @Override
  public boolean isEndOfStream(TdqEvent nextElement) {
    return isEndOfStream(nextElement.getEventTimeMs());
  }

  public boolean isEndOfStream(long t) {
    return this.endTimestamp > 0 && t > this.endTimestamp;
  }

  @Override
  Map<String, Counter> getCounterMap() {
    return counterMap;
  }

  @Override
  MetricGroup getGroup() {
    return group;
  }

  @Override
  long getErrorMsgCurrentTimeMillis() {
    return errorMsgCurrentTimeMillis;
  }

  @Override
  void setErrorMsgCurrentTimeMillis(Long t) {
    this.errorMsgCurrentTimeMillis = t;
  }

}


