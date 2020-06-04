package com.ebay.sojourner.common.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class AgentAttribute implements Attribute<AgentIpAttribute>, Serializable {

  private AgentHash agent;
  private int scsCount;
  private int ipCount;
  private int totalSessionCnt = 0;
  private int nocguidSessionCnt = 0;
  private int spsSessionCnt = 0;
  private int nouidSessionCnt = 0;
  private int directSessionCnt = 0;
  private int mktgSessionCnt = 0;
  private int ipCountForSuspect = 0;
  private Set<Integer> botFlagList = new LinkedHashSet<>();

  public AgentAttribute() {
  }

  @Override
  public void feed(AgentIpAttribute agentIpAttribute, int botFlag) {
    switch (botFlag) {
      case 6: {
        ipCount += agentIpAttribute.getIpCount();
        if (scsCount < 0) {
          return;
        }

        if (agentIpAttribute.getScsCountForBot6() < 0) {
          scsCount = -1;
        } else {
          scsCount += agentIpAttribute.getScsCountForBot6();
        }
        break;
      }
      case 202: {
        totalSessionCnt += agentIpAttribute.getTotalSessionCnt();
        nocguidSessionCnt += agentIpAttribute.getNocguidSessionCnt();
        spsSessionCnt += agentIpAttribute.getSpsSessionCnt();
        nouidSessionCnt += agentIpAttribute.getNouidSessionCnt();
        directSessionCnt += agentIpAttribute.getDirectSessionCnt();
        mktgSessionCnt += agentIpAttribute.getMktgSessionCnt();
        ipCountForSuspect += agentIpAttribute.getIpCountForSuspect();
        break;
      }
    }
  }

  @Override
  public void revert(AgentIpAttribute agentIpAttribute, int botFlag) {
  }

  @Override
  public void clear() {
    agent = null;
    scsCount = 0;
    ipCount = 0;
    totalSessionCnt = 0;
    nocguidSessionCnt = 0;
    spsSessionCnt = 0;
    nouidSessionCnt = 0;
    directSessionCnt = 0;
    mktgSessionCnt = 0;
    ipCountForSuspect = 0;
  }

  @Override
  public void clear(int botFlag) {
    agent = null;
    scsCount = 0;
    ipCount = 0;
  }

  //    public static void main(String[] args) {
  //        AgentAttribute agentAttribute = new AgentAttribute();
  //        agentAttribute.getAgent();
  //        agentAttribute.setAgent("sss");
  //    }
}