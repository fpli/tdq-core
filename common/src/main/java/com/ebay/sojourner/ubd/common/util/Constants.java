package com.ebay.sojourner.ubd.common.util;

public class Constants {

  // Version Control for POJO
  public static final int SESSION_VERSION = 3;
  public static final int EVENT_VERSION = 3;
  public static final int CONTAINER_VERION = 3;

  // Session Relatives
  public static final int SESSION_KEY_DIVISION = 100;
  public static final long DEFAULT_MAX_EVENTS_IN_SESSION = 10000;
  // Filter Context
  public static final String FILTER_NAME_DELIMITER = ",";
  // Cache and File Control
  public static final long DEFAULT_POOL_THRESHOLD = 50000;
  public static final long EOD_BOT_POOL_THRESHOLD = 1000000; // 1,000,000
  public static final long EOD_BOT_ROLLOUT_THRESHOLD = 10000000; // 10,000,000
  // Date Format for Data Partition
  public static final char[] HEX_DIGITS =
      new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  public static final String NO_SESSION_ID = "NO_SESSION_ID";
  public static final long NO_TIMESTAMP = Long.MIN_VALUE;
  public static final String DEFAULT_SESSION_FILE_NAME = "session";

  public static final long DEFAULT_TRACK_PERIOD = 10 * 1000L; // Default is 10 seconds

  public static final long DEFAULT_LARGE_SESSION_EVENTS = 100000;

  public static final int DEFAULT_CORE_SITE_COBRAND = 0;
  public static final int MOBILE_APP_COBRAND = 6;
  public static final int DESKTOP_APP_COBRAND = 11;
  public static final int EIM_APP_COBRAND = 10;
  public static final int CLASSIFIED_SITE_COBRAND = 5;
  public static final int HALF_SITE_COBRAND = 1;
  public static final int EBAYEXPRESS_SITE_COBRAND = 2;
  public static final int SHOPPING_SITE_COBRAND = 3;
  public static final int ARTISAN_COBRAND = 4;
  public static final int MOBILE_CLASSIFIED_COBRAND = 9;
  public static final int MOBILE_CORE_SITE_COBRAND = 7;
  public static final int MOBILE_HALF_COBRAND = 8;
  public static final int HASH_STUB = 37;

  // dynamicRules category
  public static final String EVENT_LEVEL = "Event";
  public static final String SESSION_LEVEL = "Session";
  public static final String AGENTIP_LEVEL = "AgentIp";
  public static final String AGENT_LEVEL = "Agent";
  public static final String IP_LEVEL = "Ip";
  public static final String GUID_LEVEL = "Guid";

  // zookeeper
  public static final String ZOOKEEPER_CONNECTION_STRING =
      "zk-0.sojzk-ns.svc.32.tess.io:2181,"
          + "zk-1.sojzk-ns.svc.32.tess.io:2181,"
          + "zk-2.sojzk-ns.svc.32.tess.io:2181";
  public static final int ZK_CONNECTION_TIME_OUT = 18000;
  public static final int ZK_SESSION_TIME_OUT = 18000;
  public static final int ZK_RETRY_TIMES = 3;
  public static final int ZK_BASE_SLEEP_TIME = 1000;
  public static final String ZK_NAMESPACE = "sojourner";


  /*
    kafka common config
    1. consumer config
    2. producer config
    3. rheos config
   */

  // consumer
  public static final String MAX_POLL_RECORDS = "kafka.common.consumer.max-poll-records";
  public static final String RECEIVE_BUFFER = "kafka.common.consumer.receive-buffer";
  public static final String FETCH_MAX_BYTES = "kafka.common.consumer.fetch-max-bytes";
  public static final String FETCH_MAX_WAIT_MS = "kafka.common.consumer.fetch-max-wait-ms";
  public static final String MAX_PARTITIONS_FETCH_BYTES = "kafka.common.consumer.max-partitions-fetch-bytes";
  public static final String AUTO_RESET_OFFSET = "kafka.common.consumer.auto-offset-reset";

