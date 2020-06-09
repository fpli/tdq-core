package com.ebay.sojourner.common.util;

public class Property {

  // --------------------- UBI Property File -------------------------------------
  // Session properties
  public static final String SESSION_IDLE_TIMEOUT = "session.idleTimeOut";
  public static final String SESSION_MAX_DURATION = "session.maxDuration";
  // Event properties
  public static final String EVENT_WAITING_PERIOD = "event.waitingPeriod";
  public static final String EVENT_POOL_THRESHOLD = "event.pool.threshold";
  public static final String EVENT_DELAY_THRESHOLD = "event.delay.threshold";
  // Database properties
  public static final String DB_DRIVER = "db.driver";
  public static final String DB_URL = "db.url";
  public static final String DB_USER = "db.user";
  public static final String DB_PWD = "db.pwd";

  // MapReduce properties
  public static final String MAP_OUTPUT_COMPRESS_CODEC = "mapred.map.output.compression.codec";
  public static final String OUTPUT_COMPRESSION_TYPE = "mapred.output.compression.type";
  public static final String OUTPUT_COMPRESSION_CODEC = "mapred.output.compression.codec";

  // -------------------- Lookup Property File ----------------------------------
  // Lookup table properties
  public static final String IFRAME_PAGE_IDS = "iframe.page.ids";
  public static final String IFRAME_PAGE_IDS4Bot12 = "iframe.page.ids.bot12";
  public static final String FINDING_FLAGS = "finding.flags";
  public static final String VTNEW_IDS = "vtNewIds";
  public static final String IAB_AGENT = "iab.agent";
  public static final String APP_ID = "appid";
  public static final String PAGE_FMLY = "pagefmly";
  public static final String TEST_USER_IDS = "test.user.ids";
  public static final String LARGE_SESSION_GUID = "large.session.guid";
  public static final String MPX_ROTATION = "mpx.rotation";

  // -------------------- Mapred Context Property --------------------------------
  public static final String CONTEXT_EVENT_KEY_SCHEMA = "event.key.schema";
  public static final String CONTEXT_UBI_EVENT_SCHEMA = "ubi.event.schema";
  public static final String CONTEXT_SESSION_KEY_SCHEMA = "session.key.schema";
  public static final String CONTEXT_EVENT_GROUP_KEY_SCHEMA = "event.group.key.schema";
  public static final String CONTEXT_AGENT_GROUP_KEY_SCHEMA = "agent.group.key.schema";
  public static final String CONTEXT_IP_GROUP_KEY_SCHEMA = "ip.group.key.schema";
  public static final String CONTEXT_AGENT_IP_GROUP_KEY_SCHEMA = "agent.ip.group.key.schema";
  public static final String CONTEXT_AGENT_IP_SHUFFLE_KEY_SCHEMA = "agent.ip.shuffle.key.schema";
  public static final String CONTEXT_GUID_GROUP_KEY_SCHEMA = "guid.group.key.schema";
  public static final String CONTEXT_AGENT_STRING_KEY_SCHEMA = "agent.string.key.schema";
  // Intraday Event Output
  public static final String INTRADAY_LOAD_END_TIMESTAMP = "load.end.timestamp";
  public static final String INTRADAY_INTERMEDIATE_EVENT_OUT = "intermediate.event.out";
  public static final String INTRADAY_SKEW_EVENT_OUT = "skew.event.out";
  public static final String INTRADAY_NOSKEW_EVENT_OUT = "noskew.event.out";

  // EOD Session Update Output Part
  public static final String EOD_LOAD_END_TIMESTAMP = "eod.load.end.timestamp";
  public static final String EOD_END_SESSION_OUT = "end.session.out";
  public static final String EOD_END_SKEW_EVENT_OUT = "end.skew.event.out";
  public static final String EOD_END_NON_SKEW_EVENT_OUT = "end.noskew.event.out";
  public static final String EOD_OPEN_SESSION_OUT = "open.session.out";
  public static final String EOD_OPEN_SKEW_EVENT_OUT = "open.skew.event.out";
  public static final String EOD_OPEN_NOSKEW_EVENT_OUT = "open.noskew.event.out";
  public static final String EOD_UPDATE_SESSION_OUT = "update.session.out";
  public static final String EOD_PCT1_BASE_OUT = "eod.pct1.base.out";
  public static final String EOD_PCT1_SKEW_SKIP = "eod.pct1.skew.skip";
  public static final String EOD_UOW = "eod.uow";

