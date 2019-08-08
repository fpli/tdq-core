package com.ebay.sojourner.ubd.rt;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.rt.connectors.kafka.RawEventDeserializationSchema;
import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import io.ebay.rheos.schema.avro.GenericRecordDomainDataDecoder;
import io.ebay.rheos.schema.event.RheosEvent;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.SaslConfigs;

import java.util.*;
import java.util.stream.Collectors;

public class ConsumerExampleWithIAF {
    public static void main(String[] args) throws Exception{

        final StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        String BOOTSTRAP_SERVERS = Arrays.asList(
                "rhs-vvrvkiaa-kfk-lvs-1.rheos-streaming-qa.svc.32.tess.io:9092",
                "rhs-vvrvkiaa-kfk-lvs-2.rheos-streaming-qa.svc.32.tess.io:9092",
                "rhs-vvrvkiaa-kfk-lvs-3.rheos-streaming-qa.svc.32.tess.io:9092",
                "rhs-vvrvkiaa-kfk-lvs-4.rheos-streaming-qa.svc.32.tess.io:9092",
                "rhs-vvrvkiaa-kfk-lvs-5.rheos-streaming-qa.svc.32.tess.io:9092")
                .stream().collect(Collectors.joining(","));

        Properties props = new Properties();
        // This is the broker addresses that can be looked up in Rheos portal
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        // This is the consumer name that is registered in Rheos
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sojourner");
        // Rheos uses byte[] as key
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        // Rheos event deserializer that decodes messages stored in Kafka to RheosEvent. This is optional if you dont use RheosEvent
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.ebay.rheos.schema.avro.RheosEventDeserializer");

        props.put("sasl.mechanism", "IAF");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "io.ebay.rheos.kafka.security.iaf.IAFLoginModule required iafConsumerId=\"urn:ebay-marketplace-consumerid:68a97ac2-013b-4915-9ed7-d6ae2ff01618\" iafSecret=\"6218c197-200e-49d7-b404-2a4dbf7595ef\" iafEnv=\"staging\";");
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());

        final KafkaConsumer<byte[], RheosEvent> kafkaConsumer = new KafkaConsumer<>(props);

//        kafkaConsumer.subscribe(Arrays.asList("behavior.pathfinder.events.total"));

//        Map<String, Object> config = new HashMap<>();
//        config.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, "https://rheos-services.qa.ebay.com");
//        GenericRecordDomainDataDecoder decoder = new GenericRecordDomainDataDecoder(config);
//
//        while (true) {
//            ConsumerRecords<byte[], RheosEvent> consumerRecords = kafkaConsumer.poll(Long.MAX_VALUE);
//            Iterator<ConsumerRecord<byte[], RheosEvent>> iterator = consumerRecords.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(decoder.decode(iterator.next().value()));
//            }
//        }

        FlinkKafkaConsumer<RawEvent> consumer = new FlinkKafkaConsumer<>("behavior.pathfinder.events.total",
                new RawEventDeserializationSchema(), props);

        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                consumer.assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                            @Override
                            public long extractTimestamp(RawEvent element) {
                                return element.getRheosHeader().getEventCreateTimestamp();
                            }
                        }
                ))
                .name("Rheos Consumer");

        rawEventDataStream.print();

        executionEnvironment.execute("test kafka consumer");

    }
}
