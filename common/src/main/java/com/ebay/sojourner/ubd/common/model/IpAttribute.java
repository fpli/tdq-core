package com.ebay.sojourner.ubd.common.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;
import org.apache.datasketches.hll.HllSketch;

@Data
public class IpAttribute implements Attribute<AgentIpAttribute>, Serializable {

  public static final int MAX_CGUID_THRESHOLD = 5;
  public static final int SESSION_COUNT_THRESHOLD = 300;
  public static final Set<Integer> pageSessionSet = new HashSet<>(Arrays.asList(2, 3, 4, 5));
  private Integer clientIp;
  private int scsCount = 0;
  private Set<Integer> botFlagList = new LinkedHashSet<>();

  // for suspected IP
  private int totalCnt = 0;
  private Boolean isAllAgentHoper = true;
  private int totalCntForSec1 = 0;

  public IpAttribute() {
  }

  @Override
  public void feed(AgentIpAttribute agentIpAttribute, int botFlag) {
    switch (botFlag) {
      case 7: {
        if (scsCount < 0) {
          return;
        }
        if (agentIpAttribute.getScsCountForBot7() < 0) {
          scsCount = -1;
        } else {
          scsCount += agentIpAttribute.getScsCountForBot7();
        }
        break;
      }
      case 210: {
        totalCnt += agentIpAttribute.getTotalCnt();
        if (selectRatio(agentIpAttribute)) {
          totalCntForSec1 += agentIpAttribute.getTotalCnt();
        }
        isAllAgentHoper = isAllAgentHoper && agentIpAttribute.getIsAllAgentHoper();
        break;
      }
    }
  }

  @Override
  public void revert(AgentIpAttribute agentIpAttribute, int botFlag) {
  }

  @Override
  public void clear() {
    clientIp = null;
    scsCount = 0;
    totalCnt = 0;
    isAllAgentHoper = true;
    totalCntForSec1 = 0;
  }

  @Override
  public void clear(int botFlag) {
    clientIp = null;
    scsCount = 0;
  }

  private boolean selectRatio(AgentIpAttribute agentIpAttribute) {
    int sessionCnt = agentIpAttribute.getTotalCnt();

    if (sessionCnt > 10
        && agentIpAttribute.isConsistent()
        && pageSessionSet.contains(agentIpAttribute.getValidPageCnt())) {
      return true;
    }

    if (sessionCnt > 3 && sessionCnt == agentIpAttribute.getHomePageCnt()) {
      return true;
    }

    if (sessionCnt > 5) {
      if (sessionCnt == agentIpAttribute.getFamilyViCnt()
          || sessionCnt == agentIpAttribute.getSigninCnt()) {
        return true;
      }
    }

    int cguidCnt = agentIpAttribute.getCguidSet().size();
    int guidCnt = agentIpAttribute.getHllSketch() == null ? 0 :
        (int) Math.round(HllSketch.heapify(agentIpAttribute.getHllSketch()).getEstimate());
    if (sessionCnt > 10
        && sessionCnt == agentIpAttribute.getNewGuidCnt()
        && (cguidCnt < 3 || agentIpAttribute.getMaxValidPageCnt() < 10)) {
      return true;
    }

    if (sessionCnt > 20
        && sessionCnt == agentIpAttribute.getMktgCnt()
        && (guidCnt == 1 || sessionCnt == guidCnt)) {
      return true;
    }

    if (sessionCnt > 50
        && (sessionCnt == agentIpAttribute.getNoUidCnt() || agentIpAttribute.getNoUidCnt() == 0)
        && (agentIpAttribute.getSiteCnt() * 1.0) < (0.1 * sessionCnt)) {
      return true;
    }

    if (sessionCnt > 100
        && (((agentIpAttribute.getFamilyViCnt() * 1.0) >= (0.95 * sessionCnt))
        || ((guidCnt * 1.0) >= (0.98 * sessionCnt)))) {
      return true;
    }

    return sessionCnt > 200
        && (((cguidCnt < MAX_CGUID_THRESHOLD)
        || (sessionCnt > 1000 && agentIpAttribute.getMaxValidPageCnt() <= 10))
        || ((agentIpAttribute.getNewGuidCnt() * 1.0) > (0.97 * sessionCnt)));

  }
}
