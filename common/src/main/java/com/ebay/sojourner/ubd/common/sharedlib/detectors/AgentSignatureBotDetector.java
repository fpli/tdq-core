package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.rule.BotRule6;
import com.ebay.sojourner.ubd.common.rule.BotRuleForDeclarativeAgent;
import com.ebay.sojourner.ubd.common.rule.BotRuleForSuspectAgent;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class AgentSignatureBotDetector implements BotDetector<AgentAttribute> {

    private static AgentSignatureBotDetector agentIpSignatureBotDetector;
    private Set<Rule> botRules = new LinkedHashSet<Rule>();


    private AgentSignatureBotDetector() {

        initBotRules();
        for (Rule rule : botRules) {
            rule.init();
        }
    }

    public static AgentSignatureBotDetector getInstance() {
        if (agentIpSignatureBotDetector == null) {
            synchronized (AgentSignatureBotDetector.class) {
                if (agentIpSignatureBotDetector == null) {
                    agentIpSignatureBotDetector = new AgentSignatureBotDetector();
                }
            }
        }
        return agentIpSignatureBotDetector;
    }

    @Override
    public Set<Integer> getBotFlagList(AgentAttribute agentAttribute) throws IOException, InterruptedException {
        Set<Integer> signature = null;
        Set<Integer> botflagSet = new HashSet<Integer>();
        if (agentAttribute != null) {
            for (Rule rule : botRules) {
                int botFlag = rule.getBotFlag(agentAttribute);
                if (botFlag != 0) {
                    botflagSet.add(botFlag);
                }
            }
        }

        return botflagSet;
    }

    @Override
    public void initBotRules() {
        botRules.add(new BotRule6());
        botRules.add(new BotRuleForSuspectAgent());
        botRules.add(new BotRuleForDeclarativeAgent());
    }

    private Set<Integer> scanSignature(String inColumnName, String inColumnValue, String outColumnName, String bucketName) {
        return CouchBaseManager.getInstance().getSignatureWithColumn(inColumnName, inColumnValue, outColumnName);
    }


}
