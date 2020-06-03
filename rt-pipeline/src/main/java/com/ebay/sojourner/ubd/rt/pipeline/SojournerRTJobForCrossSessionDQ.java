package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.CrossSessionSignature;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.rt.common.broadcast.CrossSessionDQBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.HdfsSinkUtil;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentAttributeAgg;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentIpAttributeAgg;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentIpAttributeAggSliding;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentIpSignatureWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentIpWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.operators.attribute.GuidAttributeAgg;
import com.ebay.sojourner.ubd.rt.operators.attribute.GuidWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.operators.attribute.IpAttributeAgg;
import com.ebay.sojourner.ubd.rt.operators.attribute.IpWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.operators.attribute.TupleToCrossSessionSignatureMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.IntermediateSessionToSessionCoreMapFunction;
import com.ebay.sojourner.ubd.rt.util.FlinkEnvUtils;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
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
    DataStream<IntermediateSession> intermediateSessionDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(
                    FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_TOPIC_DQ_CROSS_SESSION),
                    FlinkEnvUtils
                        .getListString(Constants.BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT),
                    FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_GROUP_ID_DQ_CROSS_SESSION),
                    IntermediateSession.class))
            .setParallelism(FlinkEnvUtils.getInteger(Constants.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For Cross Session DQ")
            .uid("source-id");

    // intermediateSession to sessionCore
    DataStream<SessionCore> sessionCoreDS = intermediateSessionDataStream
        .map(new IntermediateSessionToSessionCoreMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .name("IntermediateSession To SessionCore")
        .uid("session-enhance-id");

    // cross session
    DataStream<AgentIpAttribute> agentIpAttributeDatastream =
        sessionCoreDS
            .keyBy("userAgent", "ip")
            .window(TumblingEventTimeWindows.of(Time.minutes(5)))
            .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
            .name("Attribute Operator (Agent+IP Pre-Aggregation)")
            .setParallelism(FlinkEnvUtils.getInteger(Constants.PRE_AGENT_IP_PARALLELISM))
            .uid("pre-agent-ip-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureDataStream =
        sessionCoreDS
            .keyBy("guid")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new GuidAttributeAgg(), new GuidWindowProcessFunction())
            .name("Attribute Operator (GUID)")
            .setParallelism(FlinkEnvUtils.getInteger(Constants.GUID_PARALLELISM))
            .uid("guid-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentIpSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent", "clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(
                new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
            .name("Attribute Operator (Agent+IP)")
            .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_IP_PARALLELISM))
            .uid("agent-ip-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
            .name("Attribute Operator (Agent)")
            .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_PARALLELISM))
            .uid("agent-id");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
            .name("Attribute Operator (IP)")
            .setParallelism(FlinkEnvUtils.getInteger(Constants.IP_PARALLELISM))
            .uid("ip-id");

    // union attribute signature for broadcast
    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> attributeSignatureDataStream =
        agentIpSignatureDataStream
            .union(agentSignatureDataStream)
            .union(ipSignatureDataStream)
            .union(guidSignatureDataStream);

    // signatures hdfs sink
    DataStream<CrossSessionSignature> tupleToCrossSessionDataStream = attributeSignatureDataStream
        .map(new TupleToCrossSessionSignatureMapFunction())
        .name("Tuple4ToCrossSessionSignature")
        .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_IP_PARALLELISM))
        .uid("tuple4-transform-id");

    tupleToCrossSessionDataStream
        .addSink(HdfsSinkUtil.signatureSinkWithParquet())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.AGENT_IP_PARALLELISM))
        .name("SignaturesSink")
        .uid("signature-sink-id");

    // attribute signature broadcast
    BroadcastStream<Tuple4<String, Boolean, Set<Integer>, Long>> attributeSignatureBroadcastStream =
        attributeSignatureDataStream.broadcast(MapStateDesc.attributeSignatureDesc);

    // connect broadcast
    SingleOutputStreamOperator<IntermediateSession> intermediateSessionWithSignature =
        intermediateSessionDataStream
            .connect(attributeSignatureBroadcastStream)
            .process(new CrossSessionDQBroadcastProcessFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Constants.BROADCAST_PARALLELISM))
            .name("Signature Bot Detector")
            .uid("signature-detection-id");

    intermediateSessionWithSignature
        .addSink(HdfsSinkUtil.intermediateSessionSinkWithParquet())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.BROADCAST_PARALLELISM))
        .name("IntermediateSession sink")
        .uid("intermediate-session-sink-id");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Constants.NAME_DATA_QUALITY));
  }
}
