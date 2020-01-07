//package com.ebay.sojourner.ubd.rt.pipleline;
//
//import com.ebay.sojourner.ubd.rt.IncrementMapFunction;
//import io.flinkspector.core.input.Input;
//import io.flinkspector.core.input.InputBuilder;
//import io.flinkspector.core.quantify.MatchTuples;
//import io.flinkspector.core.quantify.OutputMatcher;
//import io.flinkspector.datastream.DataStreamTestBase;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.sink.SinkFunction;
//import org.apache.flink.test.util.MiniClusterWithClientResource;
//import org.junit.ClassRule;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.assertTrue;
//
//
//public class PipelineTest extends DataStreamTestBase {
////    @ClassRule
////    public static MiniClusterWithClientResource flinkCluster =
////            new MiniClusterWithClientResource(
////                    new MiniClusterResourceConfiguration.Builder()
////                            .setNumberSlotsPerTaskManager(2)
////                            .setNumberTaskManagers(1)
////                            .build());
//
//
//    /**
//     * Transformation to test.
//     * Builds 20 second windows and sums up the integer values.
//     *
//     * @param stream input {@link DataStream}
//     * @return {@link DataStream}
//     */
//    public static DataStream<Tuple2<Integer, String>> window( DataStream<Tuple2<Integer, String>> stream) {
//        return stream;
//    }
//
//    @org.junit.Test
//    public void testWindowing() {
//
//        setParallelism(2);
//
//        /*
//         * Define the input DataStream:
//         * Get a EventTimeSourceBuilder with, .createTimedTestStreamWith(record).
//         * Add data records to it and retrieve a DataStreamSource
//         * by calling .close().
//         *
////         * Note: The before and after keywords define the time span !between! the previous
//         * record and the current record.
//         */
//        DataStream<Tuple2<Integer, String>> testStream =
//                createTestStreamWith(Tuple2.of(1, "fritz"))
//                        .emit(Tuple2.of(2, "fritz"))
//                        //it's possible to generate unsorted input
//                        .emit(Tuple2.of(2, "fritz"))
//                        //emit the tuple multiple times, with the time span between:
//                        .emit(Tuple2.of(1, "peter"))
//                        .close();
//
//        /*
//         * Creates an OutputMatcher using MatchTuples.
//         * MatchTuples builds an OutputMatcher working on Tuples.
//         * You assign String identifiers to your Tuple,
//         * and add hamcrest matchers testing the values.
//         */
//        OutputMatcher<Tuple2<Integer, String>> matcher =
//                //name the values in your tuple with keys:
//                new MatchTuples<Tuple2<Integer, String>>("value", "name")
//                        //add an assertion using a value and hamcrest matchers
//                        .assertThat("value", greaterThan(2))
//                        .assertThat("name", either(is("fritz")).or(is("peter")))
//                        //express how many matchers must return true for your test to pass:
//                        .anyOfThem()
//                        //define how many records need to fulfill the
//                        .onEachRecord();
//
//        /*
//         * Use assertStream to map DataStream to an OutputMatcher.
//         * You're also able to combine OutputMatchers with any
//         * OutputMatcher. E.g:
//         * assertStream(swap(stream), and(matcher, outputWithSize(greaterThan(4))
//         * would additionally assert that the number of produced records is exactly 3.
//         */
//        assertStream(testStream, matcher);
//
//    }
//
//    @Test
//    public void testIncrementPipeline() throws Exception,Throwable {
////        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
////         DataStreamTestBase dataStreamTestBase = new DataStreamTestBase();
////        dataStreamTestBase.initialize();
//        // configure your test environment
////        env.setParallelism(2);
//        setParallelism(2);
//        // values are collected in a static variable
//        CollectSink.values.clear();
//        Input<Tuple2<String,Integer>> input = InputBuilder
//                .startWith(Tuple2.of("one",1));
////                .emit(Tuple2.of("two",2))
////                .emit(Tuple2.of("three",3));
////                .repeatAll(times(2))
////                .emit(Tuple2.of("four",3),times(3));
//
//        DataStream<Tuple2<String,Integer>> dataStream =createTestStream(input);
//        dataStream.print();
//        System.out.println("test");
//        dataStream.map(new IncrementMapFunction())
//                .addSink(new CollectSink());
//        // create a stream of custom elements and apply transformations
////        env.fromElements(1L, 21L, 22L)
////                .map(new IncrementMapFunction())
////                .addSink(new CollectSink());
//
//        // execute
////        env.execute();
//
////        execute();
//        this.executeTest();
//        ArrayList<Integer> alss = new ArrayList<Integer>();
//        alss.add(2);
////        alss.add(4L);
////        alss.add(3L);
//
////        ArrayList<Long> alss2 = new ArrayList<Long>();
////        alss2.add(2L);
////        alss2.add(4L);
////        alss2.add(3L);
//        for(Integer value:CollectSink.values)
//        {
//            System.out.println(value);
//        }
//        // verify your results
//
//        assertTrue(CollectSink.values.containsAll(alss));
//    }
//
//    // create a testing sink
//    private static class CollectSink implements SinkFunction<Integer> {
//
//        // must be static
//        public static final List<Integer> values = new ArrayList<>();
//
//        @Override
//        public synchronized void invoke(Integer value) throws Exception {
//            values.add(value);
//        }
//    }
//
//    // create a testing sink
//    private static class CollectSink2 implements SinkFunction<Long> {
//
//        // must be static
//        public static final List<Long> values = new ArrayList<>();
//
//        @Override
//        public synchronized void invoke(Long value) throws Exception {
//            values.add(value);
//        }
//    }
//
//
//}
