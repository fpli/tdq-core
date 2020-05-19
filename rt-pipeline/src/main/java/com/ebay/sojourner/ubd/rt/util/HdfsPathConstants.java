package com.ebay.sojourner.ubd.rt.util;

public class HdfsPathConstants {

  public static final String PARENT_PATH = AppEnv.config().getHdfs().getSinkParentPath();
  public static final String EVENT_PATH =
      PARENT_PATH + AppEnv.config().getHdfs().getSinkEventPath();
  public static final String SESSION_PATH =
      PARENT_PATH + AppEnv.config().getHdfs().getSinkSessionPath();
  public static final String SIGNATURE_PATH =
      PARENT_PATH + AppEnv.config().getHdfs().getSinkSignaturePath();
  public static final String LATE_EVENT_PATH =
      PARENT_PATH + AppEnv.config().getHdfs().getSinkLateEventPath();
  public static final String JETSTREAM_EVENT_PATH =
      PARENT_PATH + AppEnv.config().getHdfs().getSinkLateEventPath();
  public static final String JETSTREAM_SESSION_PATH =
      PARENT_PATH + AppEnv.config().getHdfs().getSinkLateEventPath();
}