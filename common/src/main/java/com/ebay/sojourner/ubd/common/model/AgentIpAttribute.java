package com.ebay.sojourner.ubd.common.model;

import com.ebay.sojourner.ubd.common.util.TransformUtil;
import com.ebay.sojourner.ubd.common.util.UbiSessionHelper;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class AgentIpAttribute implements Attribute<IntermediateSession>, Serializable {

  private String clientIp;
  private String agent;
  private Set<Integer> botFlagList = new LinkedHashSet<>();
  private int scsCountForBot5 = 0;
  private int scsCountForBot6 = 0;
  private int scsCountForBot7 = 0;
  private int scsCountForBot8 = 0;
  private int ipCount = 0;
  private int bbcCount = 0;

  // for suspected agent
  private int totalSessionCnt = 0;
  private int nocguidSessionCnt = 0;
  private int spsSessionCnt = 0;
  private int nouidSessionCnt = 0;
  private int directSessionCnt = 0;
  private int mktgSessionCnt = 0;
  private int ipCountForSuspect = 0;

  // for suspected IP
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
  private Set<String> cguidSet = new HashSet<String>();
  private Set<Guid> guidSet = new HashSet<Guid>();
  private Boolean isAllAgentHoper = true;
  private int totalCntForSec1 = 0;

  public AgentIpAttribute() {
  }

  @Override
  public void feed(IntermediateSession intermediateSession, int botFlag, boolean isNeeded) {
    if (isNeeded) {
      totalSessionCnt += 1;
      if (intermediateSession.getFirstCguid() == null) {
        nocguidSessionCnt += 1;
      }

      if (UbiSessionHelper.isSps(intermediateSession)) {
        spsSessionCnt += 1;
      }

      if (UbiSessionHelper.isNoUid(intermediateSession)) {
        nouidSessionCnt += 1;
      }

      if (UbiSessionHelper.isDirect(intermediateSession)) {
        directSessionCnt += 1;
      }
      if (UbiSessionHelper.isMktg(intermediateSession)) {
        mktgSessionCnt += 1;
      }

      if (UbiSessionHelper.getExInternalIp(intermediateSession) != null) {
        ipCountForSuspect = 1;
      }

      if (UbiSessionHelper.getExInternalIp(intermediateSession) != null) {
        ipCount = 1;
      }
      totalCnt += 1;
      consistent =
          consistent
              && (validPageCnt == intermediateSession.getValidPageCnt()
              || validPageCnt < 0
              || intermediateSession.getValidPageCnt() < 0);

      if (intermediateSession.getValidPageCnt() >= 0) {
        validPageCnt = intermediateSession.getValidPageCnt();
      }
      maxValidPageCnt = Math.max(maxValidPageCnt, intermediateSession.getValidPageCnt());

      if (UbiSessionHelper.isHomePage(intermediateSession)) {
        homePageCnt += 1;
      }
      if (UbiSessionHelper.isFamilyVi(intermediateSession)) {
        familyViCnt += 1;
      }
      if (UbiSessionHelper.isSignIn(intermediateSession)) {
        signinCnt += 1;
      }
      if (UbiSessionHelper.isNoUid(intermediateSession)) {
        noUidCnt += 1;
      }
      if (UbiSessionHelper.isDirect(intermediateSession)) {
        directCnt += 1;
      }
      if (UbiSessionHelper.isMktg(intermediateSession)) {
        mktgCnt += 1;
      }
      if (UbiSessionHelper.isSite(intermediateSession)) {
        siteCnt += 1;
      }
      if (UbiSessionHelper.isNewGuid(intermediateSession)) {
        newGuidCnt += 1;
      }
      if (intermediateSession.getGuid() != null) {
        Long[] long4Cguid = TransformUtil.md522Long(intermediateSession.getGuid());
        Guid guid = new Guid();
        guid.setGuid1(long4Cguid[0]);
        guid.setGuid2(long4Cguid[1]);
        guidSet.add(guid);
      }

      if (intermediateSession.getFirstCguid() != null) {
        if (cguidSet.size() <= 5) {
          cguidSet.add(intermediateSession.getFirstCguid());
        }
      }
      isAllAgentHoper = isAllAgentHoper && UbiSessionHelper.isAgentHoper(intermediateSession);
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

  public void merge(AgentIpAttribute agentIpAttribute, int botFlag) {

    switch (botFlag) {
      case 5:
        scsCountForBot5 += agentIpAttribute.getScsCountForBot5();
        break;
      case 8:
        scsCountForBot8 += agentIpAttribute.getScsCountForBot8();
        break;
      default:
        break;
    }
  }

  @Override
  public void revert(IntermediateSession intermediateSession, int botFlag) {
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
    totalCnt = 0;
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
