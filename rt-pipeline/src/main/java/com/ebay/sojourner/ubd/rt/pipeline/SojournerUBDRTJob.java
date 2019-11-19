package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.rt.operators.attribute.*;
import com.ebay.sojourner.ubd.rt.operators.event.AgentIpMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.SojJobParameters;
import com.ebay.sojourner.ubd.rt.util.StateBackendFactory;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;


public class SojournerUBDRTJob {

    public static void main(String[] args) throws Exception {
        // 0.0 Prepare execution environment
        // 0.1 UBI configuration
        // 0.2 Flink configuration
        InputStream resourceAsStream = SojournerUBDRTJob.class.getResourceAsStream("/ubi.properties");
        UBIConfig ubiConfig = UBIConfig.getInstance(resourceAsStream);

        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
        // LookupUtils.uploadFiles(executionEnvironment, params, ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(180 * 1000);
        executionEnvironment.getCheckpointConfig().setCheckpointTimeout(10 * 60 * 1000);
        executionEnvironment.setStateBackend(
                StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));
        executionEnvironment.setParallelism(2);

        // 1. Rheos Consumer
        // 1.1 Consume RawEvent from Rheos PathFinder topic
        // 1.2 Assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactory.createKafkaConsumer().assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                            @Override
                            public long extractTimestamp(RawEvent element) {
                                return element.getRheosHeader().getEventCreateTimestamp();
                            }
                        }
                ))
                .name("Rheos Consumer");

        // 2. Event Operator
        // 2.1 Parse and transform RawEvent to UbiEvent
        // 2.2 Event level bot detection via bot rule
        DataStream<UbiEvent> ubiEventDataStream = rawEventDataStream
                .map(new EventMapFunction())
                .name("Event Operator")
                .startNewChain();

        // 3. Session Operator
        // 3.1 Session window
        // 3.2 Session indicator accumulation
        // 3.3 Session Level bot detection (via bot rule & signature)
        // 3.4 Event level bot detection (via session flag)
        OutputTag<UbiSession> sessionOutputTag =
                new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
        OutputTag<UbiEvent> lateEventOutputTag =
                new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
