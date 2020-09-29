package com.ebay.sojourner.flink.common.util;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataStreamRescaledBuilder {

  public static <T> DataStream<T> buildKafkaSource(StreamExecutionEnvironment environment,
      String dc,
      Class<T> tClass) {

    // kafka source
    SourceDataStreamBuilder<T> dataStreamBuilder = new SourceDataStreamBuilder<>(
        environment, tClass);

    DataStream<T> byteSourceDataStream = dataStreamBuilder.buildForDumper(
        DataCenter.valueOf(dc),
        FlinkEnvUtils.getString(Property.SOURCE_OPERATOR_NAME),
        FlinkEnvUtils.getString(Property.SOURCE_UID));

    // rescale datastream
    return byteSourceDataStream.rescale();

  }

  public static <T> DataStream<T> build(DataStream<T> dataStream) {

    return dataStream.rescale();
  }
}