  public static final String ENABLE_GUIDXUID_PCT1 = "enable.guidxuid.pct1";
  public static final String ENABLE_SESSXUID_PCT1 = "enable.sessionxuid.pct1";
  public static final String ENABLE_EVENTXUID_PCT1 = "enable.eventxuid.pct1";
  public static final String ENABLE_XUID_PCT1 = "enable.xuid.pct1";

  public static final String ENABLE_CONTAINER_PCT1 = "enable.container.pct1";
  public static final String ENABLE_BOT_CONTAINER = "enable.bot.container";
  public static final String CONTAINER_MAX_EVENTS = "container.max.events";
  public static final String BOT_CONTAINER_MAX_EVENTS = "bot.container.max.events";

  // --------------------- EOD Bot Detection Property -----------------------------
  public static final String CONTEXT_EOD_BOT_ENABLE_BUFFER = "eod.bot.enable.buffer";
  public static final String CONTEXT_EOD_BOT_BUFFER_SIZE = "eod.bot.buffer.size";
  public static final String CONTEXT_EOD_BOT_FILE_SIZE = "eod.bot.file.size";
  // -------------------- Page Indicator Property --------------------------------
  public static final String SEARCH_VIEW_PAGES = "search.view.pages";
  public static final String VIEW_ITEM_PAGES = "view.item.pages";
  public static final String BID_PAGES = "bid.pages";
  public static final String BIN_PAGES = "bin.pages";
  public static final String CAPTCHA_PAGES = "captcha.pages";
  public static final String PROPERTY_DELIMITER = ",";
  public static final String HALF_PAGES = "half.pages";
  public static final String CORESITE_PAGES = "coresite.pages";
  public static final String CLASSIFIED_PAGES = "classified.pages";
  public static final String SCEVENT_EXCLUDE_PAGES1 = "scevent.exclude.pages1";
  public static final String SCEVENT_EXCLUDE_PAGES2 = "scevent.exclude.pages2";
  public static final String ROVER_PAGES = "rover.pages";
  public static final String LAND_PAGES1 = "land.pages1";
  public static final String LAND_PAGES2 = "land.pages2";
  public static final String AGENT_EXCLUDE_PAGES = "agent.exclude.pages";
  public static final String NOTIFY_CLICK_PAGES = "notify.click.pages";
  public static final String NOTIFY_VIEW_PAGES = "notify.view.pages";
  public static final String SOCIAL_AGENT_ID22 = "social.agent.ids22";
  public static final String SOCIAL_AGENT_ID23 = "social.agent.ids23";
  public static final String MOBILE_PAGES = "mobile.pages";
  // -------------------- New BOT Property --------------------------------
  public static final String INVALID_PAGE_IDS = "invalid.page.ids";
  public static final String LNDG_PAGE_IDS = "lndg.page.ids";
  public static final String EBAY_SITE_COBRAND = "ebay.site.cobrand";
  public static final String MKTG_TRAFFIC_SOURCE_IDS = "mktg.traffic.source.ids";
  public static final String BROWSER_AGENT_STRING = "browser.agent.string";
  public static final String BOT_AGENT_STRING = "bot.agent.string";
  public static final String IP_EXCLUDE_PAGES = "ip.exclude.pages";
  public static final String EXCLUDE_IP_PATTERN = "exclude.ip.pattern";
  public static final String EBAY_NONBROWSER_COBRAND = "ebay.nonbrower.cobrand";
  public static final String PAGE_SESSION_CLICKS = "page.session.clicks";
  public static final String INVALID_BOT_FILTER = "invalid.session.bot.filter";
  public static final String SELECTED_IPS = "selected.ips";
  public static final String SELECTED_AGENTS = "selected.agents";

