package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.*;
import org.apache.log4j.Logger;

public class IpIndicator extends RecordMetrics<UbiSession, IpAttributeAccumulator> {

    private static Logger logger = Logger.getLogger(SessionMetrics.class);

    private static IpIndicator ipIndicator ;

    public static IpIndicator getInstance() {
        if (ipIndicator == null) {
            synchronized (IpIndicator.class) {
                if (ipIndicator == null) {
                    ipIndicator = new IpIndicator();
                }
            }
        }
        return ipIndicator;
    }
    public IpIndicator()  {

        initFieldMetrics();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void initFieldMetrics() {

        addFieldMetrics(new SingleClickSessionCountIndicator());


    }
}
