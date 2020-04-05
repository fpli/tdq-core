package com.ebay.sojourner.ubd.rt.util;

public class HdfsPathConstants {

  public static final String EVENT_PATH =
      AppEnv.config().getHdfs().getNameNode() + "events/";
  public static final String SESSION_PATH =
      AppEnv.config().getHdfs().getNameNode() + "sessions/";
  public static final String SIGNATURE_PATH =
      AppEnv.config().getHdfs().getNameNode() + "signatures/";
  public static final String LATE_EVENT_PATH =
      AppEnv.config().getHdfs().getNameNode() + "late_events/";
}
