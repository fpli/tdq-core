package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.util.Set;

public class IpBotRule implements Rule<IpSignature> {

    public static final int TOTAL_INTERVAL_MICRO_SEC = 750000; // ms
    private QueryableStateClient client = null;
    private String proxyHost = "127.0.0.1";
    private int proxyPort = 9069;
    @Override
    public void init() {

    }

    @Override
    public  int getBotFlag(IpSignature ipSignature) {
        Set<Integer> botFlagSet= ipSignature.getBotFlag();
        if(botFlagSet!=null&&botFlagSet.contains(7)) {
            return 7;
        }
        return 0;
    }


}
