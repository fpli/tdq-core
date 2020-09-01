package com.ebay.sojourner.common.util;

import java.util.TimeZone;

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

  // flink metrics
  public static final String SOJ_METRICS_GROUP = "sojourner_ubd";

  // flied delimiter
  public static final String FIELD_DELIM = "\007";

  // signatures suffix
  public static final String GENERATION_PREFFIX = "_g";
  public static final String EXPIRATION_PREFFIX = "_e";
  public static final String AGENT = "Agent";
  public static final String AGENTIP = "AgentIp";
  public static final String IP = "Ip";
  public static final String GUID = "Guid";

  //Time format/ Time Zone Constants
  public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
  public static final String DEFAULT_DATE_MINS_FORMAT = "yyyy-MM-dd HH:mm";
  public static final String EBAY_TIMEZONE = "GMT-7";
  // the offset align with UTC-
  public static final long OFFSET = 2208963600000000L; // 25567L *24 * 3600 * 1000 * 1000 - 7 *
  // 3600 * 1000 * 1000;
  public static final long MILSECOFDAY = 86400000000L; // 24 * 3600 * 1000 * 1000
  public static final int MILLI2MICRO = 1000;

  public static final long UPPERLIMITMICRO = 1 * 60 * 1000000L; // 2 minutes
  public static final long LOWERLIMITMICRO = -30 * 60 * 1000000L; // 31 minutes
  public static final TimeZone UTC_TIMEZONE= TimeZone.getTimeZone("UTC");
  public static final TimeZone PST_TIMEZONE = TimeZone.getTimeZone("GMT-7");
  public static final long MINUS_GUID_MIN_MS = 180000L; // 417mins - 7hours = -3mins = -180000ms;
  // UNIX.
  public static final long PLUS_GUID_MAX_MS = 300000L; // 425mins - 7hours = 5mins = 300000ms;

  // TAG
  public static final String P_TAG = "p";
  public static final String TAG_ITEMIDS = "!itemIds";
  public static final String TAG_TRKP = "trkp";
  public static final String TAG_MTSTS="mtsts";

}

