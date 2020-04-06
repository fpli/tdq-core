package com.ebay.sojourner.ubd.rt.util;

public class HdfsPathConstants {

  public static final String EVENT_PATH =
      AppEnv.config().getHdfs().getSinkPath() + "events/";
  public static final String SESSION_PATH =
      AppEnv.config().getHdfs().getSinkPath() + "sessions/";
  public static final String SIGNATURE_PATH =
      AppEnv.config().getHdfs().getSinkPath() + "signatures/";
  public static final String LATE_EVENT_PATH =
      AppEnv.config().getHdfs().getSinkPath() + "late_events/";
}
