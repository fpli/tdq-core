package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UbiBotFilter implements BotFilter {

  public static final String COUNTER_FILTERED_APPID = "filteredByAppId";
  public static final String COUNTER_FILTERED_INVALID = "filteredByInvalidSession";

  private final Set<Integer> invalidSessionBotFilter;
  private Map<Integer, Set<Integer>>  appIdWithBotFlags;
  private Integer appId;

  public UbiBotFilter() {
    appIdWithBotFlags = getAppIdWithBotFlags(LkpManager.getInstance().getAppIds());
    invalidSessionBotFilter =
        PropertyUtils.getIntegerSet(
            UBIConfig.getString(Property.INVALID_BOT_FILTER), Property.PROPERTY_DELIMITER);
  }

  private static Map<Integer, Set<Integer>> getAppIdWithBotFlags(Set<String> appIds) {
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
  public boolean filter(Object session, Integer targetFlag) {

    if (session instanceof SessionCore) {
      SessionCore intermediateSession = (SessionCore) session;
      appId = intermediateSession.getAppId();
      return this.isFilter(appId, targetFlag, session);
    } else {
      UbiSession ubiSession = (UbiSession) session;
      appId = ubiSession.getFirstAppId();
      return this.isFilter(appId, targetFlag, session);
    }
  }

  private boolean isFilter(Integer appId, Integer targetFlag, Object session) {

    if (targetFlag != null && appId != null) {
      Set<Integer> botFlags = appIdWithBotFlags.get(appId);
      if (botFlags != null && botFlags.contains(targetFlag)) {
        return true;
      } else {
        return false;
      }
    }

    return UbiSessionHelper.isNonIframRdtCountZero(session)
        && invalidSessionBotFilter.contains(targetFlag);
  }

}
