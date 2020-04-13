package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.Set;

public class BotFilter implements ValueFilter<UbiSession, Integer> {

  Set<Integer> invalidSessionBotFilter = null;

  public BotFilter(UBIConfig ubiConfig) {
    invalidSessionBotFilter =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.INVALID_BOT_FILTER), Property.PROPERTY_DELIMITER);
  }

  @Override
  public boolean filter(UbiSession session, Integer botFlag) throws InterruptedException {
    Set<String> appIdWithBotFlags=LkpManager.getInstance().getAppIds();
    Integer appId = session.getFirstAppId();
    if (botFlag != null && appId != null) {
      StringBuilder appIdOnBotFlag = new StringBuilder();
      appIdOnBotFlag
          .append(appId.toString().trim())
          .append(LkpManager.LKP_FILED_DELIMITER)
          .append(botFlag.toString().trim());
      System.out.print("###QQQ###" + appIdOnBotFlag.toString());
      System.out.print("###SSS###" + appIdWithBotFlags);
      if (appIdWithBotFlags.contains(appIdOnBotFlag.toString())) {
        return true;
      }
    }

    return session.getNonIframeRdtEventCnt() == 0 && invalidSessionBotFilter.contains(botFlag);

  }

  @Override
  public void cleanup() {
    LkpManager.getInstance().clearAppId();
  }

}
