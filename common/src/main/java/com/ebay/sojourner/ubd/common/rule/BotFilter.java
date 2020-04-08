package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpListener;
import com.ebay.sojourner.ubd.common.util.LkpEnum;
import com.ebay.sojourner.ubd.common.util.LkpManager;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import java.util.Set;

public class BotFilter implements ValueFilter<UbiSession, Integer>, LkpListener {

  Set<Integer> invalidSessionBotFilter = null;
  Set<String> appIdWithBotFlags;
  LkpManager lkpFetcher;
  private boolean isContinue;

  public BotFilter(UBIConfig ubiConfig) {
    lkpFetcher = new LkpManager(this, LkpEnum.appid);
    appIdWithBotFlags = lkpFetcher.getAppIds();
    invalidSessionBotFilter =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.INVALID_BOT_FILTER), Property.PROPERTY_DELIMITER);
    isContinue = true;
  }

  @Override
  public boolean filter(UbiSession session, Integer botFlag) throws InterruptedException {
    while(!isContinue){
      Thread.sleep(10);
    }
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
    lkpFetcher.clearAppId();
  }

  @Override
  public boolean notifyLkpChange(LkpManager lkpManager) {

    try {
      this.isContinue = false;
      appIdWithBotFlags = lkpManager.getAppIds();
      return true;
    } catch (Throwable e) {
      return false;
    } finally {
      this.isContinue = true;
    }
  }
}
