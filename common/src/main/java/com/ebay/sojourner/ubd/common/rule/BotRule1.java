package com.ebay.sojourner.ubd.common.rule;


import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

import java.util.Map;
import java.util.regex.Pattern;

public class BotRule1 implements Rule<UbiEvent> {
    //    private int botFlag = 0;
    private static final Pattern pattern = Pattern.compile(".*bot[^a-z0-9_-].*|.*bot$|.*spider.*|.*crawl.*|.*ktxn.*", Pattern.CASE_INSENSITIVE);
//    private boolean findValidEvent = false;
//    private boolean first = true;

    @Override
    public void init() {


    }

    private int detectSpiderAgent(UbiEvent event) {
        String agentInfo = event.getAgentInfo();
        if (agentInfo != null && pattern.matcher(agentInfo).matches()) {
            return BotRules.SPIDER_BOT_FLAG;
        } else {
            return BotRules.NON_BOT_FLAG;
        }
    }

    boolean isValidEvent(UbiEvent event) {
        return (event.getIframe() != null && event.getRdt() != null && event.getIframe() == 0 && event.getRdt() == 0);
    }


    @Override
    public int getBotFlag(UbiEvent ubiEvent) {

        int botFlag = detectSpiderAgent(ubiEvent);
        return botFlag;

    }

}
