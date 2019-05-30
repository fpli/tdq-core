package com.ebay.sojourner.ubd.common.rule;


import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotRules;

import java.util.List;
import java.util.regex.Pattern;

public class BotRule12 implements Rule<UbiSession> {

    public static final int TOTAL_INTERVAL_MICRO_SEC = 750000; // ms

    @Override
    public void init() {


    }

    @Override
    public int getBotFlag(UbiSession ubiSession) {
        Long[] minMaxEventTtimestamp = ubiSession.getMinMaxEventTimestamp();
        Long start = minMaxEventTtimestamp[0];
        Long end = minMaxEventTtimestamp[1];
        Integer eventCount = ubiSession.getEventCnt();
        List<Integer> sessionBotFlagList = ubiSession.getBotFlagList();
        if (sessionBotFlagList.get(0) == 0) {
            if (start != 0L && end != 0L && eventCount > 1) {
                Long duration = Math.abs(end - start);
                if (duration <= (long) TOTAL_INTERVAL_MICRO_SEC * (eventCount - 1)) {
                    return BotRules.MANY_FAST_EVENTS_BOT_FLAG;
                }
                else
                {
                    return BotRules.NON_BOT_FLAG;
                }
            }
            else
            {
                return BotRules.NON_BOT_FLAG;
            }
        } else if (sessionBotFlagList.get(0) == 12) {
            return BotRules.MANY_FAST_EVENTS_BOT_FLAG;
        }


        return BotRules.NON_BOT_FLAG;

    }

}
