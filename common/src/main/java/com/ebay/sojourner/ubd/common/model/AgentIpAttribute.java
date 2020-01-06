package com.ebay.sojourner.ubd.common.model;

import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class AgentIpAttribute implements Attribute<UbiSession>, Serializable {
    private String clientIp;
    private String agent;
    private int scsCountForBot5 = 0;
    private int scsCountForBot6 = 0;
    private int scsCountForBot7 = 0;
    private int scsCountForBot8 = 0;
    private int ipCount = 0;
    private int bbcCount = 0;

    //for suspected agent
    private int totalSessionCnt = 0;
    private int nocguidSessionCnt = 0;
    private int spsSessionCnt = 0;
    private int nouidSessionCnt = 0;
    private int directSessionCnt = 0;
    private int mktgSessionCnt = 0;
    private int ipCountForSuspect = 0;

    //for suspected IP
    private int totalCnt = 0;
    private int validPageCnt = -1;
    private int maxValidPageCnt = -1;
    private boolean consistent = true;
    private int homePageCnt = 0;
    private int familyViCnt = 0;
    private int signinCnt = 0;
    private int noUidCnt = 0;
    private int directCnt = 0;
    private int mktgCnt = 0;
    private int siteCnt = 0;
    private int newGuidCnt = 0;
//    private int guidCnt = 0;
    private  Set<String> cguidSet = new HashSet<String>();
    private  Set<String> guidSet = new HashSet<String>();
    private Boolean isAllAgentHoper = true;
    private int totalCntForSec1 = 0;

    public AgentIpAttribute() {
    }

    @Override
    public void feed( UbiSession ubiSession, int botFlag, boolean isNeeded ) {
        if (isNeeded) {
            totalSessionCnt += 1;
            if (ubiSession.getFirstCguid() == null) {
                nocguidSessionCnt += 1;
            }

            if (UbiSessionHelper.isSps(ubiSession)) {
                spsSessionCnt += 1;
            }

            if (UbiSessionHelper.isNoUid(ubiSession)) {
                nouidSessionCnt += 1;
            }

            if (UbiSessionHelper.isDirect(ubiSession)) {
                directSessionCnt += 1;
            }
            if (UbiSessionHelper.isMktg(ubiSession)) {
                mktgSessionCnt += 1;
            }

            if (UbiSessionHelper.getExInternalIp(ubiSession) != null) {
                ipCountForSuspect = 1;
            }

            if (UbiSessionHelper.getExInternalIp(ubiSession) != null) {
                ipCount = 1;
            }
            totalCnt += 1;
            consistent = consistent
                    && (validPageCnt == ubiSession.getValidPageCnt()
                    || validPageCnt < 0 || ubiSession.getValidPageCnt() < 0);

            if (ubiSession.getValidPageCnt() >= 0) {
                validPageCnt = ubiSession.getValidPageCnt();
            }
            maxValidPageCnt = Math.max(maxValidPageCnt,
                    ubiSession.getValidPageCnt());

            if (UbiSessionHelper.isHomePage(ubiSession)) {
                homePageCnt += 1;
            }
            if (UbiSessionHelper.isFamilyVi(ubiSession)) {
                familyViCnt += 1;
            }
            if (UbiSessionHelper.isSignIn(ubiSession)) {
                signinCnt += 1;
            }
            if (UbiSessionHelper.isNoUid(ubiSession)) {
                noUidCnt += 1;
            }
            if (UbiSessionHelper.isDirect(ubiSession)) {
                directCnt += 1;
            }
            if (UbiSessionHelper.isMktg(ubiSession)) {
                mktgCnt += 1;
            }
            if (UbiSessionHelper.isSite(ubiSession)) {
                siteCnt += 1;
            }
            if (UbiSessionHelper.isNewGuid(ubiSession)) {
                newGuidCnt += 1;
            }
            if (ubiSession.getGuid() != null) {
                guidSet.add(ubiSession.getGuid());
            }

            if (ubiSession.getFirstCguid() != null) {
                cguidSet.add(ubiSession.getFirstCguid());
            }
            isAllAgentHoper = isAllAgentHoper && UbiSessionHelper.isAgentHoper(ubiSession);

        }
        switch (botFlag) {
            case 5:
                scsCountForBot5 += 1;
                break;
            case 6:
                scsCountForBot6 += 1;
                break;
            case 7:
                scsCountForBot7 += 1;
                break;
            case 8:
                scsCountForBot8 += 1;
                break;
            default:
                break;
        }
    }


    @Override
    public void revert(UbiSession ubiSession, int botFlag) {
        switch (botFlag) {
            case 5:
                scsCountForBot5 = -1;
                break;
            case 6:
                scsCountForBot6 = -1;
                break;
            case 7:
                scsCountForBot7 = -1;
                break;
            case 8:
                scsCountForBot8 = -1;

                break;
            default:
                break;
        }
    }

    @Override
    public void clear() {
        scsCountForBot5 = 0;
        scsCountForBot6 = 0;
        scsCountForBot7 = 0;
        scsCountForBot8 = 0;
        ipCount = 0;
        bbcCount = 0;
        totalSessionCnt = 0;
        nocguidSessionCnt = 0;
        spsSessionCnt = 0;
        nouidSessionCnt = 0;
        directSessionCnt = 0;
        mktgSessionCnt = 0;
        ipCountForSuspect = 0;

        validPageCnt = -1;
        maxValidPageCnt = -1;
        consistent = true;
        homePageCnt = 0;
        familyViCnt = 0;
        signinCnt = 0;
        noUidCnt = 0;
        directCnt = 0;
        mktgCnt = 0;
        siteCnt = 0;
        newGuidCnt = 0;
//        guidCnt = 0;
        cguidSet.clear();
        guidSet.clear();
        isAllAgentHoper = true;
        totalCntForSec1 = 0;
    }

    @Override
    public void clear(int botFlag) {
        switch (botFlag) {
            case 5:
                scsCountForBot5 = 0;
                break;
            case 6:
                scsCountForBot6 = 0;
                ipCount = 0;
                break;
            case 7:
                scsCountForBot7 = 0;
                break;
            case 8:
                scsCountForBot8 = 0;
                bbcCount = 0;
                break;
            case 202:
                totalSessionCnt = 0;
                nocguidSessionCnt = 0;
                spsSessionCnt = 0;
                nouidSessionCnt = 0;
                directSessionCnt = 0;
                mktgSessionCnt = 0;
                ipCountForSuspect = 0;
            default:
                break;
        }
    }
}
