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
  public static final String MAX_POLL_RECORDS = "kafka.consumer.max-poll-records";
  public static final String RECEIVE_BUFFER = "kafka.consumer.receive-buffer";
  public static final String FETCH_MAX_BYTES = "kafka.consumer.fetch-max-bytes";
  public static final String FETCH_MAX_WAIT_MS = "kafka.consumer.fetch-max-wait-ms";
  public static final String MAX_PARTITIONS_FETCH_BYTES = "kafka.consumer.max-partitions-fetch-bytes";
  public static final String AUTO_RESET_OFFSET = "kafka.consumer.auto-offset-reset";

  public static final String KAFKA_CONSUMER_BOOTSTRAP_SERVERS = "kafka.consumer.bootstrap-servers";
  public static final String KAFKA_CONSUMER_TOPIC = "kafka.consumer.topic";
  public static final String KAFKA_CONSUMER_GROUP_ID = "kafka.consumer.group-id";
  public static final String KAFKA_CONSUMER_DATA_CENTER = "kafka.consumer.data-center";

  // kafka producer
  public static final String BATCH_SIZE = "kafka.producer.batch-size";
  public static final String REQUEST_TIMEOUT_MS = "kafka.producer.request-timeout-ms";
  public static final String REQUEST_RETRIES = "kafka.producer.retries";
  public static final String LINGER_MS = "kafka.producer.linger-ms";
  public static final String BUFFER_MEMORY = "kafka.producer.buffer-memory";
  public static final String ACKS = "kafka.producer.acks";
  public static final String COMPRESSION_TYPE = "kafka.producer.compression-type";
  public static final String BEHAVIOR_MESSAGE_KEY_SESSION = "kafka.producer.message-key.session";
  public static final String BEHAVIOR_MESSAGE_KEY_EVENT_DEFAULT = "kafka.producer.message-key.event.default";
  public static final String BEHAVIOR_MESSAGE_KEY_EVENT_KEY1 = "kafka.producer.message-key.event.key1";
  public static final String BEHAVIOR_MESSAGE_KEY_EVENT_KEY2 = "kafka.producer.message-key.event.key2";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_IP = "kafka.producer.message-key.signature.ip";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_GUID = "kafka.producer.message-key.signature.guid";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_AGENT = "kafka.producer.message-key.signature.agent";
  public static final String BEHAVIOR_MESSAGE_KEY_SIGNATURE_AGENT_IP = "kafka.producer.message-key.signature.agent-ip";
  public static final String KAFKA_PRODUCER_BOOTSTRAP_SERVERS = "kafka.producer.bootstrap-servers";
  public static final String KAFKA_PRODUCER_TOPIC = "kafka.producer.topic";
  public static final String KAFKA_PRODUCER_SUBJECT_SOJEVENT = "kafka.producer.subject.event";
  public static final String KAFKA_PRODUCER_SUBJECT_SOJSESSION = "kafka.producer.subject.session";
  public static final String PRODUCER_ID = "kafka.producer.producerId";
  // rheos
  public static final String RHEOS_KAFKA_REGISTRY_URL = "rheos.registry-url";
  public static final String RHEOS_CLIENT_ID = "rheos.client.id";
  public static final String RHEOS_CLIENT_IAF_SECRET = "rheos.client.iaf.secret";
  public static final String RHEOS_CLIENT_IAF_ENV = "rheos.client.iaf.env";
  public static final String RHEOS_SCHEMA_NAME_EVENT = "rheos.schema-name.event";
  public static final String RHEOS_SCHEMA_NAME_SESSION = "rheos.schema-name.session";
  public static final String RHEOS_SCHEMA_NAME_ATTRIBUTE = "rheos.schema-name.attribute";

  // flink checkpoint
  public static final String CHECKPOINT_DATA_DIR = "flink.app.checkpoint.data-dir";
  public static final String CHECKPOINT_INTERVAL_MS = "flink.app.checkpoint.interval-ms";
  public static final String CHECKPOINT_TIMEOUT_MS = "flink.app.checkpoint.timeout-ms";
  public static final String CHECKPOINT_MIN_PAUSE_BETWEEN_MS = "flink.app.checkpoint.min-pause-between-ms";
  public static final String CHECKPOINT_MAX_CONCURRENT = "flink.app.checkpoint.max-concurrent";
  public static final String TOLERATE_FAILURE_CHECKPOINT_NUMBER = "flink.app.checkpoint.tolerate-failure-number";

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
  public static final String SOURCE_EVENT_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.source-event";
  public static final String SOURCE_EVENT_LVS_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.source-event-lvs";
  public static final String SOURCE_EVENT_SLC_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.source-event-slc";
  public static final String SOURCE_EVENT_RNO_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.source-event-rno";
  public static final String SESSION_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.session";
  public static final String CROSS_SESSION_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.cross-session";
  public static final String BROADCAST_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.broadcast";
  public static final String PRE_AGENT_IP_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.pre-agent-ip";
  public static final String AGENT_IP_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.agent-ip";
  public static final String AGENT_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.agent";
  public static final String IP_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.ip";
  public static final String GUID_SLOT_SHARE_GROUP = "flink.app.slot-sharing-group.guid";

  // flink - app name
  public static final String FLINK_APP_NAME = "flink.app.name";

  // flink - metric name
  public static final String FLINK_APP_METRIC_NAME = "flink.app.metric.watermark-process-progress";

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

  // ------------------------- batch pipeline common property ---------------------------
  // parallelism config
  public static final String SINK_HDFS_PARALLELISM = "flink.app.parallelism.sink.hdfs";
  public static final String SINK_KAFKA_PARALLELISM = "flink.app.parallelism.sink.kafka";

  // hdfs
  public static final String HDFS_DUMP_PATH = "hdfs.dump.path";
  public static final String HDFS_SAME_DAY_SESSION_DUMP_PATH = "hdfs.dump.path.same-day";
  public static final String HDFS_CROSS_DAY_SESSION_DUMP_PATH = "hdfs.dump.path.cross-day";
  public static final String HDFS_OPEN_SESSION_DUMP_PATH = "hdfs.dump.path.open";
  public static final String HDFS_DUMP_CLASS = "hdfs.dump.class-name";
  public static final String HDFS_DUMP_WATERMARK_PATH = "hdfs.dump.watermark-path";

  // ------------------------- rt pipeline property ------------------------
  // kafka producer
  public static final String KAFKA_TOPIC_SESSION_BOT = "kafka.producer.topic.session.bot";
  public static final String KAFKA_TOPIC_SESSION_NON_BOT = "kafka.producer.topic.session.non-bot";
  public static final String KAFKA_TOPIC_INTERMEDIATE_SESSION = "kafka.producer.topic.intermediate-session";
  public static final String KAFKA_TOPIC_EVENT_BOT = "kafka.producer.topic.event.bot";
  public static final String KAFKA_TOPIC_EVENT_NON_BOT = "kafka.producer.topic.event.non-bot";
  public static final String KAFKA_TOPIC_EVENT_LATE = "kafka.producer.topic.event.late";
  public static final String KAFKA_TOPIC_SIGNATURE_AGENT_IP = "kafka.producer.topic.signature.agent-ip";
  public static final String KAFKA_TOPIC_SIGNATURE_AGENT = "kafka.producer.topic.signature.agent";
  public static final String KAFKA_TOPIC_SIGNATURE_IP = "kafka.producer.topic.signature.ip";
  public static final String KAFKA_TOPIC_SIGNATURE_GUID = "kafka.producer.topic.signature.guid";

  // hdfs
  public static final String HDFS_PATH_PARENT = "hdfs.path.parent";
  public static final String HDFS_PATH_EVENT_NON_BOT = "hdfs.path.event.non-bot";
  public static final String HDFS_PATH_SESSION_NON_BOT = "hdfs.path.session.non-bot";
  public static final String HDFS_PATH_INTERMEDIATE_SESSION = "hdfs.path.intermediate-session";
  public static final String HDFS_PATH_SIGNATURES = "hdfs.path.signatures";

  // data skew
  public static final String IS_FILTER = "flink.app.data-skew.is-filter";
  public static final String FILTER_GUID_LIST = "flink.app.data-skew.guid-list";

}
