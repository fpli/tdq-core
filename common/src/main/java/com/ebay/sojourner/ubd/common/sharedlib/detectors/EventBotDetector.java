package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.*;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class EventBotDetector implements BotDetector<UbiEvent> {

    private static volatile EventBotDetector eventBotDetector;
    private Set<Rule> botRules = new LinkedHashSet<Rule>();

    private EventBotDetector() {
        initBotRules();
        for (Rule rule : botRules) {
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
    public Set<Integer> getBotFlagList(UbiEvent ubiEvent) throws IOException, InterruptedException {
        Set<Integer> botRuleList = new LinkedHashSet<Integer>(botRules.size());
        for (Rule rule : botRules) {
            Integer botRule = rule.getBotFlag(ubiEvent);
            if (botRule != 0) {
                botRuleList.add(botRule);
            }
        }
        return botRuleList;
    }

    @Override
    public void initBotRules() {
        botRules.add(new BotRule1());

    }
}
