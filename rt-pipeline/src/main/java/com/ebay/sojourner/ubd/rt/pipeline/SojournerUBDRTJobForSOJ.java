package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.rt.common.broadcast.AgentBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.broadcast.AttributeBroadcastProcessFunctionForDetectable;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactoryForSOJ;
import com.ebay.sojourner.ubd.rt.operators.attribute.*;
import com.ebay.sojourner.ubd.rt.operators.event.DetectableEventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.ubd.rt.operators.session.DetectableSessionMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.types.Either;
import org.apache.flink.util.OutputTag;

import java.util.concurrent.TimeUnit;

public class SojournerUBDRTJobForSOJ {

    public static void main( String[] args ) throws Exception {
        // Make sure this is being executed at start up.
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        AppEnv.config(parameterTool);

//        System.out.println(TypeInformation.of(new TypeHint<Either<UbiEvent,UbiSession>>(){}));

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
        executionEnvironment.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                3, // number of restart attempts
                org.apache.flink.api.common.time.Time.of(10, TimeUnit.SECONDS) // delay
        ));

        // for soj nrt output
        // 1. Rheos Consumer
        // 1.1 Consume RawEvent from Rheos PathFinder topic
        // 1.2 Assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment
                .addSource(KafkaConnectorFactoryForSOJ.createKafkaConsumer()
                        .setStartFromLatest()
                        .assignTimestampsAndWatermarks(
                                new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                                    @Override
                                    public long extractTimestamp( RawEvent element ) {
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
                .allowedLateness(Time.minutes(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction())
                .name("Session Operator");

        WindowOperatorHelper.enrichWindowOperator(
                (OneInputTransformation) ubiSessinDataStream.getTransformation(),
                new UbiEventMapWithStateFunction(),
                mappedEventOutputTag
        );

        // 4. Attribute Operator
        // 4.1 Sliding window
        // 4.2 Attribute indicator accumulation
        // 4.3 Attribute level bot detection (via bot rule)
        // 4.4 Store bot signature
        DataStream<AgentIpAttribute> agentIpAttributeDatastream = ubiSessinDataStream
                .keyBy("userAgent", "clientIp")
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))
                .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
                .name("Tumbling Attribute Operator (Agent+IP)")
                .setParallelism(72);

        DataStream<AttributeSignature> guidSignatureDataStream = ubiSessinDataStream
                .keyBy("guid")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12)))
                .aggregate(new GuidAttributeAgg(), new GuidWindowProcessFunction())
                .name("Attribute Operator (GUID)")
                .setParallelism(72);

        guidSignatureDataStream.addSink(new DiscardingSink<>()).setParallelism(72).name("GUID Signature");

        DataStream<AttributeSignature> agentIpSignatureDataStream = agentIpAttributeDatastream
                .keyBy("agent", "clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12)))
                .aggregate(new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
                .name("Sliding Attribute Operator (Agent+IP)")
                .setParallelism(72);

        agentIpSignatureDataStream.addSink(new DiscardingSink<>()).setParallelism(72).name("Agent+IP Signature");

        DataStream<AttributeSignature> agentSignatureDataStream = agentIpAttributeDatastream
                .keyBy("agent")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12)))
                .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
                .name("Attribute Operator (Agent)")
                .setParallelism(72);

        agentSignatureDataStream.addSink(new DiscardingSink<>()).setParallelism(72).name("Agent Signature");

        DataStream<AttributeSignature> ipSignatureDataStream = agentIpAttributeDatastream
                .keyBy("clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12)))
                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
                .name("Attribute Operator (IP)")
                .setParallelism(72);

        ipSignatureDataStream.addSink(new DiscardingSink<>()).setParallelism(72).name("Ip Signature");

        // union attribute signature for broadcast
//        DataStream<AttributeSignature> attributeSignatureDataStream = agentIpSignatureDataStream
//                .union(agentSignatureDataStream)
//                .union(ipSignatureDataStream)
//                .union(guidSignatureDataStream);


        // attribute signature broadcast
        BroadcastStream<AttributeSignature> attributeSignatureBroadcastStream = agentSignatureDataStream
                .broadcast(MapStateDesc.attributeSignatureDesc);

        // transform ubiEvent,ubiSession to same type and union
//        DataStream<UbiEvent> mappedEventStream = ubiSessinDataStream.getSideOutput(mappedEventOutputTag);
        DataStream<UbiEvent> latedStream = ubiSessinDataStream.getSideOutput(lateEventOutputTag);
//        DataStream<Either<UbiEvent,UbiSession>> detectableDataStream =
//                ubiSessinDataStream.map(new DetectableSessionMapFunction())
//                        .union(mappedEventStream.map(new com.ebay.sojourner.ubd.rt.operators.event.DetectableEventMapFunction()));
//        SingleOutputStreamOperator<UbiEvent> signatureBotDetectionForEvent = mappedEventStream
//                .connect(attributeSignatureBroadcastStream)
//                .process(new AttributeBroadcastProcessFunctionForEvent())
//                .name("Signature BotDetection for event");
        SingleOutputStreamOperator<UbiSession> signatureBotDetectionForSession = ubiSessinDataStream
                .connect(attributeSignatureBroadcastStream)
                .process(new AgentBroadcastProcessFunction())
                .name("Signature BotDetection for session");


        // connect ubiEvent,ubiSession DataStream and broadcast Stream
//        SingleOutputStreamOperator<UbiEvent> signatureBotDetectionForEvent = detectableDataStream
//                .connect(attributeSignatureBroadcastStream)
//                .process(new AttributeBroadcastProcessFunctionForDetectable(sessionOutputTag))
//                .name("Signature BotDetection for event");

        // 5. Load data to file system for batch processing
        // 5.1 IP Signature
        // 5.2 Sessions (ended)
        // 5.3 Events (with session ID & bot flags)
        // 5.4 Events late

//        DataStream<UbiSession> signatureBotDetectionForSession = signatureBotDetectionForEvent.getSideOutput(sessionOutputTag);
//
        signatureBotDetectionForSession.addSink(new DiscardingSink<>()).name("session discarding");
        latedStream.addSink(new DiscardingSink<>()).name("late stream discarding");
//        signatureBotDetectionForEvent.addSink(new DiscardingSink<>()).name("event discarding");

        // Submit this job
        executionEnvironment.execute(AppEnv.config().getFlink().getApp().getName());

    }

}
