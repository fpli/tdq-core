package com.ebay.sojourner.business.rule;

import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.util.BotHostMatcher;
import com.ebay.sojourner.common.util.TransformUtil;

public class BotRuleForDeclarativeHost extends AbstractBotRule<IpAttribute> {

  public static final int SESSION_COUNT_THRESHOLD = 300;

  private BotHostMatcher botHostMatcher = BotHostMatcher.INSTANCE;

  @Override
  public int getBotFlag(IpAttribute ipAttribute) {
    if ((ipAttribute.getIsAllAgentHoper() && ipAttribute.getTotalCnt() > SESSION_COUNT_THRESHOLD)
        || ipAttribute.getTotalCntForSec1() > SESSION_COUNT_THRESHOLD) {
      String ip = TransformUtil.int2Ip(ipAttribute.getClientIp());
      // comment for performace testing
      //      if (StringUtils.isNotBlank(ip) && botHostMatcher.isBotIp(ip)) {
      //        return 222;
      //      }
    }
    return 0;
  }
}
