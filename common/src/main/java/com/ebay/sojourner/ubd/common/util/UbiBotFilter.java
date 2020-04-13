package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author weifang.
 */
public class UbiBotFilter implements BotFilter {

  public static final String COUNTER_FILTERED_APPID = "filteredByAppId";
  public static final String COUNTER_FILTERED_INVALID = "filteredByInvalidSession";

  private final Set<Integer> invalidSessionBotFilter;
  private Set<String> appIdWithBotFlags;

  public UbiBotFilter() {
    appIdWithBotFlags = LkpManager.getInstance().getAppIds();
    invalidSessionBotFilter =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.INVALID_BOT_FILTER), Property.PROPERTY_DELIMITER);
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
    appIdWithBotFlags = LkpManager.getInstance().getAppIds();
    Integer appId = ubiSession.getFirstAppId();
    if (targetFlag != null && appId != null) {
      StringBuilder appIdOnBotFlag = new StringBuilder();
      appIdOnBotFlag
          .append(appId.toString().trim())
          .append(LkpManager.LKP_FILED_DELIMITER)
          .append(targetFlag.toString().trim());
      if (appIdWithBotFlags.contains(appIdOnBotFlag.toString())) {
        return true;
      }
    }

    return UbiSessionHelper.isNonIframRdtCountZero(ubiSession)
        && invalidSessionBotFilter.contains(targetFlag);
  }

}
