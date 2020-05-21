package com.ebay.sojourner.ubd.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JetStreamOutputEvent {

  private String guid;
  private long eventTimestamp;
  private long eventCreateTimestamp; // for generate watermark
  private String sid;
  private long eventCaptureTime; // didn't find logic
  private String requestCorrelationId;
  private String pageFamily; // need to lookup and diff from batch ignore currently
  private String remoteIP;
  private String appVersion;
  private String eventFamily;
  private String eventAction;
  private String trafficSource;
  private String osVersion;
  private String deviceFamily;
  private String deviceType;
  private String browserVersion;
  private String browserFamily;
  private String osFamily;
  private String enrichedOsVersion;
  private String rlogid;
}
