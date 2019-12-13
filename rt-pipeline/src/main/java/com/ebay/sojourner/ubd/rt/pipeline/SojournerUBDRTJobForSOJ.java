package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.rt.common.broadcast.AgentBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.broadcast.AgentIpBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.broadcast.IpBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactoryForSOJ;
import com.ebay.sojourner.ubd.rt.operators.attribute.*;
import com.ebay.sojourner.ubd.rt.operators.event.AgentIpMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventFilterFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.SojJobParameters;
import com.ebay.sojourner.ubd.rt.util.StateBackendFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

@Slf4j
public class SojournerUBDRTJobForSOJ {

    public static void main(String[] args) throws Exception {
        // 0.0 Prepare execution environment
        // 0.1 UBI configuration
        // 0.2 Flink configuration
        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(180 * 1000);
        executionEnvironment.getCheckpointConfig().setCheckpointTimeout(10 * 60 * 1000);
        executionEnvironment.setStateBackend(
                StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));


        // for soj nrt output
        // 1. Rheos Consumer
        // 1.1 Consume RawEvent from Rheos PathFinder topic
        // 1.2 Assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactoryForSOJ.createKafkaConsumer().setStartFromLatest().assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                            @Override
                            public long extractTimestamp(RawEvent element) {
                                return element.getRheosHeader().getEventCreateTimestamp();
                            }
                        }
                )).setParallelism(30)
                .name("Rheos Consumer");

        // 2. Event Operator
        // 2.1 Parse and transform RawEvent to UbiEvent
        // 2.2 Event level bot detection via bot rule
        DataStream<RawEvent> rawEventFilterDataStream = rawEventDataStream
                .filter(new EventFilterFunction())
                .name("Filter RawEvents")
                .setParallelism(30);

        // 2. Event Operator
        // 2.1 Parse and transform RawEvent to UbiEvent
        // 2.2 Event level bot detection via bot rule
        DataStream<UbiEvent> ubiEventDataStream = rawEventFilterDataStream
                .map(new EventMapFunction())
                .name("Event Operator");

        // 3. Session Operator
        // 3.1 Session window
        // 3.2 Session indicator accumulation
        // 3.3 Session Level bot detection (via bot rule & signature)
        // 3.4 Event level bot detection (via session flag)
        OutputTag<UbiSession> sessionOutputTag =
                new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
        OutputTag<UbiEvent> lateEventOutputTag =
                new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
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
                .name("Attribute Operator (Agent+IP)")
                .setParallelism(25);

        // agent ip DataStream & agent ip bot dectector
        SingleOutputStreamOperator<AgentIpSignature> agentIpSignatureDataStream = agentIpAttributeDataStream
                .keyBy("agent", "clientIp")
                .map(new AgentIpMapFunction())
                .name("Signature Generate(Agent+IP)")
                .setParallelism(25);

        agentIpSignatureDataStream.addSink(new DiscardingSink<>()).name("Agent+IP Signature").setParallelism(25);

        DataStream<AgentSignature> agentAttributeDataStream = agentIpAttributeDataStream
                .keyBy("agent")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
                .name("Attribute Operator (Agent)")
                .setParallelism(25);

        agentAttributeDataStream.addSink(new DiscardingSink<>()).name("Agent Signature").setParallelism(25);

        DataStream<IpSignature> ipAttributeDataStream = agentIpAttributeDataStream
                .keyBy("clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
                .name("Attribute Operator (IP)")
                .setParallelism(25);

        ipAttributeDataStream.addSink(new DiscardingSink<>()).name("Ip Signature").setParallelism(25);

        // agent ip broadcast
        BroadcastStream<AgentIpSignature> agentIpBroadcastStream = agentIpSignatureDataStream.broadcast(MapStateDesc.agentIpSignatureDesc);

        // agent broadcast
        BroadcastStream<AgentSignature> agentBroadcastStream = agentAttributeDataStream.broadcast(MapStateDesc.agentSignatureDesc);

        // ip broadcast
        BroadcastStream<IpSignature> ipBroadcastStrem = ipAttributeDataStream.broadcast(MapStateDesc.ipSignatureDesc);

        SingleOutputStreamOperator<UbiEvent> ipConnectDataStream = ubiEventStreamWithSessionId
                .connect(ipBroadcastStrem)
                .process(new IpBroadcastProcessFunction())
                .name("Signature BotDetection(IP)");

        SingleOutputStreamOperator<UbiEvent> agentConnectDataStream = ipConnectDataStream
                .connect(agentBroadcastStream)
                .process(new AgentBroadcastProcessFunction())
                .name("Signature BotDetection(Agent)");

        SingleOutputStreamOperator<UbiEvent> agentIpConnectDataStream = agentConnectDataStream
                .connect(agentIpBroadcastStream)
                .process(new AgentIpBroadcastProcessFunction())
                .name("Signature BotDetection(Agent+IP)");

        // 5. Load data to file system for batch processing
        // 5.1 IP Signature
        // 5.2 Sessions (ended)
        // 5.3 Events (with session ID & bot flags)
        // 5.4 Events late
        agentIpConnectDataStream.addSink(new DiscardingSink<>()).name("broadcast connect(ubiEventStreamWithSessionId)");
        sessionStream.addSink(new DiscardingSink<>()).name("session discarding");
        // Submit this job
        executionEnvironment.execute("Unified Bot Detection RT Pipeline");

    }

}
