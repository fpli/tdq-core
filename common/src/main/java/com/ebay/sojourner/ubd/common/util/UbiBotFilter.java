package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author weifang.
 */
public class UbiBotFilter implements BotFilter, LkpListener {

  public static final String COUNTER_FILTERED_APPID = "filteredByAppId";
  public static final String COUNTER_FILTERED_INVALID = "filteredByInvalidSession";

  private final Set<Integer> invalidSessionBotFilter;
  private  Map<Integer, Set<Integer>> appIdWithBotFlags;
  private volatile LkpManager lkpManager = new LkpManager(this,LkpEnum.appid);
  private boolean isContinue;
  public UbiBotFilter() {
    appIdWithBotFlags = getAppIdWithBotFlags(lkpManager.getAppIds());
    invalidSessionBotFilter =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.INVALID_BOT_FILTER), Property.PROPERTY_DELIMITER);
    isContinue=true;
  }

  public static Map<Integer, Set<Integer>> getAppIdWithBotFlags(Set<String> appIds) {
    Map<Integer, Set<Integer>> ret = new HashMap<>();
    for (String appidFlag : appIds) {
      String[] pair = appidFlag.split(",");
      int pageId = Integer.parseInt(pair[0]);
      Set<Integer> ids = ret.get(pageId);
      if (ids == null) {
        ids = new HashSet<>();
        ret.put(pageId, ids);
      }
      ids.add(Integer.parseInt(pair[1]));
    }
    return ret;
  }

  @Override
  public boolean filter(UbiSession ubiSession, Integer targetFlag) throws InterruptedException {
    while(!isContinue){
      Thread.sleep(10);
    }
    if (ubiSession.getAppId() != null) {
      Set<Integer> botFlags = appIdWithBotFlags.get(ubiSession.getAppId());
      return botFlags != null && botFlags.contains(targetFlag);
    }

    return UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
        && invalidSessionBotFilter.contains(targetFlag);
  }

  @Override
  public boolean notifyLkpChange(LkpManager lkpManager) {
    try {

      this.isContinue=false;
      appIdWithBotFlags = getAppIdWithBotFlags(lkpManager.getAppIds());
      return true;
    } catch (Throwable e) {
      return false;
    }
    finally {
      this.isContinue=true;
    }
  }
}
