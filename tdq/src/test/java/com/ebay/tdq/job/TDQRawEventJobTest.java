package com.ebay.tdq.job;

import com.ebay.tdq.Application;

/**
 * @author juntzhang
 */
public class TDQRawEventJobTest {

    public static void main(String[] args) throws Exception {
        Application.main(args);
//        run(args);
    }
/*
    private static void run(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.registerTypeWithKryoSerializer(Expression.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(PhysicalPlan.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(TdqMetric.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(TransformationConfig.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(TdqConfig.class, JavaSerializer.class);

//        DataStream<RawEvent> rawEventDataStream = BehaviorPathfinderSource.build(env);

        DataStream<PhysicalPlan> mappingSourceStream =TdqConfigSource.build(env);

        MapStateDescriptor<String, PhysicalPlan> stateDescriptor = new MapStateDescriptor<>(
                "tdqConfigMappingBroadcastState",
                BasicTypeInfo.STRING_TYPE_INFO,
                TypeInformation.of(new TypeHint<PhysicalPlan>() {
                }));

        BroadcastStream<PhysicalPlan> broadcastStream =
                mappingSourceStream.broadcast(stateDescriptor);

        DataStream<RawEvent> rawEventDataStream = MockBehaviorPathfinderSource.build(env);

//        rawEventDataStream
//                .map(rawEvent -> rawEvent.getSojA().get("p") +
//                        "," + rawEvent.getClientData().getContentLength() +
//                        "," + rawEvent.getSojA().get("t") +
//                        "," + rawEvent.getSojA().get("itm") +
//                        "," + rawEvent.getSojA().get("TDuration") +
//                        "," + FastDateFormat.getInstance("yyyy:MM:dd HH:mm:ss").format(rawEvent.getEventTimestamp()))
//                .addSink(new LocalFileSink("src", "page_id,contentLength,site_id,itm,TDuration," +
//                        "eventTime"))
//                .name("Src File Sink")
//                .uid("src-file-sink")
//                .setParallelism(1);

        TdqMetricProcessWindowTagFunction windowFunction = new TdqMetricProcessWindowTagFunction();
        SingleOutputStreamOperator<TdqMetric> preAggrStreamOperator = rawEventDataStream
                .connect(broadcastStream)
                .process(new TdqRawEventProcessFunction(stateDescriptor))
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                                .withTimestampAssigner((SerializableTimestampAssigner<TdqMetric>) (event,
                                        timestamp) -> event.getEventTime())
                                .withIdleness(Duration.ofSeconds(1))
                )
                .keyBy(TdqMetric::getUid)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .aggregate(new TdqAggregateFunction(), windowFunction)
                .setParallelism(2)
//                .slotSharingGroup("metrics-collector-post")
                .name("TDQ Metrics Pre Collector")
                .uid("tdq-metrics-pre-collector");

        windowFunction.tagMap.forEach((seconds, tag) -> {
            String key = Duration.ofSeconds(seconds).toString();

            preAggrStreamOperator.getSideOutput(tag).print("pre " + key).uid("pre_" + key).name(
                    "pre " + key).setParallelism(1);

            preAggrStreamOperator
                    .getSideOutput(tag)
//                    .map(e -> {
//                        // Align the top of the time
//                        // [start, end)
//                        long format = Time.seconds(seconds).toMilliseconds();
//                        long t      = (e.getEventTime() / format) * format;
//                        if (t - e.getEventTime() != 0) {
//                            e.setEventTime(t + format);
//                        }
//                        return e;
//                    })
//                    .assignTimestampsAndWatermarks(
//                            WatermarkStrategy
//                                    .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
//                                    .withTimestampAssigner((SerializableTimestampAssigner<TdqMetric>) (event,
//                                            timestamp) -> event.getEventTime())
//                                    .withIdleness(Duration.ofSeconds(1))
//                    )
                    .keyBy(TdqMetric::getUid)
                    .window(TumblingEventTimeWindows.of(Time.seconds(seconds)))
                    .aggregate(new TdqAggregateFunction())
//                    .slotSharingGroup("metrics-collector-final")
                    .setParallelism(1)
                    .name("TDQ Metrics Collector Window=" + key)
                    .uid("tdq-metrics-collector-" + key)
                    .print("final " + key)
                    .uid("final_" + key)
                    .name("final " + key)
                    .setParallelism(1);
        });
        env.execute("RawEvent Job Test");
    }*/

}
