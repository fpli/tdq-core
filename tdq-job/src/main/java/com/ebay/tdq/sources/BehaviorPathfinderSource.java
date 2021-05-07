package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.flink.connector.kafka.schema.RawEventDeserializationSchema;
import com.ebay.sojourner.flink.connector.kafka.schema.RawEventKafkaDeserializationSchemaWrapper;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_FROM_TIMESTAMP;
import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN;
import static com.ebay.sojourner.flink.common.DataCenter.LVS;
import static com.ebay.sojourner.flink.common.DataCenter.RNO;
import static com.ebay.sojourner.flink.common.DataCenter.SLC;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

/**
 * @author juntzhang
 */
public class BehaviorPathfinderSource extends AbstractSource {
  // 1. Rheos Consumer
  // 1.1 Consume RawEvent from Rheos PathFinder topic
  // 1.2 Assign timestamps and emit watermarks.
  public static List<DataStream<RawEvent>> build(final StreamExecutionEnvironment env) {
    SourceDataStreamBuilder<RawEvent> dataStreamBuilder =
        new SourceDataStreamBuilder<>(env);

    DataStream<RawEvent> rnoDS = dataStreamBuilder
        .dc(RNO)
        .operatorName(getString(Property.SOURCE_OPERATOR_NAME_RNO))
        .uid(getString(Property.SOURCE_UID_RNO))
        .slotGroup(getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
        .outOfOrderlessInMin(getInteger(FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN))
        .fromTimestamp(getString(FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .idleSourceTimeout(getInteger(Property.FLINK_APP_IDLE_SOURCE_TIMEOUT_IN_MIN))
        .build(new RawEventKafkaDeserializationSchemaWrapper(
            FlinkEnvUtils.getSet(Property.FILTER_GUID_SET),
            new RawEventDeserializationSchema()));
    DataStream<RawEvent> slcDS = dataStreamBuilder
        .dc(SLC)
        .operatorName(getString(Property.SOURCE_OPERATOR_NAME_SLC))
        .uid(getString(Property.SOURCE_UID_SLC))
        .slotGroup(getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP))
        .outOfOrderlessInMin(getInteger(FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN))
        .fromTimestamp(getString(FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .idleSourceTimeout(getInteger(Property.FLINK_APP_IDLE_SOURCE_TIMEOUT_IN_MIN))
        .build(new RawEventKafkaDeserializationSchemaWrapper(
            FlinkEnvUtils.getSet(Property.FILTER_GUID_SET),
            new RawEventDeserializationSchema()));
    DataStream<RawEvent> lvsDS = dataStreamBuilder
        .dc(LVS)
        .operatorName(getString(Property.SOURCE_OPERATOR_NAME_LVS))
        .uid(getString(Property.SOURCE_UID_LVS))
        .slotGroup(getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP))
        .outOfOrderlessInMin(getInteger(FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN))
        .fromTimestamp(getString(FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .idleSourceTimeout(getInteger(Property.FLINK_APP_IDLE_SOURCE_TIMEOUT_IN_MIN))
        .build(new RawEventKafkaDeserializationSchemaWrapper(
            FlinkEnvUtils.getSet(Property.FILTER_GUID_SET),
            new RawEventDeserializationSchema()));
    // union ubiEvent from SLC/RNO/LVS

    int p = getInteger(Property.SOURCE_PARALLELISM);
    DataStream<RawEvent> r = sample(rnoDS, getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP), "RNO", p);
    DataStream<RawEvent> l = sample(lvsDS, getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP), "LVS", p);
    DataStream<RawEvent> s = sample(slcDS, getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP), "SLC", p);
    // return r.union(l).union(s);
    return Lists.newArrayList(r, l, s);
  }
}
