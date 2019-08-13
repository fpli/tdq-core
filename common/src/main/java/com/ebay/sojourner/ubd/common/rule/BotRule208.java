package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

public class BotRule208 implements Rule<UbiSession> {

    private static Set<Integer> cobrandSet;
    private static UBIConfig ubiConfig;

    @Override
    public void init() {
        InputStream resourceAsStream = BotRule208.class.getResourceAsStream("/ubi.properties");
        ubiConfig = UBIConfig.getInstance(resourceAsStream);
        cobrandSet = PropertyUtils.getIntegerSet(ubiConfig.getString(Property.EBAY_SITE_COBRAND), Property.PROPERTY_DELIMITER);
    }

    @Override
    public int getBotFlag(UbiSession session) {
        if (session.getAgentString() == null && session.getSiidCnt() == 0 && !cobrandSet.contains(session.getCobrand())) {
            return BotRules.DIRECT_ACCESS_BOTFLAG;
        }
        return 0;
    }

}
