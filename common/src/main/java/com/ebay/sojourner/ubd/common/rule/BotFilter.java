package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.util.HashSet;
import java.util.Set;

public class BotFilter implements ValueFilter<UbiSession, Integer> {
    
    Set<Integer> invalidSessionBotFilter = null;
    Set<String> appIdWithBotFlags = new HashSet<String>();
    LkpFetcher lkpFetcher;
    public BotFilter(UBIConfig ubiConfig) {
        lkpFetcher=LkpFetcher.getInstance();
        lkpFetcher.loadAppIds();
        appIdWithBotFlags.addAll(lkpFetcher.getAppIds());
        invalidSessionBotFilter = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.INVALID_BOT_FILTER), Property.PROPERTY_DELIMITER);
    }
    
    @Override
    public boolean filter(UbiSession session, Integer botFlag) {
        Integer appId = session.getFirstAppId();
        if (botFlag != null && appId != null) {
            StringBuilder appIdOnBotFlag = new StringBuilder();
            appIdOnBotFlag.append(appId.toString().trim())
                          .append(LkpFetcher.LKP_FILED_DELIMITER)
                          .append(botFlag.toString().trim());
            System.out.print("###QQQ###"+appIdOnBotFlag.toString());
            System.out.print("###SSS###"+appIdWithBotFlags);
            if (appIdWithBotFlags.contains(appIdOnBotFlag.toString())) {
                return true;
            }
        }
        
        if (session.getNonIframeRdtEventCnt() != null && session.getNonIframeRdtEventCnt() == 0 && invalidSessionBotFilter.contains(botFlag)){
            return true;
        }

        return false;
    }

    @Override
    public void cleanup() {
        lkpFetcher.clearAppId();
    }
}
