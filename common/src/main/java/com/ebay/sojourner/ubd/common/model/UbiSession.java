package com.ebay.sojourner.ubd.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class UbiSession implements Serializable{
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
    private Integer eventCnt;
    private Integer viCoreCnt;
    private Integer bidCoreCnt;
    private Integer binCoreCnt;
    private Integer watchCoreCnt;
    private Integer trafficSrcId;
    private Long absStartTimestamp;
    private Integer absDuration;
    private Integer cobrand;
    private Integer firstSiteId;
    private String firstCguid;
    private Long firstMappedUserId;
    private Integer firstAppId;
    private Long endTimestamp;
    private Integer homepageCnt;
    private Integer grCnt;
    private Integer gr1Cnt;
    private Integer myebayCnt;
    private Integer signinPageCnt;
    private Integer nonIframeRdtEventCnt;
    private Boolean singleClickSessionFlag;
    private Boolean bidBinConfirmFlag;
    private Boolean sessionEndedFlag;
    private String oldSessionSkey;
    private Integer absEventCnt;
    private Integer validPageCnt;
    private Integer agentCnt;
    private String agentString;
    private Integer lndgPageId;
    private String exInternalIp;
    private Integer familyViCnt;
    private Set<Integer> distinctClickIdSet;
    private Set<String> agentSets;
    private String agentInfo;
    private String clientIp;
    private boolean findFirst ;
    private String internalIp;
    private String externalIp;
    private String externalIp2;
    private Long[] minMaxEventTimestamp;
    private List<Integer> botFlagList;
    public UbiSession()
    {
        this.distinctClickIdSet = new HashSet<Integer>();
        this.agentSets= new HashSet<String>();
    }
}
