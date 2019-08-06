package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.GuidAttribute;
import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.rule.BotRule7;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sharedlib.connectors.CouchBaseManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class GuidSignatureBotDetector implements BotDetector<GuidAttribute> {

    private static GuidSignatureBotDetector singnatureBotDetector;
    private Set<Rule> botRules = new LinkedHashSet<Rule>();


    private GuidSignatureBotDetector() {

        initBotRules();
        for (Rule rule : botRules) {
            rule.init();
        }
    }

    public static GuidSignatureBotDetector getInstance() {
        if (singnatureBotDetector == null) {
            synchronized (GuidSignatureBotDetector.class) {
                if (singnatureBotDetector == null) {
                    singnatureBotDetector = new GuidSignatureBotDetector();
                }
            }
        }
        return singnatureBotDetector;
    }

    @Override
    public Set<Integer> getBotFlagList(GuidAttribute guidAttribute) throws IOException, InterruptedException {
        Set<Integer> signature = null;
        Set<Integer> botflagSet = new HashSet<Integer>();
//        if (ubiSession.getClientIp() != null) {
//            signature = scanSignature("ip",ubiSession.getClientIp(),"botFlag","botsignature");
//
//        }
        if (guidAttribute != null) {
            for (Rule rule : botRules) {
                int botFlag = rule.getBotFlag(guidAttribute);
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
    }

    private Set<Integer> scanSignature(String inColumnName, String inColumnValue, String outColumnName, String bucketName) {
        return CouchBaseManager.getInstance().getSignatureWithColumn(inColumnName, inColumnValue, outColumnName);
    }


}
