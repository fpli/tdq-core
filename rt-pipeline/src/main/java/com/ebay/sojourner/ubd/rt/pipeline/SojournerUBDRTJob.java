package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.rt.common.metrics.EventMetricsCollectorProcessFunction;
import com.ebay.sojourner.ubd.rt.common.metrics.PipelineMetricsCollectorProcessFunction;
import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

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
            .addSource(KafkaSourceFunction.generateWatermark(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_RNO, Constants.GROUP_ID_RNO, RawEvent.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For RNO")
            .uid("kafkaSourceForRNO");

    DataStream<RawEvent> rawEventDataStreamForSLC =
        executionEnvironment
            .addSource(KafkaSourceFunction.generateWatermark(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_SLC, Constants.GROUP_ID_SLC, RawEvent.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For SLC")
            .uid("kafkaSourceForSLC");

    DataStream<RawEvent> rawEventDataStreamForLVS =
        executionEnvironment
            .addSource(KafkaSourceFunction.generateWatermark(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_LVS, Constants.GROUP_ID_LVS, RawEvent.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For LVS")
            .uid("kafkaSourceForLVS");

    // union three DC data
    DataStream<RawEvent> rawEventDataStream = rawEventDataStreamForRNO
        .union(rawEventDataStreamForLVS)
        .union(rawEventDataStreamForSLC);

    // filter 33% throughput group by guid for reduce kafka consumer lag
    /*
    DataStream<RawEvent> filteredRawEvent = rawEventDataStream
        .filter(new RawEventFilterFunction())
        .name("RawEvent Filter Operator")
        .disableChaining()
        .uid("filterSource");
        */

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStream =
        rawEventDataStream
            .map(new EventMapFunction())
            .setParallelism(AppEnv.config().getFlink().getApp().getEventParallelism())
            .name("Event Operator")
            .uid("eventLevel");

    // refine windowsoperatorø
    // 3. Session Operator
    // 3.1 Session window
    // 3.2 Session indicator accumulation
    // 3.3 Session Level bot detection (via bot rule & signature)
    // 3.4 Event level bot detection (via session flag)
    /*
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

    ubiSessionDataStream
        .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
        .name("Session Operator")
        .uid("sessionLevel");

    DataStream<UbiEvent> ubiEventWithSessionId =
        ubiSessionDataStream.getSideOutput(mappedEventOutputTag);

    DataStream<UbiEvent> latedStream =
        ubiSessionDataStream.getSideOutput(lateEventOutputTag);

    DataStream<UbiSessionForDQ> crossSessionForDQSreaming = ubiSessionDataStream
        .map(new UbiSessionToUbiSessionForDQMapFunction())
        .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
        .name("UbiSession for cross session dq")
        .uid("CrossLevel");

    // ubiSession to SessionForGuidEnhancement
    SingleOutputStreamOperator<SessionForGuidEnhancement> sessionForGuidEnhancement =
        ubiSessionDataStream
            .map(new UbiSessionForGuidEnhancementMapFunction())
            .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
            .name("ubiSession to SessionForGuidEnhancement")
            .uid("enhanceGuid");

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
            .setParallelism(AppEnv.config().getFlink().app.getPreAgentIpParallelism())
            .uid("preAgentIpLevel");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureDataStream =
        sessionForGuidEnhancement
            .keyBy("guid1", "guid2")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new GuidAttributeAgg(), new GuidWindowProcessFunction())
            .name("Attribute Operator (GUID)")
            .setParallelism(AppEnv.config().getFlink().app.getGuidParallelism())
            .uid("guidLevel");

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> guidSignatureSplitStream =
        guidSignatureDataStream.split(new SplitFunction());

    guidSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getGuidParallelism())
        .name("GUID Signature Generation")
        .uid("generationGuidLevel");

    guidSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getGuidParallelism())
        .name("GUID Signature Expiration")
        .uid("expirationGuidLevel");

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

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentIpSignatureSplitStream =
        agentIpSignatureDataStream.split(new SplitFunction());

    agentIpSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getAgentIpParallelism())
        .name("Agent+IP Signature Generation")
        .uid("generationAgentIpLevel");

    agentIpSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getAgentIpParallelism())
        .name("Agent+IP Signature Expiration")
        .uid("expirationAgentIpLevel");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("agent")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
            .name("Attribute Operator (Agent)")
            .setParallelism(AppEnv.config().getFlink().app.getAgentParallelism())
            .uid("agentLevel");

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> agentSignatureSplitStream =
        agentSignatureDataStream.split(new SplitFunction());

    agentSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getAgentParallelism())
        .name("Agent Signature Generation")
        .uid("generationAgentLevel");

    agentSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getAgentParallelism())
        .name("Agent Signature Expiration")
        .uid("expirationAgentLevel");

    DataStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureDataStream =
        agentIpAttributeDatastream
            .keyBy("clientIp")
            .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(12), Time.hours(7)))
            .trigger(OnElementEarlyFiringTrigger.create())
            .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
            .name("Attribute Operator (IP)")
            .setParallelism(AppEnv.config().getFlink().app.getIpParallelism())
            .uid("ipLevel");

    SplitStream<Tuple4<String, Boolean, Set<Integer>, Long>> ipSignatureSplitStream =
        ipSignatureDataStream.split(new SplitFunction());

    ipSignatureSplitStream
        .select("generation")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getIpParallelism())
        .name("IP Signature Generation")
        .uid("generationIpLevel");

    ipSignatureSplitStream
        .select("expiration")
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getIpParallelism())
        .name("IP Signature Expiration")
        .uid("expirationIpLevel");

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
            .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
            .union(ubiEventWithSessionId
                .map(new DetectableEventMapFunction())
                .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism()));

    // connect ubiEvent,ubiSession DataStream and broadcast Stream
    SingleOutputStreamOperator<UbiEvent> signatureBotDetectionForEvent =
        detectableDataStream
            .connect(attributeSignatureBroadcastStream)
            .process(new AttributeBroadcastProcessFunctionForDetectable(sessionOutputTag))
            .setParallelism(AppEnv.config().getFlink().app.getBroadcastParallelism())
            .name("Signature Bot Detector")
            .uid("connectLevel");

    DataStream<UbiSession> signatureBotDetectionForSession = signatureBotDetectionForEvent
        .getSideOutput(sessionOutputTag);
        */

    /*
    // UbiSession to SojSession
    DataStream<SojSession> sojSessionStream =
        signatureBotDetectionForSession
            .map(new UbiSessionToSojSessionMapFunction())
            .setParallelism(AppEnv.config().getFlink().app.getBroadcastParallelism())
            .name("UbiSession to SojSession")
            .uid("ubiSessionToSojSession");

    // UbiEvent to SojEvent
    DataStream<SojEvent> sojEventWithSessionId =
        signatureBotDetectionForEvent
            .map(new UbiEventToSojEventMapFunction())
            .setParallelism(AppEnv.config().getFlink().app.getBroadcastParallelism())
            .name("UbiEvent to SojEvent")
            .uid("ubiEventToSojEvent");
            */

    // 5. Load data to file system for batch processing
    // 5.1 IP Signature
    // 5.2 Sessions (ended)
    // 5.3 Events (with session ID & bot flags)
    // 5.4 Events late
    // for data quality

    // kafka sink for session
    /*
    sojSessionStream.addSink(KafkaConnectorFactory
        .createKafkaProducer(Constants.TOPIC_PRODUCER_SESSION, Constants.BOOTSTRAP_SERVERS_SESSION,
            SojSession.class, Constants.MESSAGE_KEY))
        .setParallelism(AppEnv.config().getFlink().app.getSessionKafkaParallelism())
        .name("SojSession Kafka")
        .uid("kafkaSink");
        */

    // metrics collector for end to end
    ubiEventDataStream
        .process(new PipelineMetricsCollectorProcessFunction())
        .setParallelism(AppEnv.config().getFlink().app.getMetricsParallelism())
        .name("Pipeline End to End Duration")
        .uid("endToEndMetricsCollector");

    // metrics collector for event rules hit
    ubiEventDataStream
        .process(new EventMetricsCollectorProcessFunction())
        .setParallelism(AppEnv.config().getFlink().app.getMetricsParallelism())
        .name("Event Metrics Collector")
        .uid("eventLevelMetricsCollector");

    // late event sink
    /*
    latedStream
        .addSink(new DiscardingSink<>())
        .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
        .name("Late Event")
        .uid("lateEvent");


    crossSessionForDQSreaming
        .addSink(KafkaConnectorFactory
            .createKafkaProducer(Constants.TOPIC_PRODUCER_CROSS_SESSION_DQ,
                Constants.BOOTSTRAP_SERVERS_CROSS_SESSION_DQ,
                UbiSessionForDQ.class, Constants.MESSAGE_KEY))
        .setParallelism(AppEnv.config().getFlink().app.getCrossSessionParallelism())
        .name("SojSession Kafka")
        .uid("kafkaSinkForSession");
        */

    // Submit this job
    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getNameForFullPipeline());
  }
}
