package com.ebay.sojourner.ubd.common.rule;

import static com.ebay.sojourner.ubd.common.util.BotRules.NON_BOT_FLAG;
import static com.ebay.sojourner.ubd.common.util.BotRules.SCS_ON_IP;

import com.ebay.sojourner.ubd.common.model.IpAttribute;

public class BotRule7 extends AbstractBotRule<IpAttribute> {

  @Override
  public int getBotFlag(IpAttribute ipAttribute) {
    int botFlag = NON_BOT_FLAG;
    if (ipAttribute.getScsCount() >= 1 && ipAttribute.getTotalCnt() >= 20) {
      botFlag = SCS_ON_IP;
    }
    return botFlag;
  }
}
