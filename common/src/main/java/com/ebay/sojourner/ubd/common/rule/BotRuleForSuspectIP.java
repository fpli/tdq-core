package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.IpAttribute;

public class BotRuleForSuspectIP extends AbstractBotRule<IpAttribute> {
  public static final int SESSION_COUNT_THRESHOLD = 300;

  @Override
  public int getBotFlag(IpAttribute ipAttribute) {
    if (ipAttribute.getIsAllAgentHoper() && ipAttribute.getTotalCnt() > SESSION_COUNT_THRESHOLD) {
      return 223;
    }
    if (ipAttribute.getTotalCntForSec1() > SESSION_COUNT_THRESHOLD) {
      return 223;
    }
    return 0;
  }
}
