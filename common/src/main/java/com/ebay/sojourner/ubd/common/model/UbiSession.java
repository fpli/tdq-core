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
  private long sessionSkey;
  private Long sessionStartDt;
  private String ip; //ipv4 in jetstream
  private String userAgent;
  private String sessionReferrer;//referer in jetstream
  private int botFlag;
  private int version;
  private String firstUserId; // userid in jetstream
  private long siteFlags;
  private int attrFlags;
  private int botFlags;
  private Long findingFlags;
  private int startPageId; //pageid in jetstream
  private int endPageId; //pageid in jetstream
  private Long startTimestamp;
  private Long startTimestampNOIFRAMERDT;
  private Long endTimestampNOIFRAMERDT;
  private Long startTimestampNOIFRAME;
  private Long startTimestampForAgentString;
  private Long startTimestampForReferrer;
  private Long startTimestampForScEvent;
  private Long absStartTimestampForRoverClick;
  private Long absStartTimestampForRover3084;
  private Long absStartTimestampForRover3085;
  private Long absStartTimestampForRover3962;
  private Long absStartTimestampForNotifyClick;
  private Long absStartTimestampForNotifyView;
  private int durationSec;
  private int eventCnt; // sojeventcnt in jetstream
  private int viCoreCnt; // vicnt in jetstream
  private int bidCoreCnt; // bidcnt in Jetstream
  private int binCoreCnt; // bincnt in jetstream
  private int watchCoreCnt; // watchcnt in jetstream
  private int trafficSrcId;
  private Long absStartTimestamp;
  private int absDuration;
  private int cobrand;
  private int firstSiteId; // siteid in jetstream
  private String firstCguid; //cguid in jetstream
  private Long firstMappedUserId;
  private Integer firstAppId; // appid in jetstream
  private Long endTimestamp;
  private int homepageCnt;
  private int grCnt;
  private int gr1Cnt;
  private int myebayCnt;
  private int signinPageCnt;
  private int nonIframeRdtEventCnt;
  private Boolean singleClickSessionFlag; // Not exists in SojSession
  private Boolean bidBinConfirmFlag;
  private Boolean sessionEndedFlag;
  private String oldSessionSkey;
  private int absEventCnt; // Not exists in SojSession
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
  //  private boolean isFirstSessionStartDt;
  private int maxScsSeqNum;
  private int firstCobrand;
  private int minSCSeqNum;
  private Long[] minMaxEventTimestamp;
  private Set<Long> oldSessionSkeySet;
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
  private Long firstSessionStartDt;
  //Column exists in Jetstream but not exists in Flink
  private int asqCnt;//select * from SOJEvent(_pgf = 'ASQ' and rdt = 0  and _ifrm = false)
  private int atcCnt;//select * from SOJEvent(_pgf = 'ATC' and rdt = 0  and _ifrm = false)
  private int atlCnt;//select * from SOJEvent(_pgf = 'ATL' and rdt = 0  and _ifrm = false)
  private int boCnt;//select * from SOJEvent(_pgf = 'BO' and rdt = 0  and _ifrm = false)
  private int srpCnt;//select * from SOJEvent(_pgf in ('GR', 'GR-1') and rdt = 0  and _ifrm = false)
  private int servEventCnt;//select * from SOJEvent(p is not null and rdt = 0 and _ifrm = false)
  private int searchViewPageCnt;
  private String browserFamily;
  private String browserVersion;
  private String city;
  private String continent;
  private String country;
  private String deviceClass;//first
  private String deviceFamily;//first
  private int endResourceId;
  private boolean isReturningVisitor;
  private String lineSpeed;
  private String osFamily;
  private String osVersion;
  private int pulsarEventCnt;
  private String region;
  private long sessionEndDt;
  private int startResourceId;
  private String streamId;
  private String buserId;

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
    this.asqCnt += ubiSession.getAsqCnt();
    this.atcCnt += ubiSession.getAtcCnt();
    this.atlCnt += ubiSession.getAtlCnt();
    this.boCnt += ubiSession.getBoCnt();
    this.srpCnt += ubiSession.getSrpCnt();
    this.searchViewPageCnt += ubiSession.getSearchViewPageCnt();
    this.servEventCnt += ubiSession.getServEventCnt();
    this.maxScsSeqNum = Math.max(this.maxScsSeqNum, ubiSession.getMaxScsSeqNum());
    this.oldSessionSkeySet.addAll(ubiSession.getOldSessionSkeySet());
    this.botFlagList.addAll(ubiSession.getBotFlagList());
    this.userIdSet.addAll(ubiSession.getUserIdSet());
    if (this.getFirstSessionStartDt() > ubiSession.getFirstSessionStartDt()) {
      this.setFirstSessionStartDt(ubiSession.getFirstSessionStartDt());
    }
    if (this.getStartTimestamp() == null && ubiSession.getStartTimestamp() != null) {
      this.setStartTimestamp(ubiSession.getStartTimestamp());
    } else if (ubiSession.getStartTimestamp() != null && this.getStartTimestamp() > ubiSession
        .getStartTimestamp()) {
      this.setStartTimestamp(ubiSession.getStartTimestamp());
    }
    if (this.getEndTimestamp() == null && ubiSession.getEndTimestamp() != null) {
      this.setEndTimestamp(ubiSession.getEndTimestamp());
    } else if (ubiSession.getEndTimestamp() != null && this.getEndTimestamp() < ubiSession
        .getEndTimestamp()) {
      this.setEndTimestamp(ubiSession.getEndTimestamp());
    }
    if (this.getAbsStartTimestamp() == null && ubiSession.getAbsStartTimestamp() != null) {
      this.setAbsStartTimestamp(ubiSession.getAbsStartTimestamp());
    } else if (ubiSession.getAbsStartTimestamp() != null && this.getAbsStartTimestamp() > ubiSession
        .getAbsStartTimestamp()) {
      this.setAbsStartTimestamp(ubiSession.getAbsStartTimestamp());
    }
    if (this.getAbsEndTimestamp() == null && ubiSession.getAbsEndTimestamp() != null) {
      this.setAbsEndTimestamp(ubiSession.getAbsEndTimestamp());
    } else if (ubiSession.getAbsEndTimestamp() != null && this.getAbsEndTimestamp() < ubiSession
        .getAbsEndTimestamp()) {
      this.setAbsEndTimestamp(ubiSession.getAbsEndTimestamp());
    }

    return this;
  }

  public void initIntermediateMetrics() {
    intermediateMetrics = new IntermediateMetrics();
  }
}
