package com.ebay.sojourner.flink.common.util;

public class Constants {

  /*
    kafka topic for customized
   */
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SESSION_BOT = "kafka.stream.behavior-total-new.topic.session.bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SESSION_NON_BOT = "kafka.stream.behavior-total-new.topic.session.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_EVENT_BOT = "kafka.stream.behavior-total-new.topic.event.bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_EVENT_NON_BOT = "kafka.stream.behavior-total-new.topic.event.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_AGENT_IP = "kafka.stream.behavior-total-new.topic.signature.agent-ip";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_AGENT = "kafka.stream.behavior-total-new.topic.signature.agent";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_IP = "kafka.stream.behavior-total-new.topic.signature.ip";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_GUID = "kafka.stream.behavior-total-new.topic.signature.guid";

  //TODO: will delete after dataquality
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_DQ_SESSION = "kafka.stream.behavior-total-new.topic.dq.session";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_DQ_CROSS_SESSION = "kafka.stream.behavior-total-new.topic.dq.cross-session";

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
    flink config
    1. checkpoint config
    2. metrics group config
   */

  // checkpoint config
  public static final String CHECKPOINT_DATA_DIR = "flink.app.checkpoint.data-dir";
  public static final String CHECKPOINT_INTERVAL_MS = "flink.app.checkpoint.interval-ms";
  public static final String CHECKPOINT_TIMEOUT_MS = "flink.app.checkpoint.timeout-ms";
  public static final String CHECKPOINT_MIN_PAUSE_BETWEEN_MS = "flink.app.checkpoint.min-pause-between-ms";
  public static final String CHECKPOINT_MAX_CONCURRENT = "flink.app.checkpoint.max-concurrent";

  // metrics config
  public static final String SOJ_METRICS_GROUP = "sojourner_ubd";

  /*
    data center name
   */
  public static final String LVS = "data-center.lvs";
  public static final String SLC = "data-center.slc";
  public static final String RNO = "data-center.rno";

  // separator
  public static final String SEPARATOR = "/007";

}
