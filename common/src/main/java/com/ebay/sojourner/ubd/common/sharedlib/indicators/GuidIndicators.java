package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.SessionMetrics;
import org.apache.log4j.Logger;

public class GuidIndicators extends AttributeIndicators<UbiSession, GuidAttributeAccumulator> {

    private static Logger logger = Logger.getLogger(SessionMetrics.class);

    private static GuidIndicators guidIndicators;

    public static GuidIndicators getInstance() {
        if (guidIndicators == null) {
            synchronized (GuidIndicators.class) {
                if (guidIndicators == null) {
                    guidIndicators = new GuidIndicators();
                }
            }
        }
        return guidIndicators;
    }

    public GuidIndicators() {

        initIndicators();

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void initIndicators() {

        addIndicators(new AbsEventCountIndicator());
    }

}
