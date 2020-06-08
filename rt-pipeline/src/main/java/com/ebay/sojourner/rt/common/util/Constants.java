package com.ebay.sojourner.rt.common.util;

public class Constants {

  /*
    kafka source and sink config
    1. source config
    2. sink config
   */

  // source config
  public static final String BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT = "kafka.stream.behavior-total-new.bootstrap-servers.default";
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_RNO = "kafka.stream.behavior-pathfinder.bootstrap-servers.rno";
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_SLC = "kafka.stream.behavior-pathfinder.bootstrap-servers.slc";
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_LVS = "kafka.stream.behavior-pathfinder.bootstrap-servers.lvs";
  public static final String BEHAVIOR_PATHFINDER_TOPIC = "kafka.stream.behavior-pathfinder.topic";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_RNO = "kafka.stream.behavior-pathfinder.group-id.default.rno";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_SLC = "kafka.stream.behavior-pathfinder.group-id.default.slc";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_LVS = "kafka.stream.behavior-pathfinder.group-id.default.lvs";

  // source & sink config
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
    1. parallelism and slot share group config
    2. app name config
   */

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

  // signatures config
  public static final String GENERATION_PREFFIX = "_g";
  public static final String EXPIRATION_PREFFIX = "_e";

  //TODO: will delete after dataquality
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

}
