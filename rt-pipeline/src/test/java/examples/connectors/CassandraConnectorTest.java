package examples.connectors;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class CassandraConnectorTest {

  // get the execution environment
  final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

  // get input data by connecting to the socket
  DataStream<String> text = env.socketTextStream("127.0.0.1", 8096, "\n");

  // parse the data, group it, window it, and aggregate the counts
  DataStream<Tuple2<String, Long>> result =
      text.flatMap(
          new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Long>> out) {
              // normalize and split the line
              String[] words = value.toLowerCase().split("\\s");

              // emit the pairs
              for (String word : words) {
                // Do not accept empty word, since word is defined as primary key in C* table
                if (!word.isEmpty()) {
                  out.collect(new Tuple2<String, Long>(word, 1L));
                }
              }
            }
          })
          .keyBy(0)
          .timeWindow(Time.seconds(5))
          .sum(1);

  //        CassandraSink.addSink(result)
  //            .setQuery("INSERT INTO example.wordcount(word, count) values (?, ?);")
  //        .setHost("127.0.0.1")
  //        .build();

}
