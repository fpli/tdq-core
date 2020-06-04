package com.ebay.sojourner.flink.common.util;

import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;

public class HdfsPathConstants {

  public static final String PARENT_PATH = FlinkEnvUtils.getString(Constants.HDFS_PATH_PARENT);
  public static final String EVENT_NON_BOT_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_EVENT_NON_BOT);
  public static final String EVENT_BOT_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_EVENT_BOT);
  public static final String SESSION_NON_BOT_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_SESSION_NON_BOT);
  public static final String SESSION_BOT_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_SESSION_BOT);
  public static final String SIGNATURE_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_CROSS_SESSION);
  public static final String LATE_EVENT_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_EVENT_LATE);
  public static final String JETSTREAM_EVENT_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_JETSTREAM_EVENT);
  public static final String JETSTREAM_SESSION_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_JETSTREAM_SESSION);
  public static final String INTERMEDIATE_SESSION_PATH =
      PARENT_PATH + FlinkEnvUtils.getString(Constants.HDFS_PATH_INTERMEDIATE_SESSION);
}