  // --------------------- APP PAYLOAD KV Property ---------------------------------
  public static final String SWD_VALUES = "swd.values";
  public static final String ROT_VALUES = "rot.values";
  public static final String VI_EVENT_VALUES = "event.pgt.vi";
  public static final String PRELOAD_PAYLOAD_TAGS = "preload.payload.tags";

  // --------------------- APP ID Property -----------------------------------------
  public static final String MOBILE_APP = "mobile.appids";
  public static final String DESKTOP_APP = "desktop.appids";
  public static final String EIM_APP = "eim.appids";

  // -------------------- BOT Rule Property ---------------------------------------
  public static final String BOT_RULE_PACKAGE = "bot.rule.package";
  public static final String BOT_RULE_CLASSES = "bot.rule.classes";
  public static final String BOT_EOD_UPDATE = "bot.eod.update";
  public static final String BOT_DELIMITER = ",";

  // ----------------------------  Cobrand Property  ------------------------------
  public static final String EXPRESS_SITE = "express.site";
  public static final String HALF_SITE = "half.site";
  public static final String EXPRESS_PARTNER = "express.partner";
  public static final String SHOPPING_PARTNER = "shopping.partner";
  public static final String HALF_PARTNER = "half.partner";
  public static final String ARTISAN_PARTNER = "artisan.partner";
  public static final String MOBILE_AGENT_START = "mobile.agent.start.pattern";
  public static final String MOBILE_AGENT_INDEX = "mobile.agent.index.pattern";
  public static final String MOBILE_AGENT_OTHER = "mobile.agent.other.pattern";
  public static final String MOBILE_AGENT_DELIMITER = "#";
  public static final String START_IDENTIFIER = "^";
  public static final String LAST_UOW = "event_data.lastuow";
  // --------------------- Filter Name List Property ------------------------------
  public static final String DISABLED_FILTER_NAMES = "disabled.filter.names";

  // ---------------------- LOG Property -----------------------------------------
  public static final String LOG_LEVEL = "log.level";
  public static final String DEFAULT_LOG_LEVEL = "INFO";

  // ---------------------- Track and Monitor -----------------------------------------
  public static final String TASK_TRACK_PERIOD = "task.track.period";

  // ---------------------- LARGE_SESSION  -----------------------------------------
  public static final String LARGE_SESSION_EVENT_NUMBER = "event.number.threshold";
  public static final String LARGE_SESSION_TIMES_OF_BOT15 = "times.of.bot15";

  // ---------------------- Enable com.ebay.sojourner.ubd.common.util.Test
  // -----------------------------------------
  public static final String IS_TEST_ENABLE = "enable.test";

  public static final String LKP_PATH = "lkpPath";

  // --------------------- common config property ------------------------------
  // kafka consumer
  public static final String MAX_POLL_RECORDS = "kafka.common.consumer.max-poll-records";
  public static final String RECEIVE_BUFFER = "kafka.common.consumer.receive-buffer";
  public static final String FETCH_MAX_BYTES = "kafka.common.consumer.fetch-max-bytes";
  public static final String FETCH_MAX_WAIT_MS = "kafka.common.consumer.fetch-max-wait-ms";
  public static final String MAX_PARTITIONS_FETCH_BYTES = "kafka.common.consumer.max-partitions-fetch-bytes";
  public static final String AUTO_RESET_OFFSET = "kafka.common.consumer.auto-offset-reset";

  // kafka producer
  public static final String BATCH_SIZE = "kafka.common.producer.batch-size";
  public static final String REQUEST_TIMEOUT_MS = "kafka.common.producer.request-timeout-ms";
  public static final String BEHAVIOR_MESSAGE_KEY_SESSION = "kafka.common.producer.message-key.session";
  public static final String BEHAVIOR_MESSAGE_KEY_EVENT = "kafka.common.producer.message-key.event";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_IP = "kafka.common.producer.message-key.signature.ip";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_GUID = "kafka.common.producer.message-key.signature.guid";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_AGENT = "kafka.common.producer.message-key.signature.agent";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_AGENT_IP = "kafka.common.producer.message-key.signature.agent-ip";

