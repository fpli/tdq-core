package com.ebay.sojourner.batch.connector.util;

public class Constants {

  /*
    kafka source and sink config
    1. consumer config
    2. producer config
   */

  public static final String KAFKA_COMMON_CONSUMER_BROKERS_DEFAULT = "kafka.common.consumer.brokers.default";
  public static final String KAFKA_COMMON_CONSUMER_BROKERS_BOT = "kafka.common.consumer.brokers.bot";
  public static final String KAFKA_COMMON_CONSUMER_BROKERS_NON_BOT = "kafka.common.consumer.brokers.non-bot";
  public static final String KAFKA_COMMON_CONSUMER_TOPIC_DEFAULT = "kafka.common.consumer.topic.default";
  public static final String KAFKA_COMMON_CONSUMER_TOPIC_BOT = "kafka.common.consumer.topic.bot";
  public static final String KAFKA_COMMON_CONSUMER_TOPIC_NON_BOT = "kafka.common.consumer.topic.non-bot";
  public static final String KAFKA_COMMON_CONSUMER_GROUP_ID_DEFAULT = "kafka.common.consumer.group-id.default";
  public static final String KAFKA_COMMON_CONSUMER_GROUP_ID_BOT = "kafka.common.consumer.group-id.bot";
  public static final String KAFKA_COMMON_CONSUMER_GROUP_ID_NON_BOT = "kafka.common.consumer.group-id.non-bot";

  // producer config
  public static final String KAFKA_COMMON_PRODUCER_BROKERS_DEFAULT = "kafka.common.producer.brokers.default";
  public static final String KAFKA_COMMON_PRODUCER_TOPIC_DEFAULT = "kafka.common.producer.topic.default";
  public static final String KAFKA_COMMON_PRODUCER_MESSAGE_KEY = "kafka.common.producer.message-key";

  /*
    flink config
    1. parallelism config
    2. app name config
   */

  // parallelism config
  public static final String SOURCE_DEFAULT_PARALLELISM = "flink.app.parallelism.source.default";
  public static final String SOURCE_BOT_PARALLELISM = "flink.app.parallelism.source.bot";
  public static final String SOURCE_NON_BOT_PARALLELISM = "flink.app.parallelism.source.non-bot";
  public static final String SINK_HDFS_PARALLELISM = "flink.app.parallelism.sink.hdfs";
  public static final String SINK_KAFKA_PARALLELISM = "flink.app.parallelism.sink.kafka";

  // app name config
  public static final String NAME_HDFS_DUMP_PIPELINE = "flink.app.name.hdfs-dump";
  public static final String NAME_KAFKA_DUMP_PIPELINE = "flink.app.name.kafka-dump";

  /*
   hdfs config
  */
  public static final String HDFS_DUMP_PATH = "hdfs.dump.path";
  public static final String HDFS_DUMP_CLASS = "hdfs.dump.class";

}
