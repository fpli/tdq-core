/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class SojEvent implements Serializable {

  private String guid;
  private String sessionId;
  private long sessionSkey;
  private int seqNum;
  private String sessionStartDt;
  private String sojDataDt;
  private int clickId;
  private int siteId = -1;
  private int version;
  private int pageId = -1;
  private String pageName;
  private Long refererHash; // String in jetstream
  private String eventTimestamp;
  private String urlQueryString;
  private String clientData;
  private String cookies;
  private String applicationPayload;
  private String webServer;
  private String referrer;
  private String userId;
  private Long itemId;
  private String flags;
  private int rdt; //int in jetstream
  private int regu;
  private String sqr;
  private int staticPageType;
  private int reservedForFuture;
  private String eventAttr;
  private Long currentImprId;//ciid in jetstream
  private Long sourceImprId;//siid in jetstream
  private int cobrand;
  private int iframe;
  private String agentInfo;
  private String forwardedFor;
  private String clientIP;
  private Integer appId;
  private Long oldSessionSkey;
  private boolean partialValidPage;
  private long sessionStartTime;
  private long sessionEndTime;
  private Set<Integer> botFlags = new LinkedHashSet<>(); // bot in jetstream int
  private long icfBinary;
  // collect some metrics for monitor and validation
  private long ingestTime;
  private long generateTime;
  private long eventCnt;
  //metric for monitor
  private String dataCenter;
  //new columns from jetstream
  private String sid;
  private long eventCaptureTime;//didn't find logic
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
