package com.ebay.sojourner.business.rule;

import static com.ebay.sojourner.common.util.BotRules.MANY_FAST_EVENTS_BOT_FLAG;
import static com.ebay.sojourner.common.util.BotRules.NON_BOT_FLAG;

import com.ebay.sojourner.common.model.UbiSession;
import java.util.Set;

public class BotRule12End extends AbstractBotRule<UbiSession> {

  public static final int TOTAL_INTERVAL_MICRO_SEC = 750000; // ms

  @Override
  public int getBotFlag(UbiSession ubiSession) {
    Long[] minMaxEventTtimestamp = ubiSession.getMinMaxEventTimestamp();
    Long start = minMaxEventTtimestamp[0];
    Long end = minMaxEventTtimestamp[1];
    Integer eventCount = ubiSession.getEventCnt();
    Set<Integer> sessionBotFlagList = ubiSession.getBotFlagList();

    int botFlag = NON_BOT_FLAG;
    if (!sessionBotFlagList.contains(MANY_FAST_EVENTS_BOT_FLAG)) {
      if ((start != 0L) && (end != 0L) && (eventCount > 1)) {
        Long duration = Math.abs(end - start);

        if (duration > 0 && duration <= (long) TOTAL_INTERVAL_MICRO_SEC * (eventCount - 1)) {
          botFlag = MANY_FAST_EVENTS_BOT_FLAG;
        }
      }
    } else {
      botFlag = MANY_FAST_EVENTS_BOT_FLAG;
    }

    return botFlag;
  }
}