  // rheos
  public static final String RHEOS_KAFKA_REGISTRY_URL = "rheos.serviceUrl";
  public static final String IAF_SECRET = "rheos.iaf.secret";
  public static final String IAF_ENV = "rheos.iaf.env";

  // flink checkpoint
  public static final String CHECKPOINT_DATA_DIR = "flink.app.checkpoint.data-dir";
  public static final String CHECKPOINT_INTERVAL_MS = "flink.app.checkpoint.interval-ms";
  public static final String CHECKPOINT_TIMEOUT_MS = "flink.app.checkpoint.timeout-ms";
  public static final String CHECKPOINT_MIN_PAUSE_BETWEEN_MS = "flink.app.checkpoint.min-pause-between-ms";
  public static final String CHECKPOINT_MAX_CONCURRENT = "flink.app.checkpoint.max-concurrent";

  // flink - parallelism and slot share group
  public static final String DEFAULT_PARALLELISM = "flink.app.parallelism.default";
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

  // flink - app name
  public static final String NAME_FULL_PIPELINE = "flink.app.name.full-pipeline";
  public static final String NAME_DATA_QUALITY = "flink.app.name.data-quality";
  public static final String NAME_HOT_DEPLOY = "flink.app.name.hot-deploy";
  public static final String NAME_HDFS_DUMP_PIPELINE = "flink.app.name.hdfs-dump";
  public static final String NAME_KAFKA_DUMP_PIPELINE = "flink.app.name.kafka-dump";

  // zookeeper
  public static final String ZOOKEEPER_SERVER = "zookeeper.server";
  public static final String ZOOKEEPER_SESSION_TIMEOUT_MS = "zookeeper.sessionTimeoutMs";
  public static final String ZOOKEEPER_CONNECTION_TIMEOUT_MS = "zookeeper.connectionTimeoutMs";
  public static final String ZOOKEEPER_MAX_RETRIES = "zookeeper.maxRetries";
  public static final String ZOOKEEPER_BASE_SLEEP_TIME_MS = "zookeeper.baseSleepTimeMs";
  public static final String ZOOKEEPER_NAMESPACE = "zookeeper.namespace";

  // rest client
  public static final String REST_SERVER = "rest-client.rest-server";
  public static final String REST_AUTH_USERNAME = "rest-client.auth-username";
  public static final String REST_AUTH_TOKEN = "rest-client.auth-token";

  // ------------------------- batch connector property -------------------------
  // kafka consumer
  public static final String KAFKA_COMMON_CONSUMER_BROKERS_DEFAULT = "kafka.common.consumer.brokers.default";
  public static final String KAFKA_COMMON_CONSUMER_TOPIC_DEFAULT = "kafka.common.consumer.topic.default";
  public static final String KAFKA_COMMON_CONSUMER_GROUP_ID_DEFAULT = "kafka.common.consumer.group-id.default";

  // kafka producer
  public static final String KAFKA_COMMON_PRODUCER_BROKERS_DEFAULT = "kafka.common.producer.brokers.default";
  public static final String KAFKA_COMMON_PRODUCER_TOPIC_DEFAULT = "kafka.common.producer.topic.default";

  // parallelism config
  public static final String SOURCE_DEFAULT_PARALLELISM = "flink.app.parallelism.source.default";
  public static final String SINK_HDFS_PARALLELISM = "flink.app.parallelism.sink.hdfs";
  public static final String SINK_KAFKA_PARALLELISM = "flink.app.parallelism.sink.kafka";

  // hdfs
  public static final String HDFS_DUMP_PATH = "hdfs.dump.path";
  public static final String HDFS_DUMP_CLASS = "hdfs.dump.class-name";

