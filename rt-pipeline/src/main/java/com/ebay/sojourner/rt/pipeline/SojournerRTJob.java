package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.DataCenter.LVS;
import static com.ebay.sojourner.flink.common.DataCenter.RNO;
import static com.ebay.sojourner.flink.common.DataCenter.SLC;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.schema.RawEventDeserializationSchema;
import com.ebay.sojourner.flink.connector.kafka.schema.RawEventKafkaDeserializationSchema;
import com.ebay.sojourner.flink.state.MapStateDesc;
import com.ebay.sojourner.flink.common.OutputTagConstants;
import com.ebay.sojourner.flink.window.CompositeTrigger;
import com.ebay.sojourner.flink.window.MidnightOpenSessionTrigger;
import com.ebay.sojourner.flink.window.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.flink.window.SojEventTimeSessionWindows;
import com.ebay.sojourner.flink.connector.kafka.KafkaProducerFactory;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.broadcast.AttributeBroadcastProcessFunctionForDetectable;
import com.ebay.sojourner.rt.metric.AgentIpMetricsCollectorProcessFunction;
import com.ebay.sojourner.rt.metric.AgentMetricsCollectorProcessFunction;
import com.ebay.sojourner.rt.metric.EventMetricsCollectorProcessFunction;
import com.ebay.sojourner.rt.metric.IpMetricsCollectorProcessFunction;
import com.ebay.sojourner.rt.metric.PipelineMetricsCollectorProcessFunction;
import com.ebay.sojourner.rt.operator.attribute.AgentAttributeAgg;
import com.ebay.sojourner.rt.operator.attribute.AgentIpAttributeAgg;
import com.ebay.sojourner.rt.operator.attribute.AgentIpAttributeAggSliding;
import com.ebay.sojourner.rt.operator.attribute.AgentIpSignatureWindowProcessFunction;
import com.ebay.sojourner.rt.operator.attribute.AgentIpWindowProcessFunction;
import com.ebay.sojourner.rt.operator.attribute.AgentWindowProcessFunction;
import com.ebay.sojourner.rt.operator.attribute.IpAttributeAgg;
import com.ebay.sojourner.rt.operator.attribute.IpWindowProcessFunction;
import com.ebay.sojourner.rt.operator.event.DetectableEventMapFunction;
import com.ebay.sojourner.rt.operator.event.EventDataStreamBuilder;
import com.ebay.sojourner.rt.operator.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.rt.operator.event.UbiEventToSojEventMapFunction;
import com.ebay.sojourner.rt.operator.event.UbiEventToSojEventProcessFunction;
import com.ebay.sojourner.rt.operator.session.DetectableSessionMapFunction;
import com.ebay.sojourner.rt.operator.session.UbiSessionAgg;
import com.ebay.sojourner.rt.operator.session.UbiSessionToSessionCoreMapFunction;
import com.ebay.sojourner.rt.operator.session.UbiSessionToSojSessionProcessFunction;
import com.ebay.sojourner.rt.operator.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.rt.util.SignatureUtils;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.types.Either;

public class SojournerRTJob {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
    // 1.2 Assign timestamps and emit watermarks.
    SourceDataStreamBuilder<RawEvent> dataStreamBuilder =
        new SourceDataStreamBuilder<>(executionEnvironment);

