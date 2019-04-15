package com.ebay.sojourner.ubd.connectors.kafka;

import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.SojEvent;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class KafkaConnectorFactory {

    public static String CLIENT_ID = "43665ed9-5673-4d92-8ea7-21decd34c903";

    public static String TOPIC_PATHFINDER_EVENTS = "behavior.pathfinder.events.total";

    public static String BOOTSTRAP_SERVERS = Arrays.asList(
            "rheos-kafka-proxy-1.lvs02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-2.lvs02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-3.lvs02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-1.phx02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-2.phx02.dev.ebayc3.com:9092",
            "rheos-kafka-proxy-3.phx02.dev.ebayc3.com:9092")
            .stream().collect(Collectors.joining(","));

    public static FlinkKafkaConsumer<RawEvent> createKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sojourner-ubd");
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
                RoundRobinAssignor.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                RheosEventDeserializer.class.getName());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID);

        return new FlinkKafkaConsumer<>(TOPIC_PATHFINDER_EVENTS,
                new RawEventDeserializationSchema(), props);
    }

    public static FlinkKafkaProducer<SojEvent> createKafkaProducer() {
        return null;
    }
}
