package com.ebay.sojourner.ubd.common.rule;


import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.BotRules;

import java.util.regex.Pattern;

public class BotRule1 extends AbstractBotRule<UbiEvent> {

    private static final Pattern pattern = Pattern.compile(".*bot[^a-z0-9_-].*|.*bot$|.*spider.*|.*crawl.*|.*ktxn.*", Pattern.CASE_INSENSITIVE);

    private int detectSpiderAgent(UbiEvent event) {
        String agentInfo = event.getAgentInfo();
        if (agentInfo != null && pattern.matcher(agentInfo).matches()) {
            return BotRules.SPIDER_BOT_FLAG;
        } else {
            return BotRules.NON_BOT_FLAG;
        }
    }

    @Override
    public int getBotFlag(UbiEvent ubiEvent) {
        return detectSpiderAgent(ubiEvent);
    }

}
