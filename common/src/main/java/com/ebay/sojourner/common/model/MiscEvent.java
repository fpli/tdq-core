package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MiscEvent {

  private long eventCreateTimestamp;
  private String guid;
  private long eventTimestamp;
  private long eventCaptureTime;
  private String requestCorrelationId;
  private String cguid;
  private String sid;
  private int pageId;
  private String pageName;
  private String pageFamily;
  private String eventFamily;
  private String eventAction;
  private String userId;
  private String clickId;
  private String siteId;
  private String sessionId;
  private String seqNum;
  private String ciid;
  private String siid;
  private int rdt;
  private int regu;
  private Boolean iframe;
  private String refererHash;
  private String sqr;
  private String itemId;
  private String flags;
  private String urlQueryString;
  private String webServer;
  private String cookies;
  private int bot;
  private String clientIP;
  private String remoteIP;
  private String agentInfo;
  private String appId;
  private String appVersion;
  private String osVersion;
  private String trafficSource;
  private String cobrand;
  private String deviceFamily;
  private String deviceType;
  private String browserVersion;
  private String browserFamily;
  private String osFamily;
  private String enrichedOsVersion;
  private String applicationPayload;
  private String rlogid;
  private String clientData;

}