//        JobID jobId = executionEnvironment.getStreamGraph().getJobGraph().getJobID();
        SingleOutputStreamOperator<UbiEvent> ubiEventStreamWithSessionId = ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .allowedLateness(Time.hours(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(),
                        new UbiSessionWindowProcessFunction(sessionOutputTag))
                .name("Session Operator");
        DataStream<UbiSession> sessionStream =
                ubiEventStreamWithSessionId.getSideOutput(sessionOutputTag); // sessions ended

        // 4. Attribute Operator
        // 4.1 Sliding window
        // 4.2 Attribute indicator accumulation
        // 4.3 Attribute level bot detection (via bot rule)
        // 4.4 Store bot signature
        DataStream<AgentIpAttribute> agentIpAttributeDataStream = sessionStream
                .keyBy("userAgent", "clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
                .name("Attribute Operator (Agent+IP)");

        // agent ip DataStream & agent ip bot dectector
        SingleOutputStreamOperator<AgentIpSignature> agentIpSignatureDataStream = agentIpAttributeDataStream
                .keyBy("agent", "clientIp")
                .map(new AgentIpMapFunction())
                .name("Signature Generate(Agent+IP)");

        agentIpSignatureDataStream.print().name("Agent+IP Signature");

        DataStream<AgentSignature> agentAttributeDataStream = agentIpAttributeDataStream
                .keyBy("agent")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
                .name("Attribute Operator (Agent)");

        agentAttributeDataStream.print().name("Agent Signature");

        DataStream<IpSignature> ipAttributeDataStream = agentIpAttributeDataStream
                .keyBy("clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
                .name("Attribute Operator (IP)");

        // agent ip broadcast
        BroadcastStream<AgentIpSignature> agentIpBroadcastStream = agentIpSignatureDataStream.broadcast(MapStateDesc.agentIpSignatureDesc);

        // agent broadcast
        BroadcastStream<AgentSignature> agentBroadcastStream = agentAttributeDataStream.broadcast(MapStateDesc.agentSignatureDesc);

        // ip broadcast
        BroadcastStream<IpSignature> ipBroadcastStrem = ipAttributeDataStream.broadcast(MapStateDesc.ipSignatureDesc);

        SingleOutputStreamOperator<UbiEvent> ipConnectDataStream = ubiEventStreamWithSessionId.connect(ipBroadcastStrem).process(new BroadcastProcessFunction<UbiEvent, IpSignature, UbiEvent>() {
            @Override
            public void processElement(UbiEvent value, ReadOnlyContext ctx, Collector<UbiEvent> out) throws Exception {
                ReadOnlyBroadcastState<String, Set<Integer>> ipBroadcastState = ctx.getBroadcastState(MapStateDesc.ipSignatureDesc);
                if(ipBroadcastState.contains(value.getClientIP())){
                    value.getBotFlags().addAll(ipBroadcastState.get(value.getClientIP()));
                }
                out.collect(value);
            }

            @Override
            public void processBroadcastElement(IpSignature value, Context ctx, Collector<UbiEvent> out) throws Exception {
                BroadcastState<String, Set<Integer>> ipBroadcastState = ctx.getBroadcastState(MapStateDesc.ipSignatureDesc);
                for (Map.Entry<String, Set<Integer>> entry : value.getIpBotSignature().entrySet()) {
                    ipBroadcastState.put(entry.getKey(), entry.getValue());
                }
            }
        }).name("Signature BotDetection(IP)");

        SingleOutputStreamOperator<UbiEvent> agentConnectDataStream = ipConnectDataStream.connect(agentBroadcastStream).process(new BroadcastProcessFunction<UbiEvent, AgentSignature, UbiEvent>() {
            @Override
            public void processElement(UbiEvent value, ReadOnlyContext ctx, Collector<UbiEvent> out) throws Exception {
                ReadOnlyBroadcastState<String, Set<Integer>> agentBroadcastState = ctx.getBroadcastState(MapStateDesc.agentSignatureDesc);
                if (agentBroadcastState.contains(value.getAgentInfo())) {
                    value.getBotFlags().addAll(agentBroadcastState.get(value.getAgentInfo()));
                }
                out.collect(value);
            }

            @Override
            public void processBroadcastElement(AgentSignature value, Context ctx, Collector<UbiEvent> out) throws Exception {
                BroadcastState<String, Set<Integer>> agentBroadcastState = ctx.getBroadcastState(MapStateDesc.agentSignatureDesc);
                for (Map.Entry<String, Set<Integer>> entry : value.getAgentBotSignature().entrySet()) {
                    agentBroadcastState.put(entry.getKey(), entry.getValue());
                }
            }
        }).name("Signature BotDetection(Agent)");

        SingleOutputStreamOperator<UbiEvent> agentIpConnectDataStream = agentConnectDataStream.connect(agentIpBroadcastStream).process(new BroadcastProcessFunction<UbiEvent, AgentIpSignature, UbiEvent>() {
            @Override
            public void processElement(UbiEvent value, ReadOnlyContext ctx, Collector<UbiEvent> out) throws Exception {
                ReadOnlyBroadcastState<String, Set<Integer>> agentIpBroadcastState = ctx.getBroadcastState(MapStateDesc.agentIpSignatureDesc);
                if(agentIpBroadcastState.contains(value.getAgentInfo()+value.getClientIP())){
                    value.getBotFlags().addAll(agentIpBroadcastState.get(value.getAgentInfo()+value.getClientIP()));
                }
                out.collect(value);
            }

            @Override
            public void processBroadcastElement(AgentIpSignature value, Context ctx, Collector<UbiEvent> out) throws Exception {
                BroadcastState<String, Set<Integer>> agentIpBroadcastState = ctx.getBroadcastState(MapStateDesc.agentIpSignatureDesc);
                for (Map.Entry<String, Set<Integer>> entry : value.getAgentIpBotSignature().entrySet()) {
                    agentIpBroadcastState.put(entry.getKey(), entry.getValue());
                }
            }
        }).name("Signature BotDetection(Agent+IP)");


        // 5. Load data to file system for batch processing
        // 5.1 IP Signature
        // 5.2 Sessions (ended)
        // 5.3 Events (with session ID & bot flags)
        // 5.4 Events late
//        ipAttributeDataStream.addSink(StreamingFileSinkFactory.ipSignatureSinkWithAP())
//                .name("IP Signature").disableChaining();
//        sessionStream.addSink(StreamingFileSinkFactory.sessionSink())
//                .name("Sessions").disableChaining();
//        ubiEventStreamWithSessionId.addSink(StreamingFileSinkFactory.eventSink())
//                .name("Events").disableChaining();

        DataStream<UbiEvent> lateEventStream =
                ubiEventStreamWithSessionId.getSideOutput(lateEventOutputTag);

//        lateEventStream.addSink(StreamingFileSinkFactory.lateEventSink())
//                .name("Events (Late)").disableChaining();

        ipAttributeDataStream.print().name("IP Signature");
//        agentIpAttributeDataStream.print().name("AgentIp Signature").disableChaining();

        sessionStream.print().name("Sessions").disableChaining();
        agentIpConnectDataStream.print().name("Events").disableChaining();
//        ubiEventStreamWithSessionId.print().name("Events").disableChaining();
        lateEventStream.print().name("Events (Late)").disableChaining();

        // Submit this job
        executionEnvironment.execute("Unified Bot Detection RT Pipeline");

    }

}
