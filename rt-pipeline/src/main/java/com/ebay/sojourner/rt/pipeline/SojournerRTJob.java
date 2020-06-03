package com.ebay.sojourner.rt.pipeline;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import com.ebay.sojourner.rt.common.broadcast.AttributeBroadcastProcessFunctionForDetectable;
import com.ebay.sojourner.rt.common.metrics.EventMetricsCollectorProcessFunction;
import com.ebay.sojourner.rt.common.metrics.PipelineMetricsCollectorProcessFunction;
import com.ebay.sojourner.flink.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.flink.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.rt.operators.attribute.AgentAttributeAgg;
import com.ebay.sojourner.rt.operators.attribute.AgentIpAttributeAgg;
import com.ebay.sojourner.rt.operators.attribute.AgentIpAttributeAggSliding;
import com.ebay.sojourner.rt.operators.attribute.AgentIpSignatureWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.AgentIpWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.AgentWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.GuidAttributeAgg;
import com.ebay.sojourner.rt.operators.attribute.GuidWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.IpAttributeAgg;
import com.ebay.sojourner.rt.operators.attribute.IpWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.SplitFunction;
import com.ebay.sojourner.rt.operators.event.DetectableEventMapFunction;
import com.ebay.sojourner.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.rt.operators.event.UbiEventToSojEventMapFunction;
import com.ebay.sojourner.rt.operators.session.DetectableSessionMapFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.rt.operators.session.UbiSessionToSessionCoreMapFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionToSojSessionMapFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import java.util.Set;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.types.Either;
import org.apache.flink.util.OutputTag;

