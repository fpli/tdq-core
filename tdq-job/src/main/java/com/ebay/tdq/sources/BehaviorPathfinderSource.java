package com.ebay.tdq.sources;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_FROM_TIMESTAMP;
import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN;
import static com.ebay.sojourner.flink.common.DataCenter.LVS;
import static com.ebay.sojourner.flink.common.DataCenter.RNO;
import static com.ebay.sojourner.flink.common.DataCenter.SLC;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventDeserializationSchema;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchemaWrapper;
import com.ebay.tdq.common.env.TdqEnv;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * todo need dynamic config json file
 * @author juntzhang
 */
public class BehaviorPathfinderSource extends AbstractSource {

  private final TdqEnv tdqEnv;
  private final StreamExecutionEnvironment env;

  @Override
  public TdqEnv getTdqEnv() {
    return tdqEnv;
  }

  public BehaviorPathfinderSource(TdqEnv tdqEnv, StreamExecutionEnvironment env) {
    this.tdqEnv = tdqEnv;
    this.env = env;
  }

  // 1. Rheos Consumer
  // 1.1 Consume RawEvent from Rheos PathFinder topic
  // 1.2 Assign timestamps and emit watermarks.
  public List<DataStream<RawEvent>> build() {
    SourceDataStreamBuilder<RawEvent> dataStreamBuilder = new SourceDataStreamBuilder<>(env);

    DataStream<RawEvent> rnoDS = dataStreamBuilder
        .dc(RNO)
        .operatorName(getString(Property.SOURCE_OPERATOR_NAME_RNO))
        .uid(getString(Property.SOURCE_UID_RNO))
        .slotGroup(getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
        .outOfOrderlessInMin(getInteger(FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN))
        .fromTimestamp(getString(FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .idleSourceTimeout(getInteger(Property.FLINK_APP_IDLE_SOURCE_TIMEOUT_IN_MIN))
        .build(new PathFinderRawEventKafkaDeserializationSchemaWrapper(
            new PathFinderRawEventDeserializationSchema(tdqEnv)));
    DataStream<RawEvent> slcDS = dataStreamBuilder
        .dc(SLC)
        .operatorName(getString(Property.SOURCE_OPERATOR_NAME_SLC))
        .uid(getString(Property.SOURCE_UID_SLC))
        .slotGroup(getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP))
        .outOfOrderlessInMin(getInteger(FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN))
        .fromTimestamp(getString(Property.FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .idleSourceTimeout(getInteger(Property.FLINK_APP_IDLE_SOURCE_TIMEOUT_IN_MIN))
        .build(new PathFinderRawEventKafkaDeserializationSchemaWrapper(
            new PathFinderRawEventDeserializationSchema(tdqEnv)));
    DataStream<RawEvent> lvsDS = dataStreamBuilder
        .dc(LVS)
        .operatorName(getString(Property.SOURCE_OPERATOR_NAME_LVS))
        .uid(getString(Property.SOURCE_UID_LVS))
        .slotGroup(getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP))
        .outOfOrderlessInMin(getInteger(FLINK_APP_SOURCE_OUT_OF_ORDERLESS_IN_MIN))
        .fromTimestamp(getString(Property.FLINK_APP_SOURCE_FROM_TIMESTAMP))
        .idleSourceTimeout(getInteger(Property.FLINK_APP_IDLE_SOURCE_TIMEOUT_IN_MIN))
        .build(new PathFinderRawEventKafkaDeserializationSchemaWrapper(
            new PathFinderRawEventDeserializationSchema(tdqEnv)));
    int p = getInteger(Property.SOURCE_PARALLELISM);
    DataStream<RawEvent> r = sample(rnoDS, getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP), "RNO");
    DataStream<RawEvent> l = sample(lvsDS, getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP), "LVS");
    DataStream<RawEvent> s = sample(slcDS, getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP), "SLC");
    return Lists.newArrayList(r, l, s);
  }

  //  public DataStream<RawEvent> build(DataCenter dc, String operatorName,
  //  int parallelism, String uid, String slotGroup) {
  //    Preconditions.checkNotNull(dc);
  //
  //    KafkaConsumerConfig config = KafkaConsumerConfig.ofDC(dc);
  //
  //    FlinkKafkaConsumer<RawEvent> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
  //        config.getTopics(),
  //        new PathFinderRawEventKafkaDeserializationSchemaWrapper(
  //            new PathFinderRawEventDeserializationSchema(tdqEnv)),
  //        config.getProperties());
  //
  //    KafkaSourceEnv sourceEnv = tdqEnv.getKafkaSourceEnv();
  //
  //    switch (sourceEnv.getStartupMode()) {
  //      case "EARLIEST":
  //        flinkKafkaConsumer.setStartFromEarliest();
  //        break;
  //      case "LATEST":
  //        flinkKafkaConsumer.setStartFromLatest();
  //        break;
  //      case "TIMESTAMP":
  //        flinkKafkaConsumer.setStartFromTimestamp(sourceEnv.getFromTimestamp());
  //        break;
  //      default:
  //        throw new IllegalArgumentException("Cannot parse fromTimestamp value");
  //    }
  //
  //    return env
  //        .addSource(flinkKafkaConsumer)
  //        .setParallelism(parallelism)
  //        .slotSharingGroup(slotGroup)
  //        .name(operatorName)
  //        .uid(uid);
  //  }

}
