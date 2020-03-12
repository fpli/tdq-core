package com.ebay.sojourner.ubd.common.model;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.IntermediateMetrics;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class UbiSession implements Serializable, Cloneable {

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
  private Boolean singleClickSessionFlag;
  private Boolean bidBinConfirmFlag;
  private Boolean sessionEndedFlag;
  private String oldSessionSkey;
  private int absEventCnt;
  private int validPageCnt;
  private int agentCnt;
  private String agentString;
  private int lndgPageId;
  private String exInternalIp;
  private int familyViCnt;

  // new added according to batch
  private Long absEndTimestamp;
  private int pageCnt;
  private int searchCnt;
  private int viewCnt;
  private boolean isRefererNull; // for bot207
  private int siidCnt2; // for bot207
  private int viCnt; // for bot215

  //interim columns
  private Set<Integer> distinctClickIdSet = new HashSet<>();
  private Set<String> agentSets = new HashSet<>();
  private String agentInfo;
  private String clientIp;
  private boolean findFirst;
  private String internalIp;
  private String externalIp;
  private String externalIp2;
  private Integer appId;
  private int siidCnt;
  private boolean isFirstSessionStartDt;
  private int maxScsSeqNum;

  private int firstCobrand;
  private int minSCSeqNum;
  private Long[] minMaxEventTimestamp;
  private Set<Long> oldSessionSkeySet;
  private byte[] siteFlagsSet;
  private Set<Integer> botFlagList = new LinkedHashSet<>();
  private Set<String> userIdSet = new HashSet<>();
  private Attributes attributes = new Attributes();
  private byte[] attributeFlags = {
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
  };
  private int seqNum;
  private IntermediateMetrics intermediateMetrics;

  public UbiSession() {
    //        this.distinctClickIdSet = new HashSet<Integer>();
    //        this.agentSets= new HashSet<String>();
  }

  public boolean isRefererNull() {
    return isRefererNull;
  }

  public void setIsRefererNull(boolean refererNull) {
    isRefererNull = refererNull;
  }

  public UbiSession merge(UbiSession ubiSession) {
    this.eventCnt += ubiSession.getEventCnt();
    this.viCnt += ubiSession.getViCnt();
    this.viCoreCnt += ubiSession.getViCoreCnt();
    this.bidCoreCnt += ubiSession.getBidCoreCnt();
    this.binCoreCnt += ubiSession.getBinCoreCnt();
    this.watchCoreCnt += ubiSession.getWatchCoreCnt();
    this.absEventCnt += ubiSession.getAbsEventCnt();
    this.homepageCnt += ubiSession.getHomepageCnt();
    this.gr1Cnt += ubiSession.getGr1Cnt();
    this.grCnt += ubiSession.getGrCnt();
    this.myebayCnt += ubiSession.getMyebayCnt();
    this.signinPageCnt += ubiSession.getSigninPageCnt();
    this.nonIframeRdtEventCnt += ubiSession.getNonIframeRdtEventCnt();
    this.validPageCnt += ubiSession.getValidPageCnt();
    this.agentCnt += ubiSession.getAgentCnt();
    this.familyViCnt += ubiSession.getFamilyViCnt();
    this.pageCnt += ubiSession.getPageCnt();
    this.searchCnt += ubiSession.getSearchCnt();
    this.viewCnt += ubiSession.getViewCnt();
    this.siidCnt += ubiSession.getSiidCnt();
    this.siidCnt2 += ubiSession.getSiidCnt2();
    this.maxScsSeqNum = Math.max(this.maxScsSeqNum, ubiSession.getMaxScsSeqNum());
    this.oldSessionSkeySet.addAll(ubiSession.getOldSessionSkeySet());
    this.botFlagList.addAll(ubiSession.getBotFlagList());
    this.userIdSet.addAll(ubiSession.getUserIdSet());

    return this;
  }
}
