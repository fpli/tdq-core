package com.ebay.sojourner.ubd.pipeline;

import com.ebay.sojourner.ubd.connectors.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.model.RawEvent;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class UBDStreamingJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactory.createKafkaConsumer());
        rawEventDataStream.print();

        executionEnvironment.execute("unified bot detection");

    }
}
