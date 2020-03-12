package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class SojSession implements Serializable, Cloneable {

  private Long sojDataDt;
  private String guid;
  private String sessionId;
  private Long sessionStartDt;
  private String ip;
  private String userAgent;
  private String sessionReferrer;
  private int botFlag;
  private int version;
  private String firstUserId;
  private Long siteFlags;
  private int attrFlags;
  private int botFlags;
  private Long findingFlags;
  private int startPageId;
  private int endPageId;
  private Long startTimestamp;
  private int durationSec;
  private int eventCnt;
  private int viCoreCnt;
  private int bidCoreCnt;
  private int binCoreCnt;
  private int watchCoreCnt;
  private int trafficSrcId;
  private Long absStartTimestamp;
  private Long absEndTimestamp;
  private int absDuration;
  private int cobrand;
  private int firstSiteId;
  private String firstCguid;
  private Long firstMappedUserId;
  private Integer firstAppId;
  private Long endTimestamp;
  private int homepageCnt;
  private int grCnt;
  private int gr1Cnt;
  private int myebayCnt;
  private int signinPageCnt;
  private int nonIframeRdtEventCnt;
  private Set<Integer> botFlagList = new LinkedHashSet<>();

  public SojSession() {
    //        this.distinctClickIdSet = new HashSet<Integer>();
    //        this.agentSets= new HashSet<String>();
  }
}