  // ------------------------- rt pipeline prod property ------------------------
  // stream - pathfinder
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_RNO = "kafka.stream.behavior-pathfinder.bootstrap-servers.rno";
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_SLC = "kafka.stream.behavior-pathfinder.bootstrap-servers.slc";
  public static final String BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_LVS = "kafka.stream.behavior-pathfinder.bootstrap-servers.lvs";
  public static final String BEHAVIOR_PATHFINDER_TOPIC = "kafka.stream.behavior-pathfinder.topic";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_RNO = "kafka.stream.behavior-pathfinder.group-id.default.rno";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_SLC = "kafka.stream.behavior-pathfinder.group-id.default.slc";
  public static final String BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_LVS = "kafka.stream.behavior-pathfinder.group-id.default.lvs";

  // stream - total-new
  public static final String BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT = "kafka.stream.behavior-total-new.bootstrap-servers.default";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SESSION_BOT = "kafka.stream.behavior-total-new.topic.session.bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SESSION_NON_BOT = "kafka.stream.behavior-total-new.topic.session.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_EVENT_BOT = "kafka.stream.behavior-total-new.topic.event.bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_EVENT_NON_BOT = "kafka.stream.behavior-total-new.topic.event.non-bot";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_AGENT_IP = "kafka.stream.behavior-total-new.topic.signature.agent-ip";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_AGENT = "kafka.stream.behavior-total-new.topic.signature.agent";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_IP = "kafka.stream.behavior-total-new.topic.signature.ip";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_SIGNATURE_GUID = "kafka.stream.behavior-total-new.topic.signature.guid";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_AGENT_IP = "kafka.stream.behavior-total-new.group-id.signature.agent-ip";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_AGENT = "kafka.stream.behavior-total-new.group-id.signature.agent";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_IP = "kafka.stream.behavior-total-new.group-id.signature.ip";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_SIGNATURE_GUID = "kafka.stream.behavior-total-new.group-id.signature.guid";

  // hdfs
  public static final String HDFS_PATH_PARENT = "hdfs.path.parent";
  public static final String HDFS_PATH_EVENT_NON_BOT = "hdfs.path.event.non-bot";
  public static final String HDFS_PATH_SESSION_NON_BOT = "hdfs.path.session.non-bot";
  public static final String HDFS_PATH_CROSS_SESSION = "hdfs.path.cross-session";
  public static final String HDFS_PATH_INTERMEDIATE_SESSION = "hdfs.path.intermediate-session";
  public static final String HDFS_PATH_SIGNATURES = "hdfs.path.signatures";

  // ---------------------------- rt pipeline dq property ----------------------------
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_DQ_SESSION = "kafka.stream.behavior-total-new.topic.dq.session";
  public static final String BEHAVIOR_TOTAL_NEW_TOPIC_DQ_CROSS_SESSION = "kafka.stream.behavior-total-new.topic.dq.cross-session";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_DQ_SESSION = "kafka.stream.behavior-total-new.group-id.dq.session";
  public static final String BEHAVIOR_TOTAL_NEW_GROUP_ID_DQ_CROSS_SESSION = "kafka.stream.behavior-total-new.group-id.dq.cross-session";

  // ---------------------------- rt pipeline qa property ----------------------------
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
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SESSION_BOT = "kafka.stream.behavior-trafficjam.group-id.session.bot";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_EVENT_BOT = "kafka.stream.behavior-trafficjam.group-id.event.bot";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_EVENT_NON_BOT = "kafka.stream.behavior-trafficjam.group-id.event.non-bot";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_AGENT_IP = "kafka.stream.behavior-trafficjam.group-id.signature.agent-ip";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_AGENT = "kafka.stream.behavior-trafficjam.group-id.signature.agent";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_IP = "kafka.stream.behavior-trafficjam.group-id.signature.ip";
  public static final String BEHAVIOR_TRAFFICJAM_GROUP_ID_SIGNATURE_GUID = "kafka.stream.behavior-trafficjam.group-id.signature.guid";

}
