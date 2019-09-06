package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

public class BotRule207 implements Rule<UbiSession> {
  private static Set<Integer> cobrandSets;
  private static UBIConfig ubiConfig;

  @Override
  public void init() {
    InputStream resourceAsStream = BotRule207.class.getResourceAsStream("/ubi.properties");
    ubiConfig = UBIConfig.getInstance(resourceAsStream);
    cobrandSets = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.EBAY_SITE_COBRAND), Property.PROPERTY_DELIMITER);
  }

  @Override
  public int getBotFlag(UbiSession session) {
      if (session.getValidPageCnt() > 5 && session.getSiidCnt2() <= 1 && session.isRefererNull() && !cobrandSets.contains(session.getCobrand())) {
        return BotRules.MANY_VALID_EVENTS_WHITHOUT_REFERER;
      }
      return 0;
    }
}
