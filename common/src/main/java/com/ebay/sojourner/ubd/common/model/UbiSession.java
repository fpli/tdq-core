package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class UbiSession implements Serializable,Cloneable{
    private Long sojDataDt;
    private String guid;
    private String sessionId;
    private Long sessionStartDt;
    private String ip;
    private String userAgent;
    private String sessionReferrer;
    private Integer botFlag;
    private Integer version;
    private String firstUserId;
    private Long siteFlags;
    private Integer attrFlags;
    private Integer botFlags;
    private Long findingFlags;
    private Integer startPageId;
    private Integer endPageId;
    private Long startTimestamp;
    private Integer durationSec;
    private int eventCnt ;
    private int viCoreCnt;
    private int bidCoreCnt;
    private int binCoreCnt;
    private int watchCoreCnt;
    private Integer trafficSrcId;
    private Long absStartTimestamp;
    private Long absEndTimestamp;
    private Integer absDuration;
    private Integer cobrand;
    private Integer firstSiteId;
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
    private Integer lndgPageId;
    private String exInternalIp;
    private int familyViCnt;
    private Set<Integer> distinctClickIdSet;
    private Set<String> agentSets;
    private String agentInfo;
    private String clientIp;
    private boolean findFirst ;
    private String internalIp;
    private String externalIp;
    private String externalIp2;
    private Long[] minMaxEventTimestamp;
    private Set<Integer> botFlagList = new LinkedHashSet<Integer>();
    public UbiSession()
    {
        this.distinctClickIdSet = new HashSet<Integer>();
        this.agentSets= new HashSet<String>();
    }
}
