package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import com.ebay.sojourner.flink.common.window.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.common.broadcast.CrossSessionDQBroadcastProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.AgentAttributeAgg;
import com.ebay.sojourner.rt.operators.attribute.AgentIpAttributeAgg;
import com.ebay.sojourner.rt.operators.attribute.AgentIpAttributeAggSliding;
import com.ebay.sojourner.rt.operators.attribute.AgentIpSignatureWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.AgentIpWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.AgentWindowProcessFunction;
import com.ebay.sojourner.rt.operators.attribute.IpAttributeAgg;
import com.ebay.sojourner.rt.operators.attribute.IpWindowProcessFunction;
import com.ebay.sojourner.rt.operators.session.IntermediateSessionToSessionCoreMapFunction;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojournerRTJobForCrossSessionDQ {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // kafka source for copy
    SourceDataStreamBuilder<IntermediateSession> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, IntermediateSession.class
    );

    DataStream<IntermediateSession> intermediateSessionDataStream = dataStreamBuilder
        .buildOfDC(RNO, FlinkEnvUtils.getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP));

    // intermediateSession to sessionCore
    DataStream<SessionCore> sessionCoreDS = intermediateSessionDataStream
        .map(new IntermediateSessionToSessionCoreMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP))
        .name("IntermediateSession To SessionCore")
        .uid("session-enhance-id");

    // cross session
    DataStream<AgentIpAttribute> agentIpAttributeDatastream = sessionCoreDS
        .keyBy("userAgent", "ip")
        .window(TumblingEventTimeWindows.of(Time.minutes(5)))
        .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.PRE_AGENT_IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Attribute Operator (Agent+IP Pre-Aggregation)")
        .uid("pre-agent-ip-id");

    DataStream<BotSignature> agentIpSignatureDataStream = agentIpAttributeDatastream
        .keyBy("agent", "clientIp")
        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
        .trigger(OnElementEarlyFiringTrigger.create())
        .aggregate(
            new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Attribute Operator (Agent+IP)")
        .uid("agent-ip-id");

    DataStream<BotSignature> agentSignatureDataStream = agentIpAttributeDatastream
        .keyBy("agent")
        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
        .trigger(OnElementEarlyFiringTrigger.create())
        .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Attribute Operator (Agent)")
        .uid("agent-id");

    DataStream<BotSignature> ipSignatureDataStream = agentIpAttributeDatastream
        .keyBy("clientIp")
        .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
        .trigger(OnElementEarlyFiringTrigger.create())
        .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("Attribute Operator (IP)")
        .uid("ip-id");

    // union attribute signature for broadcast
    DataStream<BotSignature> attributeSignatureDataStream = agentIpSignatureDataStream
        .union(agentSignatureDataStream)
        .union(ipSignatureDataStream);

    // attribute signature broadcast
    BroadcastStream<BotSignature> attributeSignatureBroadcastStream =
        attributeSignatureDataStream.broadcast(MapStateDesc.attributeSignatureDesc);

    // connect broadcast
    SingleOutputStreamOperator<IntermediateSession> intermediateSessionWithSignature =
        intermediateSessionDataStream
            .connect(attributeSignatureBroadcastStream)
            .process(new CrossSessionDQBroadcastProcessFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
            .name("Signature Bot Detector")
            .uid("signature-detection-id");

    intermediateSessionWithSignature.addSink(new DiscardingSink<>()).setParallelism(10)
        .disableChaining();

    /*
    attributeSignatureDataStream
        .addSink(HdfsConnectorFactory
            .createWithParquet(FlinkEnvUtils.getString(Property.HDFS_PATH_PARENT) +
                    FlinkEnvUtils.getString(Property.HDFS_PATH_SIGNATURES),
                BotSignature.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.AGENT_IP_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("BotSignatures")
        .uid("bot-signatures-sink-id");

    intermediateSessionWithSignature
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_PATH_PARENT) +
                FlinkEnvUtils.getString(Property.HDFS_PATH_INTERMEDIATE_SESSION),
            IntermediateSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
        .name("IntermediateSession")
        .uid("intermediate-session-sink-id");
        */
    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.NAME_DATA_QUALITY));
  }
}
