package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JetStreamOutputSession implements Serializable {

  private String guid;
  private Long eventCreateTimestamp; // for generate watermark
  private String sessionId;
  private String streamId;
  private Long absStartTimestamp;
  private Long absDuration;
  private Long sessionStartDt;
  private Long sessionEndDt;
  private String ipv4;
  private String userAgent;
  private String referer;
  private Integer botFlag;
  private Integer startResourceId;
  private Integer endResourceId;
  private Integer pageId;
  private Integer viCnt;
  private Integer bidCnt;
  private Integer binCnt;
  private Integer watchCnt;
  private Integer homepageCnt;
  private Integer boCnt;
  private Integer srpCnt;
  private Integer asqCnt;
  private Integer atcCnt;
  private Integer atlCnt;
  private Integer absEventCnt;
  private Integer eventCnt;
  private Integer pulsarEventCnt;
  private Integer sojEventCnt;
  private Integer servEventCnt;
  private Integer viewEventCnt;
  private Integer searchViewPageCnt;
  private Integer trafficSrcId;
  private String lineSpeed;
  private Boolean isReturningVisitor;
  private Integer appId;
  private String browserVersion;
  private String browserFamily;
  private String osVersion;
  private String osFamily;
  private String deviceClass;
  private String deviceFamily;
  private String city;
  private String region;
  private String country;
  private String continent;
  private Boolean singleClickSessionFlag;
  private Integer cobrand;
  private String siteId;
  private String cguid;
  private String userId;
  private String buserId;

}
