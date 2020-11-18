package com.ebay.sojourner.rt.pipeline;

import java.time.Duration;
import java.util.Properties;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;

/**
 * This job is used to test idle Kafka sources.
 * <p>
 * bin/zookeeper-server-start.sh config/zookeeper.properties bin/kafka-server-start.sh
 * config/server.properties bin/kafka-topics.sh --create --zookeeper localhost:2181
 * --replication-factor 1 --partitions 1 --topic topic1 bin/kafka-topics.sh --list --zookeeper
 * localhost:2181 bin/kafka-console-producer.sh --broker-list localhost:9092 --topic topic1
 * <p>
 * See more at: https://kafka.apache.org/11/documentation.html#quickstart
 */
public class SimpleJobForIdleSources {

  public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    env.setParallelism(1);
    env.disableOperatorChaining();

    // Kafka consumers
    String bootStrapServers = "localhost:9092";
    String groupId = "mygroup";
    String offset = "latest";
    Properties consumerConfig1 = new Properties();
    consumerConfig1.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
    consumerConfig1.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    consumerConfig1.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset);
    FlinkKafkaConsumerBase source1 = new FlinkKafkaConsumer("topic1", new SimpleStringSchema(),
        consumerConfig1).assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(
        Duration.ofSeconds(1)).withIdleness(Duration.ofMinutes(5)));

    Properties consumerConfig2 = new Properties();
    consumerConfig2.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
    consumerConfig2.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    consumerConfig2.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset);
    FlinkKafkaConsumerBase source2 = new FlinkKafkaConsumer("topic2", new SimpleStringSchema(),
        consumerConfig2).assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(
        Duration.ofSeconds(1)).withIdleness(Duration.ofMinutes(5)));

    // Setup the pipeline
    env.addSource(source1).union(env.addSource(source2)).flatMap(
        new FlatMapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            String[] words = value.split(",");
            for (String word : words) {
              out.collect(new Tuple2(word.trim(), 1));
            }
          }
        }).keyBy(0).window(EventTimeSessionWindows.withGap(Time.minutes(1))).sum(1).print();

    // Execute the pipeline
    env.execute("Simple Job For Idle Sources");
  }

}
