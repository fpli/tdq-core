package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuidIndicators extends AttributeIndicators<UbiSession, GuidAttributeAccumulator> {

    private static volatile GuidIndicators guidIndicators;

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
            log.error(e.getMessage());
        }
    }

    @Override
    public void initIndicators() {

        addIndicators(new AbsEventCountIndicator());
    }

}
