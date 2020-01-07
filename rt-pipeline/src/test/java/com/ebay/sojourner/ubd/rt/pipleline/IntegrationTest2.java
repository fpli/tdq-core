//package com.ebay.sojourner.ubd.rt.pipleline;
//
//import com.ebay.sojourner.ubd.common.model.*;
//import com.ebay.sojourner.ubd.rt.common.broadcast.AgentBroadcastProcessFunction;
//import com.ebay.sojourner.ubd.rt.common.broadcast.AgentIpBroadcastProcessFunction;
//import com.ebay.sojourner.ubd.rt.common.broadcast.IpBroadcastProcessFunction;
//import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
//import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
//import com.ebay.sojourner.ubd.rt.operators.attribute.*;
//import com.ebay.sojourner.ubd.rt.operators.event.AgentIpMapFunction;
//import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
//import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
//import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
//import com.ebay.sojourner.ubd.rt.util.RawEventGenerator;
//import com.ebay.sojourner.ubd.rt.util.ResultGenerator;
//import org.apache.flink.api.common.JobExecutionResult;
//import org.apache.flink.api.common.state.MapStateDescriptor;
//import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
//import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
//import org.apache.flink.streaming.api.datastream.BroadcastStream;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
//import org.apache.flink.streaming.api.functions.sink.SinkFunction;
//import org.apache.flink.streaming.api.functions.source.SourceFunction;
//import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
//import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
//import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
//import org.apache.flink.streaming.api.windowing.time.Time;
//import org.apache.flink.test.util.MiniClusterWithClientResource;
//import org.apache.flink.util.Collector;
//import org.apache.flink.util.OutputTag;
//import org.apache.log4j.Logger;
//import org.junit.ClassRule;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.Assert.assertTrue;
//
//public class IntegrationTest2 {
//    private static final Logger logger = Logger.getLogger(IntegrationTest2.class);
//    public class DynamicConfigSource implements SourceFunction<Tuple2<String,String>> {
//
//        private volatile boolean isRunning = true;
//
//        @Override
//        public void run( SourceFunction.SourceContext<Tuple2<String, String>> ctx) throws Exception {
//            long idx = 1;
//            while (isRunning){
//                ctx.collect(Tuple2.of("demoConfigKey","value" + idx));
//                idx++;
//                TimeUnit.SECONDS.sleep(10);
//            }
//        }
//
//        @Override
//        public void cancel() {
//            isRunning = false;
//        }
//    }
//
//    @ClassRule
//    public static MiniClusterWithClientResource flinkCluster =
//            new MiniClusterWithClientResource(
//                    new MiniClusterResourceConfiguration.Builder()
//                            .setNumberSlotsPerTaskManager(2)
//                            .setNumberTaskManagers(1)
//                            .build());
//
//
//    @Test
//    public void testIncrementPipeline() throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        // configure your test environment
//        env.setParallelism(2);
//
//        // values are collected in a static variable
//        CollectSink.values.clear();
//
//        // create a stream of custom elements and apply transformations
//        DataStream<RawEvent> rawEventDataStream = env.fromCollection(RawEventGenerator.getRawEventList("/SourceData")).assignTimestampsAndWatermarks
//                (
//                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
//                            @Override
//                            public long extractTimestamp( RawEvent element ) {
//                                return element.getRheosHeader().getEventCreateTimestamp()*1000;
//                            }
//                        }
//                ).name("Source Operator");
//        MapStateDescriptor<String, String> descriptor = new MapStateDescriptor("dynamicConfig222222", BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO);
//
//        DataStream dataStream =env.addSource(new DynamicConfigSource());
//
//        BroadcastStream<Tuple2<String,String>> configStream = dataStream.broadcast(descriptor);
//
//        DataStream<UbiEvent> ubiEventDataStream = rawEventDataStream
//                .map(new EventMapFunction())
//                .setParallelism(2)
//                .name("Event Operator");
////        ubiEventDataStream.addSink(new CollectSink2());
//
//        OutputTag<UbiSession> sessionOutputTag =
//                new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
//        OutputTag<UbiEvent> lateEventOutputTag =
//                new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
////        JobID jobId = executionEnvironment.getStreamGraph().getJobGraph().getJobID();∂
//        SingleOutputStreamOperator<UbiEvent> ubiEventStreamWithSessionId = ubiEventDataStream
//                .keyBy("guid")
//                .window(EventTimeSessionWindows.withGap(Time.seconds(30)))
//                .trigger(OnElementEarlyFiringTrigger.create())
////                .allowedLateness(Time.hours(1))
//                .sideOutputLateData(lateEventOutputTag)
//                .aggregate(new UbiSessionAgg(),
//                        new UbiSessionWindowProcessFunction(sessionOutputTag))
//                .name("Session Operator");
//        DataStream<UbiSession> sessionStream =
//                ubiEventStreamWithSessionId.getSideOutput(sessionOutputTag); // sessions ended
//
//        // 4. Attribute Operator
//        // 4.1 Sliding window
//        // 4.2 Attribute indicator accumulation
//        // 4.3 Attribute level bot detection (via bot rule)
//        // 4.4 Store bot signature
////        DataStream<AgentIpAttribute> agentIpAttributeDataStream = sessionStream
////                .keyBy("userAgent", "clientIp")
////                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
////                .trigger(OnElementEarlyFiringTrigger.create())
////                .aggregate(new AgentIpAttributeAgg(), new AgentIpWindowProcessFunction())
////                .name("Attribute Operator (Agent+IP)");
//
//        // agent ip DataStream & agent ip bot dectector
////        SingleOutputStreamOperator<AgentIpSignature> agentIpSignatureDataStream = agentIpAttributeDataStream
////                .keyBy("agent", "clientIp")
////                .map(new AgentIpMapFunction())
////                .name("Signature Generate(Agent+IP)");
////
//////        agentIpSignatureDataStream.print().name("Agent+IP Signature");
////
////        DataStream<AgentSignature> agentAttributeDataStream = agentIpAttributeDataStream
////                .keyBy("agent")
////                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
////                .trigger(OnElementEarlyFiringTrigger.create())
////                .aggregate(new AgentAttributeAgg(), new AgentWindowProcessFunction())
////                .name("Attribute Operator (Agent)");
//
////        agentAttributeDataStream.print().name("Agent Signature");
//
////        DataStream<IpSignature> ipAttributeDataStream = agentIpAttributeDataStream
////                .keyBy("clientIp")
////                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
////                .trigger(OnElementEarlyFiringTrigger.create())
////                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
////                .name("Attribute Operator (IP)");
////        ipAttributeDataStream.print().name("ipSigunature");
////        // agent ip broadcast
////        BroadcastStream<AgentIpSignature> agentIpBroadcastStream = agentIpSignatureDataStream.broadcast(MapStateDesc.agentIpSignatureDesc);
////
////        // agent broadcast
////        BroadcastStream<AgentSignature> agentBroadcastStream = agentAttributeDataStream.broadcast(MapStateDesc.agentSignatureDesc);
//
//        // ip broadcast
////        BroadcastStream<IpSignature> ipBroadcastStrem = ipAttributeDataStream.broadcast(MapStateDesc.ipSignatureDesc);
//
////        SingleOutputStreamOperator<UbiEvent> ipConnectDataStream = ubiEventStreamWithSessionId
////                .connect(configStream)
////                .process(new BroadcastProcessFunction<UbiEvent, Tuple2<String,String>, UbiEvent>() {
////                    @Override
////                    public void processElement( UbiEvent value, ReadOnlyContext ctx, Collector<UbiEvent> out) throws Exception {
////                        ReadOnlyBroadcastState<String,String> config = ctx.getBroadcastState(descriptor);
////                        String configValue = config.get("demoConfigKey");
////                        //do some process base on the config
////                        System.out.println("values:"+configValue);
////                    }
////
////                    @Override
////                    public void processBroadcastElement(Tuple2<String, String> value, Context ctx, Collector<UbiEvent> out) throws Exception {
////                        System.out.println(value);
////                        System.out.println("====");
////                        //update state
////                        ctx.getBroadcastState(descriptor).put(value.getField(0),value.getField(1));
////                    }
////                })
////                .name("Signature BotDetection(IP)");
//        SingleOutputStreamOperator<Tuple2<String,String>> ipConnectDataStream2 = dataStream
//                .connect(configStream)
//                .process(new BroadcastProcessFunction<Tuple2<String,String>, Tuple2<String,String>, Tuple2<String,String>>() {
//                    @Override
//                    public void processElement( Tuple2<String,String> value, ReadOnlyContext ctx, Collector<Tuple2<String,String>> out) throws Exception {
//                        ReadOnlyBroadcastState<String,String> config = ctx.getBroadcastState(descriptor);
//                        String configValue = config.get("demoConfigKey");
//                        //do some process base on the config
//                        System.out.println("values:"+configValue);
//                    }
//
//                    @Override
//                    public void processBroadcastElement(Tuple2<String, String> value, Context ctx, Collector<Tuple2<String,String>> out) throws Exception {
//                        System.out.println(value);
//                        System.out.println("====");
//                        //update state
//                        ctx.getBroadcastState(descriptor).put(value.getField(0),value.getField(1));
//                    }
//                })
//                .name("Signature BotDetection(IP)");
//
////
////        SingleOutputStreamOperator<UbiEvent> agentConnectDataStream = ipConnectDataStream
////                .connect(agentBroadcastStream)
////                .process(new AgentBroadcastProcessFunction())
////                .name("Signature BotDetection(Agent)");
////
////        SingleOutputStreamOperator<UbiEvent> agentIpConnectDataStream = agentConnectDataStream
////                .connect(agentIpBroadcastStream)
////                .process(new AgentIpBroadcastProcessFunction())
////                .name("Signature BotDetection(Agent+IP)");
//
////        sessionStream.addSink(new CollectSink());
////        sessionStream.print().name("test");
////        ipConnectDataStream.print().name("test");
//        // execute
//        JobExecutionResult jobExecutionResult=env.execute();
//        jobExecutionResult.isJobExecutionResult();
//
//
//       List<UbiSession> ubiSessions= ResultGenerator.getUbiSessionList("/ExpectedData");
//
//        for (UbiSession ubisession : CollectSink.values) {
////            System.out.println(JSON.toJSONString(ubisession));
//        }
//        // verify your results
//        assertTrue(CollectSink.values.containsAll(ubiSessions));
//    }
//
//    // create a testing sink
//    private static class CollectSink implements SinkFunction<UbiSession> {
//
//        // must be static
//        public static final List<UbiSession> values = new ArrayList<>();
//
//        @Override
//        public synchronized void invoke( UbiSession value ) throws Exception {
//            values.add(value);
//        }
//    }
//
//    // create a testing sink
//    private static class CollectSink2 implements SinkFunction<UbiEvent> {
//
//        // must be static
//        public static final List<UbiEvent> values = new ArrayList<>();
//
//        @Override
//        public synchronized void invoke( UbiEvent value ) throws Exception {
//            values.add(value);
//        }
//    }
//}
