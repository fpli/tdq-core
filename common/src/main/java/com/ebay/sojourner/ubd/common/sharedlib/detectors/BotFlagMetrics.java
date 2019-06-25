package com.ebay.sojourner.ubd.common.sharedlib.detectors;


import com.ebay.sojourner.ubd.common.rule.BotFilter;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import org.apache.log4j.Logger;

import java.util.LinkedHashSet;

public class BotFlagMetrics  {
    private static final Logger log = Logger.getLogger(BotFlagMetrics.class);

    private static LinkedHashSet<BotDetector> botDetectors = null;
    private BotFilter filter = null;
    private  UBIConfig ubiConfig ;

    private static BotFlagMetrics botFlagMetrics;

    private BotFlagMetrics() {
//        initBotDetectors();

    }
    public static BotFlagMetrics getInstance() {
        if (botFlagMetrics == null) {
            synchronized (BotFlagMetrics.class) {
                if (botFlagMetrics == null) {
                    botFlagMetrics = new BotFlagMetrics();
                }
            }
        }
        return botFlagMetrics;
    }

//    @Override
//    public void start(SessionAccumulator sessionAccumulator) throws Exception {
//        // As last day open session has already been detected as bot
//        if (isNotBot(sessionAccumulator.getUbiSession())) {
//            reset();
//
//        }
//    }
//
//    private void initBotDetectors()
//    {
//        botDetectors.add(EventBotDetector.getInstance());
//        botDetectors.add(SessionBotDetector.getInstance());
//
//    }
//
//
//    public boolean isNotBot(UbiSession session) {
//        Integer botFlag = session.getBotFlag();
//        return botFlag == null || botFlag == 0;
//    }
//
//    @Override
//    public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
//        if (isNotBot(sessionAccumulator.getUbiSession())) {
//            for (Rule botRule : botRules) {
//                botRule.feed(event,sessionAccumulator);
//            }
//        }
//    }
//
////    public void feed(UbiEvent event,SessionAccumulator sessionAccumulator) {
////        for (Rule botRule : botRules) {
////            botRule.feed(event,sessionAccumulator);
////        }
////    }
//
//    @Override
//    public void end(SessionAccumulator sessionAccumulator) throws Exception {
//        if (isNotBot(sessionAccumulator.getUbiSession())) {
//            sessionAccumulator.getUbiSession().setBotFlag(getBotFlag(sessionAccumulator.getUbiSession()));
//        }
//    }
//
//    public int getBotFlag(UbiSession session) throws Exception {
//        for (Rule bot : botRules) {
//            Integer botFlag = bot.getBotFlag(session);
//            if (botFlag != null && botFlag != BotRules.NON_BOT_FLAG) {
//                if (!filter.filter(session, botFlag)) {
//                    return botFlag;
//                } else {
//                    StringBuilder iphoneInfoBuilder = new StringBuilder();
//                    iphoneInfoBuilder.append("Detect iphone session:");
//                    iphoneInfoBuilder.append(session.getGuid());
//                    iphoneInfoBuilder.append("|");
//                    iphoneInfoBuilder.append(session.getSessionId());
//                    iphoneInfoBuilder.append("|");
//                    iphoneInfoBuilder.append(session.getFirstAppId());
//                    iphoneInfoBuilder.append("|");
//                    iphoneInfoBuilder.append(botFlag);
//                    log.debug(iphoneInfoBuilder.toString());
//                }
//            }
//        }
//
//        return BotRules.NON_BOT_FLAG;
//    }
//
//    @Override
//    public void init() throws Exception {
//
//        ubiConfig = UBIConfig.getInstance();
//        setBotRules(new LinkedHashSet<Rule>());
//        setBotFilter(new BotFilter(ubiConfig));
//
//        String botRulePackage = ubiConfig.getString(Property.BOT_RULE_PACKAGE) ;
//        String botRuleClasses = ubiConfig.getString(Property.BOT_RULE_CLASSES) ;
//        Collection<String> botRuleNames = PropertyUtils.parseProperty(botRuleClasses, Property.BOT_DELIMITER);
//        for (String botRuleName : botRuleNames) {
//            String botRulePath = new StringBuilder(botRulePackage).append(".").append(botRuleName).toString();
//            Class<?> botRuleClass = Thread.currentThread().getContextClassLoader().loadClass(botRulePath);
//
//            try {
//                Constructor<?> constructor = botRuleClass.getConstructor();
//                Object botRuleInstance = constructor.newInstance();
//
//                if (botRuleInstance instanceof Rule)
//                    botRules.add(Rule.class.cast(botRuleInstance));
//            } catch (Exception e) {
//                Object botRuleInstance = botRuleClass.newInstance();
//
//                if (botRuleInstance instanceof Rule)
//                    botRules.add(Rule.class.cast(botRuleInstance));
//            }
//        }
//    }
//
//    void setBotRules(LinkedHashSet<Rule> botRules) {
//        this.botRules = botRules;
//    }
    
    void setBotFilter(BotFilter filter) {
        this.filter = filter;
    }
}
