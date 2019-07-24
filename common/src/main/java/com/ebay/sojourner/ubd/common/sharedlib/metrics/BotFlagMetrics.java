package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.common.datum.UbiEvent;
import com.ebay.sojourner.common.datum.UbiSession;
import com.ebay.sojourner.constant.BotRules;
import com.ebay.sojourner.constant.Property;
import com.ebay.sojourner.intraday.session.bot.BotRule;
import com.ebay.sojourner.intraday.session.filter.BotFilter;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.BotFilter;
import com.ebay.sojourner.utils.PropertyUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashSet;

public class BotFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
    private static final Logger log = Logger.getLogger(BotFlagMetrics.class);

    private LinkedHashSet<BotRule> botRules = null;
    private BotFilter filter = null;

    @Override
    public void start(SessionAccumulator sessionAccumulator) throws Exception {
        // As last day open session has already been detected as bot
        if (isNotBot(session)) {
            reset();
            feed(event);
        }
    }
    
    public void reset() throws Exception {
        for (BotRule botRule : botRules) {
            botRule.reset();
        }
        filter.cleanup();
    }
    
    public boolean isNotBot(UbiSession session) {
        Integer botFlag = session.getBotFlag();
        return botFlag == null || botFlag == 0;
    } 

    @Override
    public void feed(UbiEvent event, UbiSession session) {
        if (isNotBot(session)) {
            feed(event);
        }
    }
    
    public void feed(UbiEvent event) {
        for (BotRule botRule : botRules) {
            botRule.feed(event);
        }
    }

    @Override
    public void end(UbiSession session) throws Exception {
        if (isNotBot(session)) {
            session.setBotFlag(getBotFlag(session));
        }
    }
    
    public int getBotFlag(UbiSession session) throws Exception {
        for (BotRule bot : botRules) {
            Integer botFlag = bot.getBotFlag(session);
            if (botFlag != null && botFlag != BotRules.NON_BOT_FLAG) {
                if (!filter.filter(session, botFlag)) {
                    return botFlag;
                } else {
                    StringBuilder iphoneInfoBuilder = new StringBuilder();
                    iphoneInfoBuilder.append("Detect iphone session:");
                    iphoneInfoBuilder.append(session.getGuid());
                    iphoneInfoBuilder.append("|");
                    iphoneInfoBuilder.append(session.getSessionSkey());
                    iphoneInfoBuilder.append("|");
                    iphoneInfoBuilder.append(session.getFirstAppId());
                    iphoneInfoBuilder.append("|");
                    iphoneInfoBuilder.append(botFlag);
                    log.debug(iphoneInfoBuilder.toString());
                }
            }
        }
        
        return BotRules.NON_BOT_FLAG;
    }

    @Override
    public void init(Configuration conf) throws Exception {
        setBotRules(new LinkedHashSet<BotRule>());
        setBotFilter(new BotFilter(conf));
        
        String botRulePackage = conf.get(Property.BOT_RULE_PACKAGE);
        String botRuleClasses = conf.get(Property.BOT_RULE_CLASSES);
        Collection<String> botRuleNames = PropertyUtils.parseProperty(botRuleClasses, Property.BOT_DELIMITER);
        for (String botRuleName : botRuleNames) {
            String botRulePath = new StringBuilder(botRulePackage).append(".").append(botRuleName).toString();
            Class<?> botRuleClass = Thread.currentThread().getContextClassLoader().loadClass(botRulePath);
            
            try {
                Constructor<?> constructor = botRuleClass.getConstructor(Configuration.class);
                Object botRuleInstance = constructor.newInstance(conf);
                
                if (botRuleInstance instanceof BotRule)
                    botRules.add(BotRule.class.cast(botRuleInstance));
            } catch (Exception e) {
                Object botRuleInstance = botRuleClass.newInstance();
                
                if (botRuleInstance instanceof BotRule)
                    botRules.add(BotRule.class.cast(botRuleInstance));
            }
        }
    }
    
    void setBotRules(LinkedHashSet<BotRule> botRules) {
        this.botRules = botRules;
    }
    
    void setBotFilter(BotFilter filter) {
        this.filter = filter;
    }
}
