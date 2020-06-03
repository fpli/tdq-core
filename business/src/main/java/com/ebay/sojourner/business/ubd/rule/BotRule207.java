package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.common.util.PropertyUtils;
import com.ebay.sojourner.common.util.UBIConfig;
import java.util.Set;

public class BotRule207 extends AbstractBotRule<UbiSession> {

  private Set<Integer> cobrandSets;

  @Override
  public void init() {
    cobrandSets =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.EBAY_SITE_COBRAND), Property.PROPERTY_DELIMITER);
  }

  @Override
  public int getBotFlag(UbiSession session) {
    int cobrand = session.getCobrand()==Integer.MIN_VALUE?session.getFirstCobrand():
        session.getCobrand();
    if (session.getValidPageCnt() > 5
        && session.getSiidCnt2() <= 1
        && session.isRefererNull()
        && !cobrandSets.contains(cobrand)) {
      return BotRules.MANY_VALID_EVENTS_WHITHOUT_REFERER;
    }
    return 0;
  }
}
