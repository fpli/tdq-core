package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.PropertyUtils;
import com.ebay.sojourner.common.util.UBIConfig;
import java.util.Set;

public class BotRule208 extends AbstractBotRule<UbiSession> {

  private Set<Integer> cobrandSet;

  @Override
  public void init() {
    cobrandSet =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.EBAY_SITE_COBRAND), Property.PROPERTY_DELIMITER);
  }

  @Override
  public int getBotFlag(UbiSession session) {
    int cobrand = session.getCobrand() == Integer.MIN_VALUE ? session.getFirstCobrand() :
        session.getCobrand();
    if (session.getAgentString() == null
        && session.getSiidCnt() == 0
        && !cobrandSet.contains(cobrand)) {
      return BotRules.DIRECT_ACCESS_BOTFLAG;
    }
    return 0;
  }
}
