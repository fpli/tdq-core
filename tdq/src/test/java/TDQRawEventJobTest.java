import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.config.ExpressionConfig;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.SinkConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.config.TransformationConfig;
import com.ebay.tdq.rules.TdqAggregateFunction;
import com.ebay.tdq.rules.TdqConfigSourceFunction;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.rules.TdqRawEventProcessFunction;
import com.ebay.tdq.rules.TdqTdqMetricProcessWindowFunction;
import com.ebay.tdq.sinks.LocalFileSink;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import java.time.Duration;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author juntzhang
 */
public class TDQRawEventJobTest {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<RawEvent> src1 = env.addSource(new TDQRawEventSourceFunction()).name("Raw Event Src1").uid(
                "raw_event_src1");
        DataStream<RawEvent> src2 = env.addSource(new TDQRawEventSourceFunction()).name("Raw Event Src2").uid(
                "raw_event_src2");

        env.registerTypeWithKryoSerializer(TdqMetric.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(ExpressionConfig.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(TransformationConfig.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(ProfilerConfig.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(RuleConfig.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(SinkConfig.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(SourceConfig.class, JavaSerializer.class);
        env.registerTypeWithKryoSerializer(TdqConfig.class, JavaSerializer.class);

        DataStream<TdqConfig> mappingSourceStream = env
                .addSource(new TdqConfigSourceFunction("", 30000L, "dev"))
                .name("Tdq Config Source")
                .uid("tdq-config-source")
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<TdqConfig>forBoundedOutOfOrderness(Duration.ofMinutes(0))
                        .withIdleness(Duration.ofSeconds(1)))
                .setParallelism(1)
                .name("Tdq Config Watermark Source")
                .uid("tdq-config-watermark-source");

        MapStateDescriptor<String, TdqConfig> stateDescriptor = new MapStateDescriptor<>(
                "tdqConfigMappingBroadcastState",
                BasicTypeInfo.STRING_TYPE_INFO,
                TypeInformation.of(new TypeHint<TdqConfig>() {
                }));

        BroadcastStream<TdqConfig> broadcastStream = mappingSourceStream.broadcast(stateDescriptor);

        DataStream<RawEvent> src = src1.union(src2);

        src
                .map(rawEvent -> rawEvent.getSojA().get("p") +
                        "," + rawEvent.getClientData().getContentLength() +
                        "," + rawEvent.getSojA().get("t") +
                        "," + rawEvent.getSojA().get("itm") +
                        "," + rawEvent.getSojA().get("TDuration") +
                        "," + FastDateFormat.getInstance("yyyy:MM:dd HH:mm:ss").format(rawEvent.getEventTimestamp()))
                .addSink(new LocalFileSink("src", "page_id,contentLength,site_id,itm,TDuration,eventTime"))
                .name("Src File Sink")
                .uid("src-file-sink")
                .setParallelism(1);

        TdqTdqMetricProcessWindowFunction windowFunction = new TdqTdqMetricProcessWindowFunction();
        SingleOutputStreamOperator<TdqMetric> preAggrStreamOperator = src
                .connect(broadcastStream)
                .process(new TdqRawEventProcessFunction(stateDescriptor))
                .map(e -> {
                    // 00,01,02,03,04,05,07,09,10.0001
                    // 00,05,05,05,05,05,10,10,10   00:(55,00]  05:(00,05]  10:(05,10]
                    // 00,10,10,10,10,10,10,10,10   10:(00,10]
                    long format = Time.seconds(5).toMilliseconds();
                    long t      = (e.getEventTime() / format) * format;
                    if (t - e.getEventTime() != 0) {
                        e.setEventTime(t + format);
                    }
                    return e;
                })
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                                .withTimestampAssigner((SerializableTimestampAssigner<TdqMetric>) (event,
                                        timestamp) -> event.getEventTime())
                                .withIdleness(Duration.ofSeconds(1))
                )
                .keyBy(TdqMetric::getUid)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .aggregate(new TdqAggregateFunction(), windowFunction);

        windowFunction.tagMap.forEach((seconds, tag) -> {
            String key = Duration.ofSeconds(seconds).toString();

            preAggrStreamOperator.getSideOutput(tag).print("pre " + key).uid("pre_" + key).name("pre " + key).setParallelism(1);

            preAggrStreamOperator
                    .getSideOutput(tag)
                    .map(e -> {
                        // Align the top of the time
                        // [start, end)
                        long format = Time.seconds(seconds).toMilliseconds();
                        long t      = (e.getEventTime() / format) * format;
                        if (t - e.getEventTime() != 0) {
                            e.setEventTime(t + format);
                        }
                        return e;
                    })
                    .assignTimestampsAndWatermarks(
                            WatermarkStrategy
                                    .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                                    .withTimestampAssigner((SerializableTimestampAssigner<TdqMetric>) (event,
                                            timestamp) -> event.getEventTime())
                                    .withIdleness(Duration.ofSeconds(1))
                    )
                    .keyBy(TdqMetric::getUid)
                    .window(TumblingEventTimeWindows.of(Time.seconds(seconds)))
                    .aggregate(new TdqAggregateFunction())
                    .print("final " + key)
                    .uid("final_" + key)
                    .name("final " + key)
                    .setParallelism(1);
        });

        env.execute("RawEvent Job Test");
    }

}
