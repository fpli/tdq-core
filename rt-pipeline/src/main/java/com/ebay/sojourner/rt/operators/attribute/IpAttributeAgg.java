package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.business.ubd.detectors.IpSignatureBotDetector;
import com.ebay.sojourner.business.ubd.indicators.IpIndicators;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import java.io.IOException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class IpAttributeAgg
    implements AggregateFunction<AgentIpAttribute, IpAttributeAccumulator, IpAttributeAccumulator> {

  @Override
  public IpAttributeAccumulator createAccumulator() {

    IpAttributeAccumulator ipAttributeAccumulator = new IpAttributeAccumulator();

    try {
      IpIndicators.getInstance().start(ipAttributeAccumulator);
    } catch (Exception e) {
      log.error("init ip indicators failed", e);
    }

    return ipAttributeAccumulator;
  }

  @Override
  public IpAttributeAccumulator add(
      AgentIpAttribute agentIpAttribute, IpAttributeAccumulator ipAttributeAccumulator) {
    if (ipAttributeAccumulator.getIpAttribute().getClientIp() == null) {
      ipAttributeAccumulator.getIpAttribute().setClientIp(agentIpAttribute.getClientIp());
    }
    try {
      IpIndicators.getInstance().feed(agentIpAttribute, ipAttributeAccumulator);
    } catch (Exception e) {
      log.error("start ip indicators collection failed", e);
    }

    Set<Integer> ipBotFlag = null;
    try {
      if (ipAttributeAccumulator.getBotFlagStatus().containsValue(0)
          || ipAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
        ipBotFlag = IpSignatureBotDetector.getInstance()
            .getBotFlagList(ipAttributeAccumulator.getIpAttribute());
        if (ipBotFlag.contains(7)) {
          switch (ipAttributeAccumulator.getBotFlagStatus().get(7)) {
            case 0:
              ipAttributeAccumulator.getBotFlagStatus().put(7, 1);
              break;
            case 1:
              ipAttributeAccumulator.getBotFlagStatus().put(7, 2);
              break;
          }
        } else if (ipBotFlag.contains(222)) {
          switch (ipAttributeAccumulator.getBotFlagStatus().get(222)) {
            case 0:
              ipAttributeAccumulator.getBotFlagStatus().put(222, 1);
              break;
            case 1:
              ipAttributeAccumulator.getBotFlagStatus().put(222, 2);
              break;
          }
        } else if (ipBotFlag.contains(223)) {
          switch (ipAttributeAccumulator.getBotFlagStatus().get(223)) {
            case 0:
              ipAttributeAccumulator.getBotFlagStatus().put(223, 1);
              break;
            case 1:
              ipAttributeAccumulator.getBotFlagStatus().put(223, 2);
              break;
          }
        }
      }
    } catch (IOException | InterruptedException e) {
      log.error("start get ip botFlagList failed", e);
    }

    Set<Integer> botFlagList = ipAttributeAccumulator.getIpAttribute().getBotFlagList();
    if (ipBotFlag != null && ipBotFlag.size() > 0) {
      botFlagList.addAll(ipBotFlag);
    }

    ipAttributeAccumulator.getIpAttribute().setBotFlagList(botFlagList);

    return ipAttributeAccumulator;
  }

  @Override
  public IpAttributeAccumulator getResult(IpAttributeAccumulator ipAttr) {
    return ipAttr;
  }

  @Override
  public IpAttributeAccumulator merge(IpAttributeAccumulator a, IpAttributeAccumulator b) {
    return null;
  }
}