public class SojournerRTJob {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
    // 1.2 Assign timestamps and emit watermarks.
    DataStream<RawEvent> rawEventDataStreamForRNO =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Constants.BEHAVIOR_PATHFINDER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Constants.BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_RNO),
                    FlinkEnvUtils
                        .getString(Constants.BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_RNO),
                    RawEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Constants.SOURCE_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
            .name("Rheos Kafka Consumer For RNO")
            .uid("source-rno-id");

    DataStream<RawEvent> rawEventDataStreamForSLC =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Constants.BEHAVIOR_PATHFINDER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Constants.BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_SLC),
                    FlinkEnvUtils
                        .getString(Constants.BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_SLC),
                    RawEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Constants.SOURCE_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP))
            .name("Rheos Kafka Consumer For SLC")
            .uid("source-slc-id");

    DataStream<RawEvent> rawEventDataStreamForLVS =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Constants.BEHAVIOR_PATHFINDER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Constants.BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_LVS),
                    FlinkEnvUtils
                        .getString(Constants.BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_LVS),
                    RawEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Constants.SOURCE_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP))
            .name("Rheos Kafka Consumer For LVS")
            .uid("source-lvs-id");

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStreamForRNO = rawEventDataStreamForRNO
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.EVENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
        .name("Event Operator For RNO")
        .uid("event-rno-id");

    DataStream<UbiEvent> ubiEventDataStreamForLVS = rawEventDataStreamForLVS
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.EVENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP))
        .name("Event Operator For LVS")
        .uid("event-lvs-id");

    DataStream<UbiEvent> ubiEventDataStreamForSLC = rawEventDataStreamForSLC
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.EVENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP))
        .name("Event Operator For SLC")
        .uid("event-slc-id");

    // union ubiEvent from SLC/RNO/LVS
    DataStream<UbiEvent> ubiEventDataStream = ubiEventDataStreamForLVS
        .union(ubiEventDataStreamForSLC)
        .union(ubiEventDataStreamForRNO);

    // refine windowsoperatorø
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
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SESSION_SLOT_SHARE_GROUP))
        .name("Session Operator")
        .uid("session-id");

    DataStream<UbiEvent> ubiEventWithSessionId =
        ubiSessionDataStream.getSideOutput(mappedEventOutputTag);

    DataStream<UbiEvent> latedStream =
        ubiSessionDataStream.getSideOutput(lateEventOutputTag);

    // ubiSession to SessionCore
    DataStream<SessionCore> sessionCoreDataStream = ubiSessionDataStream
        .map(new UbiSessionToSessionCoreMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SESSION_SLOT_SHARE_GROUP))
        .name("UbiSession To SessionCore")
        .uid("session-enhance-id");

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
            .setParallelism(FlinkEnvUtils.getInteger(Constants.PRE_AGENT_IP_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
            .name("Attribute Operator (Agent+IP Pre-Aggregation)")
            .uid("pre-agent-ip-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureDataStream =
        sessionCoreDataStream
            .keyBy("guid")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new GuidAttributeAgg(), new GuidWindowProcessFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Constants.GUID_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
            .name("Attribute Operator (GUID)")
            .uid("guid-id");

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureSplitStream =
        guidSignatureDataStream.split(new SplitFunction());

    guidSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.GUID_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("GUID Signature Generation")
        .uid("guid-generation-id");

    guidSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.GUID_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("GUID Signature Expiration")
        .uid("guid-expiration-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentIpSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent", "clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(
                new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_IP_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
            .name("Attribute Operator (Agent+IP)")
            .uid("agent-ip-id");

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentIpSignatureSplitStream;
    agentIpSignatureSplitStream = agentIpSignatureDataStream.split(new SplitFunction());

    agentIpSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("Agent+IP Signature Generation")
        .uid("agent-ip-generation-id");

    agentIpSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("Agent+IP Signature Expiration")
        .uid("agent-ip-expiration-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
            .name("Attribute Operator (Agent)")
            .uid("agent-id");

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureSplitStream =
        agentSignatureDataStream.split(new SplitFunction());

    agentSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("Agent Signature Generation")
        .uid("agent-generation-id");

    agentSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("Agent Signature Expiration")
        .uid("agent-expiration-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Constants.IP_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
            .name("Attribute Operator (IP)")
            .uid("ip-id");

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureSplitStream =
        ipSignatureDataStream.split(new SplitFunction());

    ipSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("IP Signature Generation")
        .uid("ip-generation-id");

    ipSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("IP Signature Expiration")
        .uid("ip-expiration-id");

    // union attribute signature for broadcast
    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> attributeSignatureDataStream =
        agentIpSignatureDataStream
            .union(agentSignatureDataStream)
            .union(ipSignatureDataStream)
            .union(guidSignatureDataStream);

    // attribute signature broadcast
    BroadcastStream<Tuple4<String, Boolean, Set<Integer>, Long>> attributeSignatureBroadcastStream =
        attributeSignatureDataStream.broadcast(MapStateDesc.attributeSignatureDesc);

    // transform ubiEvent,ubiSession to same type and union
    DataStream<Either<UbiEvent, UbiSession>> ubiSessionTransDataStream = ubiSessionDataStream
        .map(new DetectableSessionMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SESSION_SLOT_SHARE_GROUP))
        .name("UbiSessionTransForBroadcast")
        .uid("session-broadcast-id");

    DataStream<Either<UbiEvent, UbiSession>> ubiEventTransDataStream = ubiEventWithSessionId
        .map(new DetectableEventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SESSION_SLOT_SHARE_GROUP))
        .name("UbiEventTransForBroadcast")
        .uid("event-broadcast-id");

    DataStream<Either<UbiEvent, UbiSession>> detectableDataStream = ubiSessionTransDataStream
        .union(ubiEventTransDataStream);

    // connect ubiEvent,ubiSession DataStream and broadcast Stream
    SingleOutputStreamOperator<UbiEvent> signatureBotDetectionForEvent = detectableDataStream
        .connect(attributeSignatureBroadcastStream)
        .process(new AttributeBroadcastProcessFunctionForDetectable(sessionOutputTag))
        .setParallelism(FlinkEnvUtils.getInteger(Constants.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("Signature Bot Detector")
        .uid("signature-detection-id");

    DataStream<UbiSession> signatureBotDetectionForSession = signatureBotDetectionForEvent
        .getSideOutput(sessionOutputTag);

    // ubiEvent to sojEvent
    DataStream<SojEvent> sojEventWithSessionId =
        signatureBotDetectionForEvent
            .map(new UbiEventToSojEventMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Constants.BROADCAST_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
            .name("UbiEvent to SojEvent")
            .uid("event-transform-id");

    // ubiSession to sojSession
    DataStream<SojSession> sojSessionStream =
        signatureBotDetectionForSession
            .map(new UbiSessionToSojSessionMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Constants.BROADCAST_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
            .name("UbiSession to SojSession")
            .uid("session-transform-id");

    // 5. Load data to file system for batch processing
    // 5.1 IP Signature
    // 5.2 Sessions (ended)
    // 5.3 Events (with session ID & bot flags)
    // 5.4 Events late

    // kafka sink for sojsession
    sojSessionStream.addSink(KafkaConnectorFactory
        .createKafkaProducer(
            FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_TOPIC_SESSION_NON_BOT),
            FlinkEnvUtils.getListString(Constants.BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT),
            SojSession.class,
            FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_MESSAGE_KEY_SESSION)))
        .setParallelism(FlinkEnvUtils.getInteger(Constants.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("SojSession")
        .uid("session-sink-id");

    // kafka sink for sojevent
    sojEventWithSessionId.addSink(KafkaConnectorFactory
        .createKafkaProducer(
            FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_TOPIC_EVENT_NON_BOT),
            FlinkEnvUtils.getListString(Constants.BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT),
            SojEvent.class,
            FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_MESSAGE_KEY_EVENT)))
        .setParallelism(FlinkEnvUtils.getInteger(Constants.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("SojEvent")
        .uid("event-sink-id");

    // metrics collector for end to end
    signatureBotDetectionForEvent
        .process(new PipelineMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.METRICS_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("Pipeline Metrics Collector")
        .uid("pipeline-metrics-id");

    // metrics collector for event rules hit
    signatureBotDetectionForEvent
        .process(new EventMetricsCollectorProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.METRICS_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.BROADCAST_SLOT_SHARE_GROUP))
        .name("Event Metrics Collector")
        .uid("event-metrics-id");

    // late event sink
    latedStream
        .addSink(new DiscardingSink<>())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Constants.SESSION_SLOT_SHARE_GROUP))
        .name("Late Event")
        .uid("event-late-id");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Constants.NAME_FULL_PIPELINE));
  }
}
