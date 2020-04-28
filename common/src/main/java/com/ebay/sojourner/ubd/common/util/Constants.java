package com.ebay.sojourner.ubd.common.util;

import java.io.File;

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
  public static final String DATE_PARTITION =
      new StringBuilder()
          .append("yyyy")
          .append(File.pathSeparator)
          .append("MM")
          .append(File.pathSeparator)
          .append("dd")
          .append(File.pathSeparator)
          .toString();
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
  public static final String ZK_NODE_PATH = "/rule/publish/notification";

  // rule management restful api
  public static final String REST_SERVER = "https://sojubdportalservice.vip.qa.ebay.com";
  public static final String API_RULE_LIST_PUBLISHED = "/api/rule/list/published";
  public static final String API_SPECIFIED_RULE_PREFIX = "/api/rule/";

}
