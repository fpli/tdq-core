package com.ebay.sojourner.ubd.connectors;

import com.ebay.sojourner.ubd.model.SojEvent;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;

import java.util.Arrays;
import java.util.stream.Collectors;

public class KafkaConnectorFactory {

    public static String BOOTSTRAP_SERVERS = Arrays.asList(
            "rheos-kafka-proxy-1.lvs02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-2.lvs02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-3.lvs02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-1.phx02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-2.phx02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-3.phx02.dev.ebayc3.com:9092")
            .stream().collect(Collectors.joining(","));

    public FlinkKafkaConsumer010<SojEvent> createKafkaConsumer() {
        return null;
    }

    public FlinkKafkaProducer010<SojEvent> createKafkaProducer() {
        return null;
    }
}
