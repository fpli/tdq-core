package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;

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
    private final Map<Integer, Set<Integer>> appIdWithBotFlags;

    public UbiBotFilter() {
        appIdWithBotFlags = getAppIdWithBotFlags(LkpFetcher.getInstance().getAppIds());
        invalidSessionBotFilter = PropertyUtils.getIntegerSet(UBIConfig.getString(Property.INVALID_BOT_FILTER), Property.PROPERTY_DELIMITER);
    }

    @Override
    public boolean filter(UbiSession ubiSession, Integer targetFlag) {
        if (ubiSession.getAppId() != null) {
            Set<Integer> botFlags = appIdWithBotFlags.get(ubiSession.getAppId());
            if (botFlags != null && botFlags.contains(targetFlag)) {
                return true;
            } else {
                return false;
            }
        }

        if (UbiSessionHelper.isNonIframRdtCountZero(ubiSession) && invalidSessionBotFilter.contains(targetFlag)) {
            return true;
        }
        return false;
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
}
