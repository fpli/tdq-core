package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.LVS;
import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;
import static com.ebay.sojourner.flink.common.util.DataCenter.SLC;

import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.KafkaProducerFactory;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.operator.event.EventMapFunction;
import com.ebay.sojourner.rt.operator.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.rt.operator.session.UbiSessionAgg;
import com.ebay.sojourner.rt.operator.session.UbiSessionToIntermediateSessionMapFunction;
import com.ebay.sojourner.rt.operator.session.UbiSessionWindowProcessFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.util.OutputTag;

public class SojournerRTJobForSessionDataCopy {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic(RNO/LVS/SLC)
    // 1.2 Assign timestamps and emit watermarks.
    SourceDataStreamBuilder<RawEvent> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, RawEvent.class
    );

    DataStream<RawEvent> rawEventDataStreamForRNO = dataStreamBuilder
        .buildOfDC(RNO, FlinkEnvUtils.getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP));
    DataStream<RawEvent> rawEventDataStreamForSLC = dataStreamBuilder
        .buildOfDC(SLC, FlinkEnvUtils.getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP));
    DataStream<RawEvent> rawEventDataStreamForLVS = dataStreamBuilder
        .buildOfDC(LVS, FlinkEnvUtils.getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP));

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStreamForRNO = rawEventDataStreamForRNO
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.EVENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
        .name("Event Operator For RNO")
        .uid("event-rno-id");

    DataStream<UbiEvent> ubiEventDataStreamForLVS = rawEventDataStreamForLVS
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.EVENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP))
        .name("Event Operator For LVS")
        .uid("event-lvs-id");

    DataStream<UbiEvent> ubiEventDataStreamForSLC = rawEventDataStreamForSLC
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.EVENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP))
        .name("Event Operator For SLC")
        .uid("event-slc-id");

    // union ubiEvent from SLC/RNO/LVS
    DataStream<UbiEvent> ubiEventDataStream = ubiEventDataStreamForLVS
        .union(ubiEventDataStreamForSLC)
        .union(ubiEventDataStreamForRNO);

    // 3. Session Operator
    // 3.1 Session window
    // 3.2 Session indicator accumulation
    // 3.3 Session Level bot detection (via bot rule & signature)
    // 3.4 Event level bot detection (via session flag)
    OutputTag<UbiSession> sessionOutputTag =
        new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
    OutputTag<UbiEvent> lateEventOutputTag =
        new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));

    OutputTag<UbiEvent> mappedEventOutputTag =
        new OutputTag<>("mapped-event-output-tag", TypeInformation.of(UbiEvent.class));
    SingleOutputStreamOperator<UbiSession> ubiSessionDataStream =
        ubiEventDataStream
            .keyBy("guid")
            .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
            .allowedLateness(Time.minutes(3))
            .sideOutputLateData(lateEventOutputTag)
            .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction());

    WindowOperatorHelper.enrichWindowOperator(
        (OneInputTransformation) ubiSessionDataStream.getTransformation(),
        new UbiEventMapWithStateFunction(),
        mappedEventOutputTag);

    ubiSessionDataStream
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("Session Operator")
        .uid("session-id");

    DataStream<IntermediateSession> intermediateSessionDataStream = ubiSessionDataStream
        .map(new UbiSessionToIntermediateSessionMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("IntermediateSession For Cross Session DQ")
        .uid("session-enhance-id");

    // sink for cross session dq
    intermediateSessionDataStream
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_TOPIC),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS_RNO),
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SESSION),
            IntermediateSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("IntermediateSession")
        .uid("intermediate-session-sink-id");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}

