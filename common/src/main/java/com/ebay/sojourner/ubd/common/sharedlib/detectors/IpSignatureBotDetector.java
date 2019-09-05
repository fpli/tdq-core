package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.rule.BotRule7;
import com.ebay.sojourner.ubd.common.rule.BotRuleForDeclarativeHost;
import com.ebay.sojourner.ubd.common.rule.BotRuleForSuspectIP;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class IpSignatureBotDetector implements BotDetector<IpAttribute> {

    private static IpSignatureBotDetector singnatureBotDetector;
    private Set<Rule> botRules = new LinkedHashSet<Rule>();


    private IpSignatureBotDetector() {

        initBotRules();
        for (Rule rule : botRules) {
            rule.init();
        }
    }

    public static IpSignatureBotDetector getInstance() {
        if (singnatureBotDetector == null) {
            synchronized (IpSignatureBotDetector.class) {
                if (singnatureBotDetector == null) {
                    singnatureBotDetector = new IpSignatureBotDetector();
                }
            }
        }
        return singnatureBotDetector;
    }

    @Override
    public Set<Integer> getBotFlagList(IpAttribute ipAttribute) throws IOException, InterruptedException {
        Set<Integer> signature = null;
        Set<Integer> botflagSet = new HashSet<Integer>();
//        if (ubiSession.getClientIp() != null) {
//            signature = scanSignature("ip",ubiSession.getClientIp(),"botFlag","botsignature");
//
//        }
        if (ipAttribute != null) {
            for (Rule rule : botRules) {
                int botFlag = rule.getBotFlag(ipAttribute);
                if (botFlag != 0) {
                    botflagSet.add(botFlag);
                }
            }
        }

        return botflagSet;
    }

    @Override
    public void initBotRules() {
        botRules.add(new BotRule7());
        botRules.add(new BotRuleForSuspectIP());
        botRules.add(new BotRuleForDeclarativeHost());

    }

    private Set<Integer> scanSignature(String inColumnName, String inColumnValue, String outColumnName, String bucketName) {
//        return CouchBaseManager.getInstance().getSignatureWithColumn(inColumnName, inColumnValue, outColumnName);
        return null;
    }


}
