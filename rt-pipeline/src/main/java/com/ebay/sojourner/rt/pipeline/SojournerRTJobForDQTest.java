package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.LVS;
import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;
import static com.ebay.sojourner.flink.common.util.DataCenter.SLC;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.OutputTagConstants;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.operators.event.EventDataStreamBuilder;
import com.ebay.sojourner.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.rt.operators.session.UbiSessionToSojSessionMapFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionWindowProcessFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;

public class SojournerRTJobForDQTest {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
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
    DataStream<UbiEvent> ubiEventDataStreamForLVS = EventDataStreamBuilder
        .build(rawEventDataStreamForLVS, LVS);
    DataStream<UbiEvent> ubiEventDataStreamForSLC = EventDataStreamBuilder
        .build(rawEventDataStreamForSLC, SLC);
    DataStream<UbiEvent> ubiEventDataStreamForRNO = EventDataStreamBuilder
        .build(rawEventDataStreamForRNO, RNO);

    // union ubiEvent from SLC/RNO/LVS
    DataStream<UbiEvent> ubiEventDataStream = ubiEventDataStreamForLVS
        .union(ubiEventDataStreamForSLC)
        .union(ubiEventDataStreamForRNO);

    // refine windowsoperator
    // 3. Session Operator
    // 3.1 Session window
    // 3.2 Session indicator accumulation
    // 3.3 Session Level bot detection (via bot rule & signature)
    // 3.4 Event level bot detection (via session flag)
    SingleOutputStreamOperator<UbiSession> ubiSessionDataStream =
        ubiEventDataStream
            .keyBy("guid")
            .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
            .allowedLateness(Time.minutes(3))
            .sideOutputLateData(OutputTagConstants.lateEventOutputTag)
            .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction());

    WindowOperatorHelper.enrichWindowOperator(
        (OneInputTransformation) ubiSessionDataStream.getTransformation(),
        new UbiEventMapWithStateFunction(),
        OutputTagConstants.mappedEventOutputTag);

    ubiSessionDataStream
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("Session Operator")
        .uid("session-id");

    DataStream<UbiEvent> ubiEventWithSessionId =
        ubiSessionDataStream.getSideOutput(OutputTagConstants.mappedEventOutputTag);

    DataStream<UbiEvent> latedStream =
        ubiSessionDataStream.getSideOutput(OutputTagConstants.lateEventOutputTag);

    //    // ubiSession to SessionCore
    //    DataStream<SessionCore> sessionCoreDataStream =
    //        ubiSessionDataStream
    //            .map(new UbiSessionToSessionCoreMapFunction())
    //            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
    //            .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
    //            .name("UbiSession To SessionCore")
    //            .uid("session-enhance-id");

    //    // ubiSession to intermediate session
    //    DataStream<IntermediateSession> intermediateSessionDataStream = ubiSessionDataStream
    //        .map(new UbiSessionToIntermediateSessionMapFunction())
    //        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
    //        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
    //        .name("UbiSession To IntermediateSession")
    //        .uid("intermediate-session-enhance-id");

    //    // intermediate session sink
    //    intermediateSessionDataStream
    //        .addSink(KafkaProducerFactory.getProducer(
    //            FlinkEnvUtils.getString(Property.KAFKA_TOPIC_INTERMEDIATE_SESSION),
    //            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS_RNO),
    //            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SESSION),
    //            IntermediateSession.class))
    //        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
    //        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
    //        .name("IntermediateSession")
    //        .uid("intermediate-session-sink-id");

    // 4. Attribute Operator
    // 4.1 Sliding window
    // 4.2 Attribute indicator accumulation
    // 4.3 Attribute level bot detection (via bot rule)
    // 4.4 Store bot signature
    //    DataStream<AgentIpAttribute> agentIpAttributeDatastream =
    //        sessionCoreDataStream
    //            .keyBy("userAgent", "ip")
    //            .window(TumblingEventTimeWindows.of(Time.minutes(5)))
    //            .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
    //            .setParallelism(FlinkEnvUtils.getInteger(Property.PRE_AGENT_IP_PARALLELISM))
    //            .slotSharingGroup(FlinkEnvUtils.getString(Property
    //            .CROSS_SESSION_SLOT_SHARE_GROUP))
    //            .name("Attribute Operator (Agent+IP Pre-Aggregation)")
    //            .uid("pre-agent-ip-id");
    //
    //    DataStream<BotSignature> agentIpSignatureDataStream = agentIpAttributeDatastream
    //        .keyBy("agent", "clientIp")
    //        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
    //        .trigger(OnElementEarlyFiringTrigger.create())
    //        .aggregate(
    //            new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
    //        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_IP_PARALLELISM))
    //        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
    //        .name("Attribute Operator (Agent+IP)")
    //        .uid("agent-ip-id");
    //
    //    DataStream<BotSignature> agentSignatureDataStream = agentIpAttributeDatastream
    //        .keyBy("agent")
    //        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
    //        .trigger(OnElementEarlyFiringTrigger.create())
    //        .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
    //        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_PARALLELISM))
    //        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
    //        .name("Attribute Operator (Agent)")
    //        .uid("agent-id");
    //
    //    DataStream<BotSignature> ipSignatureDataStream = agentIpAttributeDatastream
    //        .keyBy("clientIp")
    //        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
    //        .trigger(OnElementEarlyFiringTrigger.create())
    //        .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
    //        .setParallelism(FlinkEnvUtils.getInteger(Property.IP_PARALLELISM))
    //        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
    //        .name("Attribute Operator (IP)")
    //        .uid("ip-id");
    //
    //    // union attribute signature for broadcast
    //    DataStream<BotSignature> attributeSignatureDataStream = agentIpSignatureDataStream
    //        .union(agentSignatureDataStream)
    //        .union(ipSignatureDataStream);
    //
    //    // attribute signature broadcast
    //    BroadcastStream<BotSignature> attributeSignatureBroadcastStream =
    //    attributeSignatureDataStream
    //        .broadcast(MapStateDesc.attributeSignatureDesc);
    //
    //    // transform ubiEvent,ubiSession to same type and union
    //    DataStream<Either<UbiEvent, UbiSession>> ubiSessionTransDataStream =
    //        ubiSessionDataStream
    //            .map(new DetectableSessionMapFunction())
    //            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
    //            .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
    //            .name("UbiSessionTransForBroadcast")
    //            .uid("session-broadcast-id");
    //
    //    DataStream<Either<UbiEvent, UbiSession>> ubiEventTransDataStream =
    //        ubiEventWithSessionId
    //            .map(new DetectableEventMapFunction())
    //            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
    //            .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
    //            .name("UbiEventTransForBroadcast")
    //            .uid("event-broadcast-id");
    //
    //    DataStream<Either<UbiEvent, UbiSession>> detectableDataStream =
    //        ubiSessionTransDataStream.union(ubiEventTransDataStream);
    //
    //    // connect ubiEvent,ubiSession DataStream and broadcast Stream
    //    SingleOutputStreamOperator<UbiEvent> signatureBotDetectionForEvent = detectableDataStream
    //        .connect(attributeSignatureBroadcastStream)
    //        .process(
    //            new AttributeBroadcastProcessFunctionForDetectable(OutputTagConstants
    //            .sessionOutputTag))
    //        .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
    //        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
    //        .name("Signature Bot Detector")
    //        .uid("signature-detection-id");
    //
    //    DataStream<UbiSession> signatureBotDetectionForSession =
    //        signatureBotDetectionForEvent.getSideOutput(OutputTagConstants.sessionOutputTag);
    //
    //    // ubiEvent to sojEvent
    //    DataStream<SojEvent> sojEventWithSessionId =
    //        ubiEventWithSessionId
    //            .map(new UbiEventToSojEventMapFunction())
    //            .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
    //            .slotSharingGroup(FlinkEnvUtils.getString(Property
    //                .CROSS_SESSION_SLOT_SHARE_GROUP))
    //            .name("UbiEvent to SojEvent")
    //            .uid("event-transform-id");

    // ubiSession to sojSession
    DataStream<SojSession> sojSessionStream =
        ubiSessionDataStream
            .map(new UbiSessionToSojSessionMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property
                .SESSION_SLOT_SHARE_GROUP))
            .name("UbiSession to SojSession")
            .uid("session-transform-id");

    // 5. Load data to file system for batch processing
    // 5.1 IP Signature
    // 5.2 Sessions (ended)
    // 5.3 Events (with session ID & bot flags)
    // 5.4 Events late
    // This path is for local test. For production, we should use
    // "hdfs://apollo-rno//user/o_ubi/events/"
    sojSessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_PATH_PARENT) +
                FlinkEnvUtils.getString(Property.HDFS_PATH_SESSION_NON_BOT),
            SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property
            .SESSION_SLOT_SHARE_GROUP))
        .name("SojSession sink")
        .uid("session-sink-id");

    ubiEventWithSessionId
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property
            .SESSION_SLOT_SHARE_GROUP))
        .name("SojEvent sink")
        .uid("event-sink-id");

    // late event sink
    latedStream
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("Late Event")
        .uid("event-late-id");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