  // producer
  public static final String BATCH_SIZE = "kafka.common.producer.batch-size";
  public static final String REQUEST_TIMEOUT_MS = "kafka.common.producer.request-timeout-ms";

  // rheos
  public static final String RHEOS_KAFKA_REGISTRY_URL = "rheos.serviceUrl";
  public static final String IAF_SECRET = "rheos.iaf.secret";
  public static final String IAF_ENV = "rheos.iaf.env";

  /*
    kafka source and sink config
    1. source config
    2. sink config
   */

  // source config
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_RNO = "kafka.stream.behavior-pathfinder.bootstrap-servers.rno";
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_SLC = "kafka.stream.behavior-pathfinder.bootstrap-servers.slc";
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_LVS = "kafka.stream.behavior-pathfinder.bootstrap-servers.lvs";
  public static final String BEHAVIOR_PATHFINDER_TOPIC = "kafka.stream.behavior-pathfinder.topic";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_RNO = "kafka.stream.behavior-pathfinder.group-id.default.rno";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_SLC = "kafka.stream.behavior-pathfinder.group-id.default.slc";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_LVS = "kafka.stream.behavior-pathfinder.group-id.default.lvs";

  public static final String BEHAVIOR_TOTAL_BOOTSTRAP_SERVERS_DEFAULT = "kafka.stream.behavior-total.bootstrap-servers.default";
  public static final String BEHAVIOR_TOTAL_TOPIC = "kafka.stream.behavior-total.topic";
  public static final String BEHAVIOR_TOTAL_GROUP_ID_EVENT_NON_BOT = "kafka.stream.behavior-total.group-id.event.non-bot";

  public static final String BEHAVIOR_PULSAR_BOOTSTRAP_SERVERS_DEFAULT = "kafka.stream.behavior-pulsar.bootstrap-servers.default";
  public static final String BEHAVIOR_PULSAR_TOPIC_SESSION_BOT = "kafka.stream.behavior-pulsar.topic.session.bot";
  public static final String BBEHAVIOR_PULSAR_TOPIC_SESSION_NON_BOT = "kafka.stream.behavior-pulsar.topic.session.non-bot";
  public static final String BEHAVIOR_PULSAR_TOPIC_EVENT_BOT = "kafka.stream.behavior-pulsar.topic.event.bot";
  public static final String BBEHAVIOR_PULSAR_GROUP_ID_SESSION_BOT = "kafka.stream.behavior-pulsar.group-id.session.bot";
  public static final String BEHAVIOR_PULSAR_GROUP_ID_SESSION_NON_BOT = "kafka.stream.behavior-pulsar.group-id.session.non-bot";
  public static final String BEHAVIOR_PULSAR_GROUP_ID_EVENT_BOT = "kafka.stream.behavior-pulsar.group-id.event.bot";

  // source & sink config
  public static final String BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT = "kafka.stream.behavior-total-new.bootstrap-servers.default";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SESSION_BOT = "kafka.stream.behavior-total-new.topic.session.bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SESSION_NON_BOT = "kafka.stream.behavior-total-new.topic.session.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_EVENT_BOT = "kafka.stream.behavior-total-new.topic.event.bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_EVENT_NON_BOT = "kafka.stream.behavior-total-new.topic.event.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_AGENT_IP = "kafka.stream.behavior-total-new.topic.signature.agent-ip";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_AGENT = "kafka.stream.behavior-total-new.topic.signature.agent";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_IP = "kafka.stream.behavior-total-new.topic.signature.ip";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_GUID = "kafka.stream.behavior-total-new.topic.signature.guid";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_DQ_SESSION = "kafka.stream.behavior-total-new.topic.dq.session";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_DQ_CROSS_SESSION = "kafka.stream.behavior-total-new.topic.dq.cross-session";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SESSION_BOT = "kafka.stream.behavior-total-new.group-id.session.bot";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SESSION_NON_BOT = "kafka.stream.behavior-total-new.group-id.session.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_EVENT_BOT = "kafka.stream.behavior-total-new.group-id.event.bot";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_EVENT_NON_BOT = "kafka.stream.behavior-total-new.group-id.event.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_AGENT_IP = "kafka.stream.behavior-total-new.group-id.signature.agent-ip";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_AGENT = "kafka.stream.behavior-total-new.group-id.signature.agent";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_IP = "kafka.stream.behavior-total-new.group-id.signature.ip";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_GUID = "kafka.stream.behavior-total-new.group-id.signature.guid";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_DQ_SESSION = "kafka.stream.behavior-total-new.group-id.dq.session";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_DQ_CROSS_SESSION = "kafka.stream.behavior-total-new.group-id.dq.cross-session";
  public static final String BEHAVIOR_TOTAL_NEW_MESSAGE_KEY_SESSION = "kafka.stream.behavior-total-new.message-key.session";
  public static final String BEHAVIOR_TOTAL_NEW_MESSAGE_KEY_EVENT = "kafka.stream.behavior-total-new.message-key.event";
  public static final String BEHAVIOR_TOTAL_NEW_MESSAGE_KEY_SIGNATURE = "kafka.stream.behavior-total-new.message-key.signatureId";

