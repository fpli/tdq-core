package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SessionForGuidEnhancement;
import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.broadcast.AttributeBroadcastProcessFunctionForDetectable;
import com.ebay.sojourner.ubd.rt.common.metrics.EventRulesCounterMetricsCollector;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunctionForLVS;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunctionForRNO;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunctionForSLC;
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
import com.ebay.sojourner.ubd.rt.operators.attribute.SplitFunction;
import com.ebay.sojourner.ubd.rt.operators.event.DetectableEventMapFunction;
import com.ebay.sojourner.ubd.rt.common.metrics.SojournerEndToEndMetricsCollector;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.ubd.rt.operators.session.DetectableSessionMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionForGuidEnhancementMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionToSojSessionMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
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

public class SojournerUBDRTJob {

  public static void main(String[] args) throws Exception {
    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ParameterTool.fromArgs(args);
    AppEnv.config(parameterTool);

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment =
        StreamExecutionEnvironment.getExecutionEnvironment();
    executionEnvironment.getConfig().setGlobalJobParameters(parameterTool);
    executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    // checkpoint settings
    executionEnvironment.enableCheckpointing(
        AppEnv.config().getFlink().getCheckpoint().getInterval().getSeconds() * 1000,
        CheckpointingMode.EXACTLY_ONCE);
    executionEnvironment
        .getCheckpointConfig()
        .setCheckpointTimeout(
            AppEnv.config().getFlink().getCheckpoint().getTimeout().getSeconds() * 1000);
    executionEnvironment
        .getCheckpointConfig()
        .setMinPauseBetweenCheckpoints(
            AppEnv.config().getFlink().getCheckpoint().getMinPauseBetween().getSeconds() * 1000);
    executionEnvironment
        .getCheckpointConfig()
        .setMaxConcurrentCheckpoints(
            AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent() == null
                ? 1
                : AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent());
    executionEnvironment.setStateBackend(
        StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));
    executionEnvironment.setRestartStrategy(
        RestartStrategies.fixedDelayRestart(
            3, // number of restart attempts
            org.apache.flink.api.common.time.Time.of(10, TimeUnit.SECONDS) // delay
        ));

    // for soj nrt output
    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
    // 1.2 Assign timestamps and emit watermarks.
    DataStream<RawEvent> rawEventDataStreamForRNO =
        executionEnvironment
            .addSource(KafkaSourceFunctionForRNO.generateWatermark())
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For RNO");

    DataStream<RawEvent> rawEventDataStreamForSLC =
        executionEnvironment
            .addSource(KafkaSourceFunctionForSLC.generateWatermark())
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For SLC");

    DataStream<RawEvent> rawEventDataStreamForLVS =
        executionEnvironment
            .addSource(KafkaSourceFunctionForLVS.generateWatermark())
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For LVS");

