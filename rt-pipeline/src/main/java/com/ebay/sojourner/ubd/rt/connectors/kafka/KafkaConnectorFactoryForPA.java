package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SojEvent;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class KafkaConnectorFactoryForPA {

//    public static String CLIENT_ID = "82034abc-572d-4b71-82df-c9820ef1627c";
    public static String GROUP_ID = "sojourner";

//    public static String CLIENT_ID = "43665ed9-5673-4d92-8ea7-21decd34c903";


    public static String TOPIC_PATHFINDER_EVENTS = "behavior.pathfinder.events.total";

    public static String BOOTSTRAP_SERVERS = Arrays.asList(
            "rhs-vvrvkiaa-kfk-lvs-1.rheos-streaming-qa.svc.32.tess.io:9092",
            "rhs-vvrvkiaa-kfk-lvs-2.rheos-streaming-qa.svc.32.tess.io:9092",
            "rhs-vvrvkiaa-kfk-lvs-3.rheos-streaming-qa.svc.32.tess.io:9092",
            "rhs-vvrvkiaa-kfk-lvs-4.rheos-streaming-qa.svc.32.tess.io:9092",
            "rhs-vvrvkiaa-kfk-lvs-5.rheos-streaming-qa.svc.32.tess.io:9092")
            .stream().collect(Collectors.joining(","));

    public static FlinkKafkaConsumer<GenericRecord> createKafkaConsumer() {

        Properties props = new Properties();
        props.put("sasl.mechanism", "IAF");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "io.ebay.rheos.kafka.security.iaf.IAFLoginModule required iafConsumerId=\"urn:ebay-marketplace-consumerid:68a97ac2-013b-4915-9ed7-d6ae2ff01618\" iafSecret=\"6218c197-200e-49d7-b404-2a4dbf7595ef\" iafEnv=\"staging\";");

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
                RoundRobinAssignor.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                RheosEventDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
//        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID);

        return new FlinkKafkaConsumer<GenericRecord>(TOPIC_PATHFINDER_EVENTS,
                new RawEventDeserializationSchemaForPA(), props);
    }



    public static FlinkKafkaProducer<SojEvent> createKafkaProducer() {
        return null;
    }
}
