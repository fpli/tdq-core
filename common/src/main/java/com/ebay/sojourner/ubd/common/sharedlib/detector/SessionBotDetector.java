package com.ebay.sojourner.ubd.common.sharedlib.detector;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.BotRule12;
import com.ebay.sojourner.ubd.common.rule.Rule;

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
    public Set<Integer> getBotFlagList(UbiSession ubiSession) {
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
        botRules.add(new BotRule12());

    }
}