  // qa
  public static final String BEHAVIOR_TRAFFICJAM_BOOTSTRAP_SERVERS_DEFAULT = "kafka.stream.behavior-trafficjam.bootstrap-servers.default";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_SESSION_BOT = "kafka.stream.behavior-trafficjam.topic.session.bot";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_SESSION_NON_BOT = "kafka.stream.behavior-trafficjam.topic.session.non-bot";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_EVENT_BOT = "kafka.stream.behavior-trafficjam.topic.event.bot";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_EVENT_NON_BOT = "kafka.stream.behavior-trafficjam.topic.event.non-bot";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_SIGNATURE_AGENT_IP = "kafka.stream.behavior-trafficjam.topic.signature.agent-ip";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_SIGNATURE_AGENT = "kafka.stream.behavior-trafficjam.topic.signature.agent";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_SIGNATURE_IP = "kafka.stream.behavior-trafficjam.topic.signature.ip";
  public static final String BEHAVIOR_TRAFFICJAM_TOPIC_SIGNATURE_GUID = "kafka.stream.behavior-trafficjam.topic.signature.guid";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SESSION_NON_BOT = "kafka.stream.behavior-trafficjam.group-id.session.non-bot";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_EVENT_BOT = "kafka.stream.behavior-trafficjam.group-id.event.bot";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_EVENT_NON_BOT = "kafka.stream.behavior-trafficjam.group-id.event.non-bot";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_AGENT_IP = "kafka.stream.behavior-trafficjam.group-id.signature.agent-ip";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_AGENT = "kafka.stream.behavior-trafficjam.group-id.signature.agent";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_IP = "kafka.stream.behavior-trafficjam.group-id.signature.ip";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_GUID = "kafka.stream.behavior-trafficjam.group-id.signature.guid";
  public static final String BEHAVIOR_TRAFFICJAM_MESSAGE_KEY_SESSION = "kafka.stream.behavior-trafficjam.message-key.session";
  public static final String BEHAVIOR_TRAFFICJAM_MESSAGE_KEY_EVENT = "kafka.stream.behavior-trafficjam.message-key.event";
  public static final String BEHAVIOR_TRAFFICJAM_MESSAGE_KEY_SIGNATURE = "kafka.stream.behavior-trafficjam.message-key.signatureId";

  /*
    flink config
    1. checkpoint config
    2. parallelism and slot share group config
    3. app name config
   */

  // checkpoint config
  public static final String CHECKPOINT_DATA_DIR = "flink.app.checkpoint.data-dir";
  public static final String CHECKPOINT_INTERVAL_MS = "flink.app.checkpoint.interval-ms";
  public static final String CHECKPOINT_TIMEOUT_MS = "flink.app.checkpoint.timeout-ms";
  public static final String CHECKPOINT_MIN_PAUSE_BETWEEN_MS = "flink.app.checkpoint.min-pause-between-ms";
  public static final String CHECKPOINT_MAX_CONCURRENT = "flink.app.checkpoint.max-concurrent";

