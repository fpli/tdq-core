package com.ebay.sojourner.ubd.common.sharedlib.detector;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.BotRule1;
import com.ebay.sojourner.ubd.common.rule.Rule;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class EventBotDetector implements BotDetector<UbiEvent> {

    private static EventBotDetector eventBotDetector;
    private LinkedHashSet<Rule> botRules = null;
    private EventBotDetector() {
       initBotRules();
        for (Rule rule:botRules)
        {
            rule.init();
        }
    }
    public static EventBotDetector getInstance() {
        if (eventBotDetector == null) {
            synchronized (EventBotDetector.class) {
                if (eventBotDetector == null) {
                    eventBotDetector = new EventBotDetector();
                }
            }
        }
        return eventBotDetector;
    }

    @Override
    public List<Integer> getBotFlagList(UbiEvent ubiEvent) {
        List<Integer> botRuleList = new ArrayList<Integer>(botRules.size());
        for (Rule rule:botRules)
        {
            Integer botRule =rule.getBotFlag(ubiEvent);
            botRuleList.add(botRule);
        }
        return botRuleList;
    }

    @Override
    public void initBotRules() {
        botRules.add(new BotRule1());

    }
}