    // union three DC data
    DataStream<RawEvent> rawEventDataStream = rawEventDataStreamForRNO
        .union(rawEventDataStreamForLVS)
        .union(rawEventDataStreamForSLC);

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStream =
        rawEventDataStream
            .map(new EventMapFunction())
            .setParallelism(AppEnv.config().getFlink().getApp().getEventParallelism())
            .name("Event Operator");

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
            .allowedLateness(Time.minutes(1))
            .sideOutputLateData(lateEventOutputTag)
            .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction());

    WindowOperatorHelper.enrichWindowOperator(
        (OneInputTransformation) ubiSessionDataStream.getTransformation(),
        new UbiEventMapWithStateFunction(),
        mappedEventOutputTag);

    ubiSessionDataStream.name("Session Operator");

    DataStream<UbiEvent> ubiEventWithSessionId =
        ubiSessionDataStream.getSideOutput(mappedEventOutputTag);

    DataStream<UbiEvent> latedStream =
        ubiSessionDataStream.getSideOutput(lateEventOutputTag);

    // ubiSession to SessionForGuidEnhancement
    SingleOutputStreamOperator<SessionForGuidEnhancement> sessionForGuidEnhancement =
        ubiSessionDataStream
            .map(new UbiSessionForGuidEnhancementMapFunction())
            .name("ubiSession to SessionForGuidEnhancement")
            .setParallelism(150);

    // 4. Attribute Operator
    // 4.1 Sliding window
    // 4.2 Attribute indicator accumulation
    // 4.3 Attribute level bot detection (via bot rule)
    // 4.4 Store bot signature
    DataStream<AgentIpAttribute> agentIpAttributeDatastream =
        ubiSessionDataStream
            .keyBy("userAgent", "clientIp")
            .window(TumblingEventTimeWindows.of(Time.minutes(5)))
            .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
            .name("Attribute Operator (Agent+IP Pre-Aggregation)")
            .setParallelism(150);

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureDataStream =
        sessionForGuidEnhancement
            .keyBy("guid1", "guid2")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new GuidAttributeAgg(), new GuidWindowProcessFunction())
            .name("Attribute Operator (GUID)")
            .setParallelism(150);

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureSplitStream =
        guidSignatureDataStream.split(new SplitFunction());

    guidSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("GUID Signature Generation");

    guidSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("GUID Signature Expiration");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentIpSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent", "clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(
                new AgentIpAttributeAggSliding(), new AgentIpSignatureWindowProcessFunction())
            .name("Attribute Operator (Agent+IP)")
            .setParallelism(150);

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentIpSignatureSplitStream =
        agentIpSignatureDataStream.split(new SplitFunction());

    agentIpSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("Agent+IP Signature Generation");

    agentIpSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("Agent+IP Signature Expiration");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
            .name("Attribute Operator (Agent)")
            .setParallelism(150);

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureSplitStream =
        agentSignatureDataStream.split(new SplitFunction());

    agentSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("Agent Signature Generation");

    agentSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("Agent Signature Expiration");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
            .name("Attribute Operator (IP)")
            .setParallelism(150);

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureSplitStream =
        ipSignatureDataStream.split(new SplitFunction());

    ipSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("IP Signature Generation");

    ipSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(150)
        .name("IP Signature Expiration");

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
    DataStream<Either<UbiEvent, UbiSession>> detectableDataStream =
        ubiSessionDataStream
            .map(new DetectableSessionMapFunction())
            .union(ubiEventWithSessionId.map(new DetectableEventMapFunction()));

    // connect ubiEvent,ubiSession DataStream and broadcast Stream
    SingleOutputStreamOperator<UbiEvent> signatureBotDetectionForEvent =
        detectableDataStream
            .connect(attributeSignatureBroadcastStream)
            .process(new AttributeBroadcastProcessFunctionForDetectable(sessionOutputTag))
            .name("Signature Bot Detector");

    DataStream<UbiSession> signatureBotDetectionForSession = signatureBotDetectionForEvent
        .getSideOutput(sessionOutputTag);

    // UbiSession to SojSession
    SingleOutputStreamOperator<SojSession> sojSessionStream =
        signatureBotDetectionForSession
            .map(new UbiSessionToSojSessionMapFunction())
            .name("UbiSession to SojSession");

    // UbiEvent to SojEvent
    /*
    DataStream<SojEvent> sojEventWithSessionId = signatureBotDetectionForEvent
        .map(new UbiEventToSojEventMapFunction())
        .name("UbiEvent to SojEvent");
        */

    // 5. Load data to file system for batch processing
    // 5.1 IP Signature
    // 5.2 Sessions (ended)
    // 5.3 Events (with session ID & bot flags)
    // 5.4 Events late
    // for data quality
    // for monitor

    // kafka sink for session
    sojSessionStream.addSink(KafkaConnectorFactory
        .createKafkaProducer(Constants.TOPIC_PRODUCER, Constants.BOOTSTRAP_PRODUCER_BROKERS,
            SojSession.class, Constants.MESSAGE_KEY))
        .setParallelism(50)
        .name("SojSession Kafka")
        .uid("kafkaSink");

    // metrics collector for end to end
    signatureBotDetectionForEvent
        .addSink(new SojournerEndToEndMetricsCollector())
        .name("Pipeline End to End Duration");

    // metrics collector for event rules hit
    signatureBotDetectionForEvent
        .addSink(new EventRulesCounterMetricsCollector())
        .name("Event Metrics Collector")
        .disableChaining();

    // late event sink
    latedStream
        .addSink(new DiscardingSink<>())
        .name("Late Event");

    // Submit this job
    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getName());
  }
}
