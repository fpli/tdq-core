package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
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
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojournerUBDRTJobForCrossSession {

  public static void main(String[] args) throws Exception {
    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
    AppEnv.config(parameterTool);

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment =
        ExecutionEnvUtil.prepare(parameterTool);

    // kafka source for copy
    DataStream<IntermediateSession> intermediateSessionDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(Constants.TOPIC_PRODUCER_CROSS_SESSION_DQ,
                    Constants.BOOTSTRAP_SERVERS_CROSS_SESSION_DQ,
                    Constants.GROUP_ID_CROSS_SESSION_DQ,
                    IntermediateSession.class))
            .setParallelism(AppEnv.config().getFlink().app.getCrossSessionParallelism())
            .name("Rheos Kafka Consumer For Cross Session DQ")
            .uid("kafkaSourceForCrossSessionDQ");

    /*
    // filter agent ip
    DataStream<IntermediateSession> agentIpFilterStream =
        intermediateSessionDataStream
            .filter(new AgentIpFilterFunction())
            .setParallelism(AppEnv.config().getFlink().app.getCrossSessionParallelism())
            .name("Agent Ip filter")
            .uid("agentIpFilter");
      */

    // cross session
    DataStream<AgentIpAttribute> agentIpAttributeDatastream =
        intermediateSessionDataStream
            .keyBy("userAgent", "clientIp")
            .window(TumblingEventTimeWindows.of(Time.minutes(5)))
            .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
            .name("Attribute Operator (Agent+IP Pre-Aggregation)")
            .setParallelism(AppEnv.config().getFlink().app.getPreAgentIpParallelism())
            .uid("pre-agentIpLevel");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureDataStream =
        intermediateSessionDataStream
            .keyBy("guid1", "guid2")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new GuidAttributeAgg(), new GuidWindowProcessFunction())
            .name("Attribute Operator (GUID)")
            .setParallelism(AppEnv.config().getFlink().app.getGuidParallelism())
            .uid("guidLevel");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentIpSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent", "clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(
                new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
            .name("Attribute Operator (Agent+IP)")
            .setParallelism(AppEnv.config().getFlink().app.getAgentIpParallelism())
            .uid("agentIpLevel");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
            .name("Attribute Operator (Agent)")
            .setParallelism(AppEnv.config().getFlink().app.getAgentParallelism())
            .uid("agentLevel");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
            .name("Attribute Operator (IP)")
            .setParallelism(AppEnv.config().getFlink().app.getIpParallelism())
            .uid("ipLevel");

    // union attribute signature for broadcast
    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> attributeSignatureDataStream =
        agentIpSignatureDataStream
            .union(agentSignatureDataStream)
            .union(ipSignatureDataStream)
            .union(guidSignatureDataStream);

    // attribute signature broadcast
    BroadcastStream<Tuple4<String, Boolean, Set<Integer>, Long>> attributeSignatureBroadcastStream =
        attributeSignatureDataStream.broadcast(MapStateDesc.attributeSignatureDesc);

    // connect broadcast
    SingleOutputStreamOperator<IntermediateSession> intermediateSessionWithSignature =
        intermediateSessionDataStream
            .connect(attributeSignatureBroadcastStream)
            .process(new CrossSessionDQBroadcastProcessFunction())
            .setParallelism(AppEnv.config().getFlink().app.getBroadcastParallelism())
            .name("Signature Bot Detector")
            .uid("connectLevel");

    intermediateSessionWithSignature
        .addSink(HdfsSinkUtil.signatureSinkWithParquet())
        .setParallelism(AppEnv.config().getFlink().app.getBroadcastParallelism())
        .name("IntermediateSession sink")
        .uid("intermediateSessionHdfsSink");

    // Submit this job
    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getNameForDQPipeline());

  }
}
