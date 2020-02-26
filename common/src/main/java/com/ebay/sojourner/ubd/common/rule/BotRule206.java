package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.util.Set;

public class BotRule206 extends AbstractBotRule<UbiSession> {

    private Set<Integer> lndgPageIds;

    @Override
    public void init() {
        lndgPageIds = PropertyUtils.getIntegerSet(UBIConfig.getString(Property.LNDG_PAGE_IDS), Property.PROPERTY_DELIMITER);
    }

    @Override
    public int getBotFlag(UbiSession session) {
        if (session.getAgentString() == null && session.getMaxScsSeqNum() <= 5 && lndgPageIds.contains(session.getLndgPageId())) {
            return BotRules.SHORT_SESSION_WITHOUT_AGENT;
        }
        return 0;
    }

}
