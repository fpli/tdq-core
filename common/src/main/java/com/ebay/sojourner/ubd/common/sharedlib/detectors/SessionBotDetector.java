package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.*;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class SessionBotDetector implements BotDetector<UbiSession> {

    private static SessionBotDetector sessionBotDetector;
    private Set<Rule> botRules = new LinkedHashSet<Rule>();
    private SessionBotDetector() {
        initBotRules();
        for (Rule rule:botRules)
        {
            rule.init();
        }
    }
    public static SessionBotDetector getInstance() {
        if (sessionBotDetector == null) {
            synchronized (SessionBotDetector.class) {
                if (sessionBotDetector == null) {
                    sessionBotDetector = new SessionBotDetector();
                }
            }
        }
        return sessionBotDetector;
    }

    @Override
    public Set<Integer> getBotFlagList(UbiSession ubiSession) throws IOException, InterruptedException {
        Set<Integer> botRuleList = new LinkedHashSet<Integer>(botRules.size());
        for (Rule rule:botRules)
        {
            Integer botRule =rule.getBotFlag(ubiSession);
            if(botRule!=0) {
                botRuleList.add(botRule);
            }
        }
        return botRuleList;
    }

    @Override
    public void initBotRules() {
        botRules.add(new BotRule15());
        botRules.add(new BotRule9());
        botRules.add(new BotRule10());
        botRules.add(new BotRule12());
        botRules.add(new BotRule203());
        botRules.add(new BotRule204());
        botRules.add(new BotRule205());
        botRules.add(new BotRule206());
        botRules.add(new BotRule207());
        botRules.add(new BotRule208());
        botRules.add(new BotRule212());
        botRules.add(new BotRule215());
        botRules.add(new BotRule11());
    }
}
