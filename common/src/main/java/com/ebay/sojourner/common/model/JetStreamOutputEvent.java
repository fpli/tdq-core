package com.ebay.sojourner.common.model;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JetStreamOutputEvent implements Serializable {

  private String guid;
  private Long eventTimestamp;
  private Long eventCreateTimestamp; // for generate watermark
  private String sid;
  private Long eventCaptureTime; // didn't find logic
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
  private String sessionId;
  private String cguid;
  private Integer pageId;
  private String pageName;
  private String userId;
  private String clickId;
  private String siteId;
  private String seqNum;
  private String ciid;
  private String siid;
  private Integer rdt;
  private Integer regu;
  private Boolean iframe;
  private String refererHash;
  private String sqr;
  private String itemId;
  private String flags;
  private String urlQueryString;
  private String webServer;
  private String cookies;
  private Integer bot;
  private String clientIP;
  private String agentInfo;
  private String appId;
  private String cobrand;
  private Map<String, String> applicationPayload;
  private Map<String, String> clientData;

}
