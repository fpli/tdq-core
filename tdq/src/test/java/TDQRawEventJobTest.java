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
import com.ebay.tdq.rules.TdqProcessFunction;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
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
                TypeInformation.of(new TypeHint<TdqConfig>() {}));

        BroadcastStream<TdqConfig> broadcastStream = mappingSourceStream.broadcast(stateDescriptor);

        // todo pre agg by uuid
        DataStream<TdqMetric> pre = src1
                .union(src2).connect(broadcastStream)
                .process(new TdqProcessFunction(stateDescriptor))
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<TdqMetric>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                                .withTimestampAssigner((SerializableTimestampAssigner<TdqMetric>) (event,
                                        timestamp) -> event.getEventTime())
                                .withIdleness(Duration.ofSeconds(1))
                )
                .keyBy(TdqMetric::getUid)
                .timeWindow(Time.seconds(2))
                .aggregate(new TdqAggregateFunction());

        pre
                .keyBy(TdqMetric::getUid)
                .timeWindow(Time.seconds(10))
                .aggregate(new TdqAggregateFunction())
                .print()
                .setParallelism(2);

        env.execute("RawEvent Job Test");
    }

}