  // parallelism and slot share group config
  public static final String SOURCE_PARALLELISM = "flink.app.parallelism.source";
  public static final String EVENT_PARALLELISM = "flink.app.parallelism.event";
  public static final String SESSION_PARALLELISM = "flink.app.parallelism.session";
  public static final String PRE_AGENT_IP_PARALLELISM = "flink.app.parallelism.pre-agent-ip";
  public static final String AGENT_IP_PARALLELISM = "flink.app.parallelism.agent-ip";
  public static final String AGENT_PARALLELISM = "flink.app.parallelism.agent";
  public static final String IP_PARALLELISM = "flink.app.parallelism.ip";
  public static final String GUID_PARALLELISM = "flink.app.parallelism.guid";
  public static final String BROADCAST_PARALLELISM = "flink.app.parallelism.broadcast";
  public static final String METRICS_PARALLELISM = "flink.app.parallelism.metrics";

  public static final String DEFAULT_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.default";
  public static final String SOURCE_EVENT_LVS_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.source-event-lvs";
  public static final String SOURCE_EVENT_SLC_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.source-event-slc";
  public static final String SOURCE_EVENT_RNO_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.source-event-rno";
  public static final String SESSION_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.session";
  public static final String CROSS_SESSION_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.cross-session";
  public static final String BROADCAST_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.broadcast";

  // app name config
  public static final String NAME_FULL_PIPELINE = "flink.app.name.full-pipeline";
  public static final String NAME_DATA_QUALITY = "flink.app.name.data-quality";
  public static final String NAME_HOT_DEPLOY = "flink.app.name.hot-deploy";
  public static final String NAME_HDFS_DUMP = "flink.app.name.hdfs-dump";
  public static final String NAME_KAFKA_DUMP = "flink.app.name.kafka-dump";

  /*
    zookeeper config
   */
  public static final String ZOOKEEPER_SERVER = "zookeeper.server";
  public static final String ZOOKEEPER_SESSION_TIMEOUT_MS = "zookeeper.sessionTimeoutMs";
  public static final String ZOOKEEPER_CONNECTION_TIMEOUT_MS = "zookeeper.connectionTimeoutMs";
  public static final String ZOOKEEPER_MAX_RETRIES = "zookeeper.maxRetries";
  public static final String ZOOKEEPER_BASE_SLEEP_TIME_MS = "zookeeper.baseSleepTimeMs";
  public static final String ZOOKEEPER_NAMESPACE = "zookeeper.namespace";
  public static final String ZK_NODE_PATH = "zookeeper.nodepath";

  /*
    hdfs config
   */
  public static final String HDFS_PATH_PARENT = "hdfs.path.parent";
  public static final String HDFS_PATH_EVENT_BOT = "hdfs.path.event.bot";
  public static final String HDFS_PATH_EVENT_NON_BOT = "hdfs.path.event.non-bot";
  public static final String HDFS_PATH_EVENT_LATE = "hdfs.path.event.late";
  public static final String HDFS_PATH_SESSION_BOT = "hdfs.path.session.bot";
  public static final String HDFS_PATH_SESSION_NON_BOT = "hdfs.path.session.non-bot";
  public static final String HDFS_PATH_CROSS_SESSION = "hdfs.path.cross-session";
  public static final String HDFS_PATH_JETSTREAM_SESSION = "hdfs.path.jetstream.event";
  public static final String HDFS_PATH_JETSTREAM_EVENT = "hdfs.path.jetstream.session";
  public static final String HDFS_PATH_INTERMEDIATE_SESSION = "hdfs.path.intermediate-session";

  /*
    rest client config
   */
  public static final String REST_SERVER = "rest-client.rest-server";
  public static final String REST_PUBLISHED_RULE_LIST = "rest-client.published-rule-list";
  public static final String REST_SPECIFIED_RULE = "rest-client.specified-rule";
  public static final String REST_AUTH_USERNAME_KEY = "rest-client.auth-username-key";
  public static final String REST_AUTH_USERNAME_VALUE = "rest-client.auth-username-value";
  public static final String REST_AUTH_TOKEN_KEY = "rest-client.auth-token-key";
  public static final String REST_AUTH_TOKEN_VALUE = "rest-client.auth-token-value";

}