    DataStream<RawEvent> rawEventDataStreamForRNO = dataStreamBuilder
        .dc(RNO)
        .operatorName(FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME_RNO))
        .uid(FlinkEnvUtils.getString(Property.SOURCE_UID_RNO))
        .slotGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
        .build(new RawEventKafkaDeserializationSchema(
            FlinkEnvUtils.getSet(Property.FILTER_GUID_SET),
            new RawEventDeserializationSchema()));
    DataStream<RawEvent> rawEventDataStreamForSLC = dataStreamBuilder
        .dc(SLC)
        .operatorName(FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME_SLC))
        .uid(FlinkEnvUtils.getString(Property.SOURCE_UID_SLC))
        .slotGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP))
        .build(new RawEventKafkaDeserializationSchema(
            FlinkEnvUtils.getSet(Property.FILTER_GUID_SET),
            new RawEventDeserializationSchema()));
    DataStream<RawEvent> rawEventDataStreamForLVS = dataStreamBuilder
        .dc(LVS)
        .operatorName(FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME_LVS))
        .uid(FlinkEnvUtils.getString(Property.SOURCE_UID_LVS))
        .slotGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP))
        .build(new RawEventKafkaDeserializationSchema(
            FlinkEnvUtils.getSet(Property.FILTER_GUID_SET),
            new RawEventDeserializationSchema()));

    // filter skew guid
    /*
    DataStream<RawEvent> filteredRawEventDataStreamForLVS = rawEventDataStreamForLVS
        .process(new DataSkewProcessFunction(OutputTagConstants.dataSkewOutputTag,
            FlinkEnvUtils.getBoolean(Property.IS_FILTER),
            FlinkEnvUtils.getSet(Property.FILTER_PAGE_ID_SET)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP))
        .name("Filter LVS Skew Guid")
        .uid("filter-lvs-skew-guid");

    DataStream<RawEvent> filteredRawEventDataStreamForSLC = rawEventDataStreamForSLC
        .process(new DataSkewProcessFunction(OutputTagConstants.dataSkewOutputTag,
            FlinkEnvUtils.getBoolean(Property.IS_FILTER),
            FlinkEnvUtils.getSet(Property.FILTER_PAGE_ID_SET)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP))
        .name("Filter SLC Skew Guid")
        .uid("filter-slc-skew-guid");

    DataStream<RawEvent> filteredRawEventDataStreamForRNO = rawEventDataStreamForRNO
        .process(new DataSkewProcessFunction(OutputTagConstants.dataSkewOutputTag,
            FlinkEnvUtils.getBoolean(Property.IS_FILTER),
            FlinkEnvUtils.getSet(Property.FILTER_PAGE_ID_SET)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
        .name("Filter RNO Skew Guid")
        .uid("filter-rno-skew-guid");
        */

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
            .window(SojEventTimeSessionWindows.withGapAndMaxDuration(Time.minutes(30),
                Time.hours(24)))
            .trigger(CompositeTrigger.Builder.create().trigger(EventTimeTrigger.create())
                .trigger(MidnightOpenSessionTrigger
                    .of(Time.hours(7))).build())
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
        .uid("session-operator");

    DataStream<UbiEvent> ubiEventWithSessionId =
        ubiSessionDataStream.getSideOutput(OutputTagConstants.mappedEventOutputTag);

    DataStream<UbiEvent> latedStream =
        ubiSessionDataStream.getSideOutput(OutputTagConstants.lateEventOutputTag);

    // ubiSession to SessionCore
    DataStream<SessionCore> sessionCoreDataStream =
        ubiSessionDataStream
            .map(new UbiSessionToSessionCoreMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
            .name("UbiSession To SessionCore")
            .uid("ubisession-to-sessioncore");

    // 4. Attribute Operator
    // 4.1 Sliding window
    // 4.2 Attribute indicator accumulation
    // 4.3 Attribute level bot detection (via bot rule)
    // 4.4 Store bot signature
    DataStream<AgentIpAttribute> agentIpAttributeDatastream =
        sessionCoreDataStream
            .keyBy("userAgent", "ip")
            .window(TumblingEventTimeWindows.of(Time.minutes(5)))
            .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.PRE_AGENT_IP_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
            .name("Attribute Operator (Agent+IP Pre-Aggregation)")
            .uid("attribute-operator-pre-aggregation");

    DataStream<BotSignature> agentIpSignatureDataStream = agentIpAttributeDatastream
        .keyBy("agent", "clientIp")
        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
        .trigger(OnElementEarlyFiringTrigger.create())
        .aggregate(
            new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Attribute Operator (Agent+IP)")
        .uid("attribute-operator-agent-ip");

    DataStream<BotSignature> agentSignatureDataStream = agentIpAttributeDatastream
        .keyBy("agent")
        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
        .trigger(OnElementEarlyFiringTrigger.create())
        .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Attribute Operator (Agent)")
        .uid("attribute-operator-agent");

    DataStream<BotSignature> ipSignatureDataStream = agentIpAttributeDatastream
        .keyBy("clientIp")
        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
        .trigger(OnElementEarlyFiringTrigger.create())
        .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Attribute Operator (IP)")
        .uid("attribute-operator-ip");

    // union attribute signature for broadcast
    DataStream<BotSignature> attributeSignatureDataStream = agentIpSignatureDataStream
        .union(agentSignatureDataStream)
        .union(ipSignatureDataStream);

    // attribute signature broadcast
    BroadcastStream<BotSignature> attributeSignatureBroadcastStream = attributeSignatureDataStream
        .broadcast(MapStateDesc.attributeSignatureDesc);

    // transform ubiEvent,ubiSession to same type and union
    DataStream<Either<UbiEvent, UbiSession>> ubiSessionTransDataStream =
        ubiSessionDataStream
            .map(new DetectableSessionMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
            .name("Transform UbiSession for Union")
            .uid("transform-ubisession-for-union");

    DataStream<Either<UbiEvent, UbiSession>> ubiEventTransDataStream =
        ubiEventWithSessionId
            .map(new DetectableEventMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
            .name("Transform UbiEvent for Union")
            .uid("transform-ubievent-for-union");

    DataStream<Either<UbiEvent, UbiSession>> detectableDataStream =
        ubiSessionTransDataStream.union(ubiEventTransDataStream);

    // connect ubiEvent,ubiSession DataStream and broadcast Stream
    SingleOutputStreamOperator<UbiEvent> signatureBotDetectionForEvent =
        detectableDataStream.connect(attributeSignatureBroadcastStream)
            .process(
                new AttributeBroadcastProcessFunctionForDetectable(
                    OutputTagConstants.sessionOutputTag))
            .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
            .name("Signature Bot Detector")
            .uid("signature-bot-detector");

    DataStream<UbiSession> signatureBotDetectionForSession =
        signatureBotDetectionForEvent.getSideOutput(OutputTagConstants.sessionOutputTag);

    // ubiEvent to sojEvent
    SingleOutputStreamOperator<SojEvent> sojEventWithSessionId =
        signatureBotDetectionForEvent
            .process(new UbiEventToSojEventProcessFunction(OutputTagConstants.botEventOutputTag))
            .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
            .name("UbiEvent to SojEvent")
            .uid("ubievent-to-sojevent");

    DataStream<SojEvent> botSojEventStream = sojEventWithSessionId
        .getSideOutput(OutputTagConstants.botEventOutputTag);

    // ubiSession to sojSession
    SingleOutputStreamOperator<SojSession> sojSessionStream =
        signatureBotDetectionForSession
            .process(
                new UbiSessionToSojSessionProcessFunction(OutputTagConstants.botSessionOutputTag))
            .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
            .name("UbiSession to SojSession")
            .uid("ubisession-to-sojsession");

    DataStream<SojSession> botSojSessionStream = sojSessionStream
        .getSideOutput(OutputTagConstants.botSessionOutputTag);

    // 5. Load data to file system for batch processing
    // 5.1 IP Signature
    // 5.2 Sessions (ended)
    // 5.3 Events (with session ID & bot flags)
    // 5.4 Events late

    // kafka sink for bot and nonbot sojsession
    sojSessionStream
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_TOPIC_SESSION_NON_BOT),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS),
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_SUBJECT_SOJSESSION),
            SojSession.class,
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SESSION)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Nonbot SojSession")
        .uid("nonbot-sojsession-sink");

    botSojSessionStream
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_TOPIC_SESSION_BOT),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS),
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_SUBJECT_SOJSESSION),
            SojSession.class,
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SESSION)
        ))
        .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Bot SojSession")
        .uid("bot-sojsession-sink");

    // kafka sink for bot and nonbot sojevent
    sojEventWithSessionId
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_TOPIC_EVENT_NON_BOT),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS),
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_SUBJECT_SOJEVENT),
            SojEvent.class,
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_EVENT_KEY1),
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_EVENT_KEY2)
        ))
        .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Nonbot SojEvent")
        .uid("nonbot-sojevent-sink");

    botSojEventStream
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_TOPIC_EVENT_BOT),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS),
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_SUBJECT_SOJEVENT),
            SojEvent.class,
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_EVENT_KEY1),
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_EVENT_KEY2)
        ))
        .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Bot SojEvent")
        .uid("bot-sojevent-sink");

    // metrics collector for end to end
    signatureBotDetectionForEvent
        .process(new PipelineMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.METRICS_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Pipeline Metrics Collector")
        .uid("pipeline-metrics-collector");

    // metrics collector for event rules hit
    signatureBotDetectionForEvent
        .process(new EventMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.METRICS_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Event Metrics Collector")
        .uid("event-metrics-collector");

    // metrics collector for signature generation or expiration
    agentIpSignatureDataStream
        .process(new AgentIpMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("AgentIp Metrics Collector")
        .uid("agent-ip-metrics-collector");

    agentSignatureDataStream
        .process(new AgentMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Agent Metrics Collector")
        .uid("agent-metrics-id");

    ipSignatureDataStream
        .process(new IpMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Ip Metrics Collector")
        .uid("ip-metrics-id");

    // signature sink
    SignatureUtils.buildSignatureKafkaSink(agentIpSignatureDataStream,
        FlinkEnvUtils.getString(Property.KAFKA_TOPIC_SIGNATURE_AGENT_IP),
        Constants.AGENTIP,
        FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP),
        FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SIGNATURE_AGENT_IP));

    SignatureUtils.buildSignatureKafkaSink(agentSignatureDataStream,
        FlinkEnvUtils.getString(Property.KAFKA_TOPIC_SIGNATURE_AGENT),
        Constants.AGENT,
        FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP),
        FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SIGNATURE_AGENT));

    SignatureUtils.buildSignatureKafkaSink(ipSignatureDataStream,
        FlinkEnvUtils.getString(Property.KAFKA_TOPIC_SIGNATURE_IP),
        Constants.IP,
        FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP),
        FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SIGNATURE_IP));

    // kafka sink for late event
    DataStream<SojEvent> lateSojEventStream = latedStream
        .map(new UbiEventToSojEventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("Late UbiEvent to SojEvent")
        .uid("late-ubievent-to-sojevent");

    lateSojEventStream
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_TOPIC_EVENT_LATE),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS),
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_SUBJECT_SOJEVENT),
            SojEvent.class,
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_EVENT_KEY1),
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_EVENT_KEY2)))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("Late SojEvent")
        .uid("late-sojevent-sink");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}
