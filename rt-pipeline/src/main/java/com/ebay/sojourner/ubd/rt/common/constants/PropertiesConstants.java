package com.ebay.sojourner.ubd.rt.common.constants;

/**
 * Created 2019-08-01 10:12
 *
 * @author : Unikal Liu
 * @version : 1.0.0
 */
public class PropertiesConstants {

  public static final String APPLICATION_PROPERTIES_FILENAME = "application.properties.filename";
  public static final String DEFAULT_APPLICATION_PROPERTIES_FILENAME = "/application.properties";
  public static final String KITE2_PROPERTIES_FILENAME = "kite2.properties.filename";
  public static final String DEFAULT_KITE2_PROPERTIES_FILENAME = "/kite2.properties";
  public static final String PROD_CONFIG = "prod.config";

  // Flink
  public static final String STREAM_PARALLELISM = "stream.parallelism";
  public static final String STREAM_SINK_PARALLELISM = "stream.sink.parallelism";
  public static final String STREAM_DEFAULT_PARALLELISM = "stream.default.parallelism";
  public static final String STREAM_CHECKPOINT_ENABLE = "stream.checkpoint.enable";
  public static final String STREAM_CHECKPOINT_INTERVAL = "stream.checkpoint.interval";
  public static final String STREAM_CHECKPOINT_MINPAUSE = "stream.checkpoint.min.pause";
  public static final String STREAM_CHECKPOINT_TIMEOUT = "stream.checkpoint.timeout";
  public static final String STREAM_RESTARTSTRATEGIES_ATTEMPTS =
      "stream.restart.strategies.attempts";
  public static final String STREAM_RESTARTSTRATEGIES_DELAYINTERVAL =
      "stream.restart.strategies.delay.interval";
  public static final String WATERMARK_MAX_OUT_OF_ORDERNESS = "watermark.max.out.of.orderness";
  public static final String CHECKPOINT_PATH = "checkpoint.path";

  // Source - Kafka
  public static final String KAFKA_BROKERS = "kafka.brokers";
  public static final String DEFAULT_KAFKA_BROKERS = "localhost:9092";
  public static final String KAFKA_ZOOKEEPER_CONNECT = "kafka.zookeeper.connect";
  public static final String DEFAULT_KAFKA_ZOOKEEPER_CONNECT = "localhost:2181";
  public static final String KAFKA_GROUP_ID = "kafka.group.id";
  public static final String DEFAULT_KAFKA_GROUP_ID = "loyalty-dcp-tracking";
  public static final String KAFKA_TOPIC = "kafka.topic";
  public static final String KAFKA_IAF_CONSUMERID = "kafka.iaf.consumerid";
  public static final String KAFKA_IAF_SECRET = "kafka.iaf.secret";
  public static final String KAFKA_IAF_ENV = "kafka.iaf.env";
  public static final String DEFAULT_KAFKA_IAF_ENV = "staging";
  public static final String KAFKA_AUTO_OFFSET_RESET = "auto.offset.reset";
  public static final String KAFKA_CONSUMER_FROM_TIME = "kafka.consumer.from.time";
  public static final String RHEOS_SERVICES_URL = "rheos.services.url";

  // Sink - Hdfs
  public static final String HDFS_DATA_PATH = "hdfs.data.path";
  public static final String HDFS_WRITE_BUFFER = "hdfs.write.buffer";
  public static final String HDFS_ROW_COUNT = "hdfs.row.count";

  // Sink - Mysql
  public static final String MYSQL_URL = "mysql.url";
  public static final String MYSQL_USERNAME = "mysql.username";
  public static final String MYSQL_PASSWORD = "mysql.password";
  public static final String MYSQL_INSERT_SQL = "mysql.insert.sql";
}
