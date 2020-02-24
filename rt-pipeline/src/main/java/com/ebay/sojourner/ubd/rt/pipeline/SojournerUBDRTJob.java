package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentIpAttributeAgg;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentIpMapFunction;
import com.ebay.sojourner.ubd.rt.operators.attribute.AgentIpWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.util.OutputTag;


public class SojournerUBDRTJob {

    public static void main(String[] args) throws Exception {
        // Make sure this is being executed at start up.
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        AppEnv.config(parameterTool);

        // hack StringValue to use the version 1.10
//        Method m = TypeExtractor.class.getDeclaredMethod("registerFactory", Type.class, Class.class);
//        m.setAccessible(true);
//        m.invoke(null, String.class, SOjStringFactory.class);

        // 0.0 Prepare execution environment
        // 0.1 UBI configuration
        // 0.2 Flink configuration
        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
//        final ParameterTool params = ParameterTool.fromArgs(args);
//        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
//         LookupUtils.uploadFiles(executionEnvironment, params, ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
//        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);

        // checkpoint settings
        executionEnvironment.enableCheckpointing(AppEnv.config().getFlink().getCheckpoint().getInterval().getSeconds() * 1000, CheckpointingMode.EXACTLY_ONCE);
        executionEnvironment.getCheckpointConfig().setCheckpointTimeout(AppEnv.config().getFlink().getCheckpoint().getTimeout().getSeconds() * 1000);
        executionEnvironment.getCheckpointConfig().setMinPauseBetweenCheckpoints(AppEnv.config().getFlink().getCheckpoint().getMinPauseBetween().getSeconds() * 1000);
        executionEnvironment.getCheckpointConfig().setMaxConcurrentCheckpoints(AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent() == null ?
                1 : AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent());
        executionEnvironment.setStateBackend(StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));

        // for soj nrt output
        // 1. Rheos Consumer
        // 1.1 Consume RawEvent from Rheos PathFinder topic
        // 1.2 Assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment
                .addSource(KafkaConnectorFactory.createKafkaConsumer()
                        .setStartFromLatest()
                        .assignTimestampsAndWatermarks(
                                new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                                    @Override
                                    public long extractTimestamp(RawEvent element) {
                                        return element.getRheosHeader().getEventCreateTimestamp();
                                    }
                                }))
                .setParallelism(AppEnv.config().getFlink().getApp().getSourceParallelism() == null ?
                        30 : AppEnv.config().getFlink().getApp().getSourceParallelism())
                .name("Rheos Kafka Consumer");

        // 2. Event Operator
        // 2.1 Parse and transform RawEvent to UbiEvent
        // 2.2 Event level bot detection via bot rule
//        DataStream<RawEvent> filterRawEventDataStream = rawEventDataStream
//                .filter(new EventFilterFunction())
//                .setParallelism(30)
//                .name("filter RawEvents");

        DataStream<UbiEvent> ubiEventDataStream = rawEventDataStream
                .map(new EventMapFunction())
                .setParallelism(AppEnv.config().getFlink().getApp().getEventParallelism())
                .name("Event Operator");

        //refine windowsoperator

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
        SingleOutputStreamOperator<UbiSession> ubiSessinDataStream = ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
//                .trigger(OnElementEarlyFiringTrigger.create())   //no need to customize the triiger, use the default eventtimeTrigger
                .allowedLateness(Time.hours(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction())
                .name("Session Operator");

        WindowOperatorHelper.enrichWindowOperator(
                (OneInputTransformation) ubiSessinDataStream.getTransformation(),
                new UbiEventMapWithStateFunction(),
                mappedEventOutputTag
        );

        DataStream<UbiEvent> mappedEventStream = ubiSessinDataStream.getSideOutput(mappedEventOutputTag);

        // 4. Attribute Operator
        // 4.1 Sliding window
        // 4.2 Attribute indicator accumulation
        // 4.3 Attribute level bot detection (via bot rule)
        // 4.4 Store bot signature
        DataStream<AgentIpAttribute> agentIpAttributeDataStream = ubiSessinDataStream
                .keyBy("userAgent", "clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())

                .name("Attribute Operator (Agent+IP)")
                .setParallelism(25);

        // agent ip DataStream & agent ip bot detector
        SingleOutputStreamOperator<AgentIpSignature> agentIpSignatureDataStream = agentIpAttributeDataStream
                .keyBy("agent", "clientIp")
                .map(new AgentIpMapFunction())
                .name("Signature Generate(Agent+IP)")
                .setParallelism(25);

        agentIpSignatureDataStream.addSink(new DiscardingSink<>()).setParallelism(25).name("Agent+IP Signature");
//
//        DataStream<AgentSignature> agentAttributeDataStream = agentIpAttributeDataStream
//                .keyBy("agent")
//                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
//                .trigger(OnElementEarlyFiringTrigger.create())
//                .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
//                .name("Attribute Operator (Agent)")
//                .setParallelism(25);
//
//        agentAttributeDataStream.addSink(new DiscardingSink<>()).setParallelism(25).name("Agent Signature");
//
//        DataStream<IpSignature> ipAttributeDataStream = agentIpAttributeDataStream
//                .keyBy("clientIp")
//                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
//                .trigger(OnElementEarlyFiringTrigger.create())
//                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
//                .name("Attribute Operator (IP)")
//                .setParallelism(25);
//
//        ipAttributeDataStream.addSink(new DiscardingSink<>()).setParallelism(25).name("Ip Signature");

        // agent ip broadcast
//        BroadcastStream<AgentIpSignature> agentIpBroadcastStream = agentIpSignatureDataStream.broadcast(MapStateDesc.agentIpSignatureDesc);

        // agent broadcast
//        BroadcastStream<AgentSignature> agentBroadcastStream = agentAttributeDataStream.broadcast(MapStateDesc.agentSignatureDesc);
//
//        // ip broadcast
//        BroadcastStream<IpSignature> ipBroadcastStrem = ipAttributeDataStream.broadcast(MapStateDesc.ipSignatureDesc);

//        SingleOutputStreamOperator<UbiEvent> ipConnectDataStream = mappedEventStream
//                .connect(ipBroadcastStrem)
//                .process(new IpBroadcastProcessFunction())
//                .name("Signature BotDetection(IP)");
//
//        SingleOutputStreamOperator<UbiEvent> agentConnectDataStream = ipConnectDataStream
//                .connect(agentBroadcastStream)
//                .process(new AgentBroadcastProcessFunction())
//                .name("Signature BotDetection(Agent)");
//
//        SingleOutputStreamOperator<UbiEvent> agentIpConnectDataStream = agentConnectDataStream
//                .connect(agentIpBroadcastStream)
//                .process(new AgentIpBroadcastProcessFunction())
//                .name("Signature BotDetection(Agent+IP)");

        // 5. Load data to file system for batch processing
        // 5.1 IP Signature
        // 5.2 Sessions (ended)
        // 5.3 Events (with session ID & bot flags)
        // 5.4 Events late

        ubiSessinDataStream.addSink(new DiscardingSink<>()).name("session discarding");
//        agentIpConnectDataStream.addSink(new DiscardingSink<>()).name("ubiEvent with SessionId and bot");

        // Submit this job
        executionEnvironment.execute(AppEnv.config().getFlink().getApp().getName());

    }
}
