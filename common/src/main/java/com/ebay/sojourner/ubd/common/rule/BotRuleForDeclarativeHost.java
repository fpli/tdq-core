package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.IpAttribute;
import com.ebay.sojourner.ubd.common.util.BotHostMatcher;
import com.ebay.sojourner.ubd.common.util.BotHostQuerier;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BotRuleForDeclarativeHost implements Rule<IpAttribute> {
//    public static final int MAX_CGUID_THRESHOLD = 5;
    public static final int SESSION_COUNT_THRESHOLD = 300;
//    public static final int DEFAULT_THREAD_SIZE = 8;
//    public static final int DEFAULT_TIME_OUT_SEC = 30 * 60;
//    BotHostQuerier querier = new BotHostQuerier(DEFAULT_THREAD_SIZE);

    private BotHostMatcher botHostMatcher = BotHostMatcher.INSTANCE;

    @Override
    public int getBotFlag(IpAttribute ipAttribute) throws InterruptedException {
        if ((ipAttribute.getIsAllAgentHoper() && ipAttribute.getTotalCnt() > SESSION_COUNT_THRESHOLD) || ipAttribute.getTotalCntForSec1() > SESSION_COUNT_THRESHOLD) {
            String ip = ipAttribute.getClientIp();
            if (StringUtils.isNotBlank(ip) && botHostMatcher.isBotIp(ip)) {
                return 222;
            }

//            final Map<String, Boolean> itemsOut = new ConcurrentHashMap<>();
//            final CountDownLatch latch = new CountDownLatch(1);
//            BotHostQuerier.Callback cb = new BotHostQuerier.Callback() {
//                @Override
//                public void postProcess(String ip, boolean isBotIp) {
//                    itemsOut.put(ip, isBotIp);
//                    latch.countDown();
//                }
//            };
//            querier.startQuery(ip, cb);
//            latch.await(DEFAULT_TIME_OUT_SEC, TimeUnit.SECONDS);
//            if (itemsOut.size() > 0) {
//                for (Map.Entry<String, Boolean> elment : itemsOut.entrySet()) {
//                    if (elment.getValue()) {
//                        return 222;
//                    }
//                }
//            }
        }

        return 0;
    }

    @Override
    public void init() {

    }

}
