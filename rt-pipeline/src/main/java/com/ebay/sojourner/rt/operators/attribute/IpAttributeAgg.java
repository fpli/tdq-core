package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.business.ubd.detectors.IpSignatureBotDetector;
import com.ebay.sojourner.business.ubd.indicators.IpIndicators;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.model.SignatureInfo;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class IpAttributeAgg implements
    AggregateFunction<AgentIpAttribute, IpAttributeAccumulator, IpAttributeAccumulator> {

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

    IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();

    if (ipAttributeAccumulator.getIpAttribute().getClientIp() == null) {
      ipAttributeAccumulator.getIpAttribute().setClientIp(agentIpAttribute.getClientIp());
    }

    try {
      IpIndicators.getInstance().feed(agentIpAttribute, ipAttributeAccumulator);
    } catch (Exception e) {
      log.error("feed ip indicators collection failed", e);
    }

    Set<Integer> ipBotFlag = null;
    Map<Integer, SignatureInfo> signatureStatus = ipAttributeAccumulator.getSignatureStatus();

    try {
      //      if (signatureStatus.containsValue(0) || signatureStatus.containsValue(1)) {
      ipBotFlag = IpSignatureBotDetector.getInstance().getBotFlagList(ipAttribute);
      //        if (CollectionUtils.isNotEmpty(ipBotFlag)) {
      SignatureUtils.updateSignatureStatus(signatureStatus, ipBotFlag);
      //        }
      //      }
    } catch (Exception e) {
      log.error("start get ip botFlagList failed", e);
    }

    Set<Integer> botFlagList = SignatureUtils.setBotFlags(ipBotFlag, ipAttribute.getBotFlagList());
    ipAttribute.setBotFlagList(botFlagList);
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
